package com.fdu.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fdu.server.Service.Services;
import com.fdu.server.genJava.generalize.*;
import org.apache.thrift.TException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


public class ServiceHandlerImpl implements DIYFrameworkService.Iface {
    @Override
    public Response send(Request request) throws TException, InvocationTargetException, IllegalAccessException {
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
        Class<Services> servicesClass = Services.class;
        Services servicesObject = null;
        Method targetMethod = null;
        Object trueParam = null;
     //   Class trueType = null;

        Method[] declaredMethods = servicesClass.getDeclaredMethods();
//        System.out.println(declaredMethods);
        for(Method method : declaredMethods) {
            String funcName = method.getName();

            /** 每个服务均用一个对象接受参数 **/
            Class[] paramTypes = method.getParameterTypes();
            Parameter[] parameters = method.getParameters();
            int paramCnt =  method.getParameterCount();
//            for(int i=0; i < paramCnt; i++) {
//                System.out.println(paramTypes[i].getName()+" "+parameters[i].getName());
//            }

            /** 不支持多态 */
            if(funcName.equals(serviceName)) {
                servicesObject = new Services();
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
}
