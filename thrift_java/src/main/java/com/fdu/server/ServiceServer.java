package com.fdu.server;


import com.fdu.server.genJava.generalize.DIYFrameworkService;
import com.fdu.server.handler.ServiceHandlerImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class ServiceServer {
    /**
     * 启动thrift服务器
     */
    public static void main(String[] args) {
        try {
            System.out.println("服务端开启....");
            // 1.创建TProcessor
            TProcessor tprocessor = new DIYFrameworkService.Processor<DIYFrameworkService.Iface>(new ServiceHandlerImpl());
            TServerSocket serverTransport = new TServerSocket(9898);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(tprocessor));
            server.serve();
//            TServer.Args tArgs = new TServer.Args(serverTransport);
//            tArgs.processor(tprocessor);
//            tArgs.protocolFactory(new TBinaryProtocol.Factory());
//            TServer server = new TSimpleServer(tArgs);
//            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}