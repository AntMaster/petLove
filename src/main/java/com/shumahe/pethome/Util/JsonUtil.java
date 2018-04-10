package com.shumahe.pethome.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by zhangyu
 * 2018-02-23 21:50
 */
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
