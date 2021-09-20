from server.Service.Services import Services
from server.genPy.ttypes import EXCCODE, ServiceException, Response, RESCODE
import json

class ServiceHandlerImpl:
    def send(self, request):
        serviceName = request.serviceName
        print(type(request.paramJSON))
        paramStr = str(request.paramJSON, 'UTF-8')
        paramJSON = json.loads(paramStr)

        services = Services()

        if (hasattr(services, serviceName)):
            targetMethod = getattr(services, serviceName)
       # else:
       #     raise ServiceException(EXCCODE.SERVICENOTFOUND, "Service Not Found!")

        responseDict = targetMethod(paramJSON)
        responseJSON = json.dumps(responseDict)

        response = Response(RESCODE._200, bytes(responseJSON, 'utf-8'))

        return response
