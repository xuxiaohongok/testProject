 package com.zhidian3g.common.util;

import com.alibaba.fastjson.JSON;

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

}

