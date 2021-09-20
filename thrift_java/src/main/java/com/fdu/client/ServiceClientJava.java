package com.fdu.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fdu.server.genJava.generalize.DIYFrameworkService;
import com.fdu.server.genJava.generalize.Request;
import com.fdu.server.genJava.generalize.Response;
import com.fdu.server.genJava.generalize.ServiceException;
import com.fdu.server.handler.ServiceHandlerImpl;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.HashMap;
import java.util.Map;

public class ServiceClientJava {
    /**
     * 启动客户端远程调用Java服务
     */
    public static void main(String[] args) {
        System.out.println("客户端启动....");
        TTransport transport = null;
        try {
            transport = new TSocket("127.0.0.1", 9898);
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            DIYFrameworkService.Client client = new DIYFrameworkService.Client(protocol);
            transport.open();

            // 创建请求对象Request
            Request request = new Request(null, "saySB"); // Java request

            Map<String,String> map = new HashMap<String,String>();
            map.put("param", "thrift");

            String jsonString = JSON.toJSONString(map);
            byte[] bytes = jsonString.getBytes();
            request.setParamJSON(bytes);

            ServiceHandlerImpl serviceHandler = new ServiceHandlerImpl();
            //Response response = serviceHandler.send(request);
            Response response = client.send(request);
            System.out.println(response.getResponeCode().getValue());
            String responseString = new String(response.getResponseJSON());
            System.out.println(JSONObject.parseObject(responseString));

        } catch (ServiceException e) {
            System.out.println(e.getExceptionMess());
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException te) {
            te.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }
}