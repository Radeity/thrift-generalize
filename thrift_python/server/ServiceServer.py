import json

from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from thrift.transport import TSocket, TTransport

from server.genPy import DIYFrameworkService
from server.genPy.ttypes import Request
from server.handler.ServiceHandlerImpl import ServiceHandlerImpl

if __name__ == '__main__':
    serviceHandler = ServiceHandlerImpl()
    processor = DIYFrameworkService.Processor(serviceHandler)
    transport = TSocket.TServerSocket(host='127.0.0.1', port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    #tfactory = TTransport.TFramedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()
    #server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print('Starting the server...')
    server.serve()

