package com.fdu.server.Service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServicesB implements Services {
    public Map<String,String> saySB(JSONObject param) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("res", "SB " + param.getString("param"));
        return map;
    }
}
