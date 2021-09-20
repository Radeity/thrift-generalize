namespace py generalize
namespace java generalize

# 请求体
struct Request {
    # 参数 -- Byte[]
    1:required binary paramJSON;
    # 服务名
    2:required string serviceName
}

# 返回体
struct Response {
    # 状态码
    1:required  RESCODE responeCode;
    # 返回的处理结果，同样使用JSON格式进行描述
    2:required  binary responseJSON;
}

# 服务调用异常
exception ServiceException {
    # 异常代码
    1:required EXCCODE exceptionCode;
    # 异常描述信息
    2:required string exceptionMess;
}

# 响应代码
enum RESCODE {
    _200=200;
    _500=500;
    _400=400;
}

# 异常种类
enum EXCCODE {
    PARAMNOTFOUND = 2001;
    SERVICENOTFOUND = 2002;
}

# 泛化后Apache Thrift接口
service DIYFrameworkService {
    Response send(1:required Request request) throws (1: ServiceException e);
}

