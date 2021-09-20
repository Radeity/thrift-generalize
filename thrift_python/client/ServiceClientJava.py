import json

from thrift import Thrift
from thrift.protocol import TBinaryProtocol
from thrift.transport import TSocket, TTransport

from server.genPy import DIYFrameworkService
from server.genPy.ttypes import Request, ServiceException


def main():
    # Make socket
    transport = TSocket.TSocket('127.0.0.1', 9898)

    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TBufferedTransport(transport)
    #transport = TTransport.TFramedTransport(transport)

    # Wrap in a protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a client to use the protocol encoder
    client = DIYFrameworkService.Client(protocol)
    transport.open()

    request = Request(None, "sayHello")
    paramDict = {
        'param': 'thrift'
    }
    paramJson = json.dumps(paramDict)
    paramBytes = bytes(paramJson, 'utf-8')
    request.paramJSON = paramBytes

    try:
        response = client.send(request)
        print(response.responseJSON)
    except ServiceException as e:
        print(e.exceptionMess)


if __name__ == '__main__':
    try:
        main()
    except Thrift.TException as tx:
        print('%s' % tx.message)