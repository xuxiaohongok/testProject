 package com.zhidian3g.dsp.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * Java对象和JSON字符串相互转化工具类
 * @date 2013-08-10
 */
public class JsonUtil {
	 

    /**
     * 对象转换成json字符串
     * @param obj 
     * @return 
     */
    public static String toJson(Object obj) {
    	Gson gson = new Gson(); 
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * @param str  
     * @param type  {@code new TypeToken<List<Map<String,String>>>(){}.getType()}
     * @return 
     */
    public static <T> T fromJson(String str, Type type) {
    	Gson gson = new Gson(); 
        return (T)gson.fromJson(str, type);
    }


    /**
     * json字符串转成对象
     * @param str  需要转换的字符串
     * @param type 需要转换的对象类型
     * @return 对象
     */
    public static <T> T fromJson(String str, Class<T> type) {
    	Gson gson = new Gson(); 
        return (T)gson.fromJson(str, type);
    }
    
    public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("12", 12);
		String ok = JsonUtil.toJson(map);
		System.out.println(JsonUtil.fromJson(ok, new TypeToken<Map<Integer,Integer>>(){}.getType()));
	}

}

