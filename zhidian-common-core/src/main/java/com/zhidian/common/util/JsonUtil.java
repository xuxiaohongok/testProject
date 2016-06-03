 package com.zhidian.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * Java对象和JSON字符串相互转化工具类
 * 
 */
public class JsonUtil {
	
    /**
     * 对象转换成json字符串
     * @param obj 
     * @return 
     */
    public static String toJson(Object obj) {
    	return JSON.toJSONString(obj);
    }

    /**
     * json字符串转成对象
     * @param str  需要转换的字符串
     * @param type 需要转换的对象类型
     * @return 对象
     */
    public static <T> T fromJson(String jsonStr, Class<T> type) {
    	return JSON.parseObject(jsonStr, type);
    }
    
    /**
     * 复杂类型转换
     * @param jsonStr
     * @param type
     * @return
     * new TypeReference<Foo<List<Integer>>>(){}
     */
    public static <T> T fromJsonType(String jsonStr, TypeReference<T> type) {
    	return JSON.parseObject(jsonStr, type);
    }

}

