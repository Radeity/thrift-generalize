package com.fdu.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fdu.server.Service.Services;
import com.fdu.server.genJava.generalize.*;
import org.apache.thrift.TException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class ServiceHandlerImpl implements DIYFrameworkService.Iface {

    @Override
    public Response send(Request request) throws TException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String serviceName = request.getServiceName();
        byte[] paramJSON = request.getParamJSON();

        JSONObject jsonObject = null;
        if(paramJSON!=null && paramJSON.length > 0) {
            String paramsString = new String(paramJSON);
            jsonObject = JSONObject.parseObject(paramsString);
        }

        /**
         *  建议所有服务封装到一个接口（Services）中
         */
        //Class<Services> servicesClass = Services.class;
        Object servicesObject = null;
        Method targetMethod = null;
        Object trueParam = null;
     //   Class trueType = null;

        List<Class> clazzs = getAllInterfaceAchieveClass(Services.class);
        for(Class c : clazzs) {
            Method[] declaredMethods = c.getDeclaredMethods();
            for(Method method : declaredMethods) {
                String funcName = method.getName();

                /** 每个服务均用一个对象接受参数 **/
                Class[] paramTypes = method.getParameterTypes();
                Parameter[] parameters = method.getParameters();
                int paramCnt =  method.getParameterCount();

                /** 不支持多态 */
                if(funcName.equals(serviceName)) {
                    servicesObject = c.newInstance();
                    if(paramCnt != 0) {
                        targetMethod = method;
                        // trueType = paramTypes[0];
                        try {
                            trueParam = JSON.toJavaObject(jsonObject, paramTypes[0]);
                        } catch (JSONException e) {
                            throw new ServiceException(EXCCODE.PARAMNOTFOUND, "Param Not Found!");
                        }
                    }
                    break;
                }
            }
        }

        // 未找到方法抛出异常
        if(servicesObject == null) {
            throw new ServiceException(EXCCODE.SERVICENOTFOUND, "Service Not Found!");
        }

        // 执行方法
        Object responsePojo = targetMethod.invoke(servicesObject, trueParam);

        String returnString = JSON.toJSONString(responsePojo);
        byte[] returnBytes = returnString.getBytes();

        Response response = new Response(RESCODE._200, null);
        response.setResponseJSON(returnBytes);
        return response;

//        return null;
    }

    public static List<Class> getAllInterfaceAchieveClass(Class clazz){
        ArrayList<Class> list = new ArrayList<>();
        //判断是否是接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClassByPath(clazz.getPackage().getName());
                /**
                 * 循环判断路径下的所有类是否实现了指定的接口
                 * 并且排除接口类自己
                 */
                for (int i = 0; i < allClass.size(); i++) {

                    //排除抽象类
                    if(Modifier.isAbstract(allClass.get(i).getModifiers())){
                        continue;
                    }
                    //判断是不是同一个接口
                    if (clazz.isAssignableFrom(allClass.get(i))) {
                        if (!clazz.equals(allClass.get(i))) {
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("出现异常");
            }
        }
        return list;
    }

    public static ArrayList<Class> getAllClassByPath(String packagename){
        ArrayList<Class> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packagename.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (int i = 0; i < fileList.size(); i++) {
                list.addAll(findClass(fileList.get(i),packagename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ArrayList<Class> findClass(File file,String packagename) {
        ArrayList<Class> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                assert !file2.getName().contains(".");//添加断言用于判断
                ArrayList<Class> arrayList = findClass(file2, packagename+"."+file2.getName());
                list.addAll(arrayList);
            }else if(file2.getName().endsWith(".class")){
                try {
                    //保存的类文件不需要后缀.class
                    list.add(Class.forName(packagename + '.' + file2.getName().substring(0,
                            file2.getName().length()-6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
