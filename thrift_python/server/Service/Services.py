class Services:
    def predict(this, paramDict):
        resultDict = {}
        resultDict['result'] = None
        try:
            resultDict['result'] = paramDict['param']>0
            resultDict['RESCODE'] = 200
        except TypeError as e:
            resultDict['RESCODE'] = 500

        print(resultDict)
        return resultDict