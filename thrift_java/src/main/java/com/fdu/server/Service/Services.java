package com.fdu.server.Service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Services {
    public Map<String,String> sayHello(JSONObject param) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("res", "hello " + param.getString("param"));
        map.put("ResponeCode", "200");
        return map;
    }

    public Map<String,String> sayGoodBye(A a) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("res", "hello " + a.paramA);
        return map;
    }

    public Map<String,String> saySB(JSONObject param) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("res", "SB " + param.getString("param"));
        return map;
    }
}

class A {
    public String paramA;
    public String paramB;
    A(String paramA, String paramB) {
        this.paramA = paramA;
        this.paramB = paramB;
    }
}