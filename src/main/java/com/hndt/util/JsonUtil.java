package com.hndt.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hystar
 * @date 2017/10/14
 */
public class JsonUtil {

    /**
     * 将obj对象转换成json类型的字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(obj);
    }

    /**
     * 将json对象转换成obj类型
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Type type) {
        T obj = new GsonBuilder().create().fromJson(json, type);
        return obj;
    }

    /**
     * 将传入的多个参数转换成map类型再转换成json类型
     *
     * @param params
     * @return
     */
    public static String asJsonMap(Object... params) {
        Map map = asHashMap(params);
        return toJson(map);
    }

    private static HashMap asHashMap(Object... params) {
        if (params.length == 0 || params.length % 2 != 0) {
            throw new RuntimeException();
        }
        HashMap map = new HashMap(params.length / 2);
        for (int i = 0; i < params.length; i = i + 2) {
            map.put(params[i], params[i + 1]);
        }
        return map;
    }

    public static <T> List<T> toList(String json, Class<T> elementClass) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        JsonElement root = new JsonParser().parse(json);
        for (JsonElement e : root.getAsJsonArray()) {
            T entity = gson.fromJson(e, elementClass);
            list.add(entity);
        }
        return list;
    }

}
