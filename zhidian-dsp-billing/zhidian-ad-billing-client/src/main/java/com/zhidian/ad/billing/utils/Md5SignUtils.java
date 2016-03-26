package com.zhidian.ad.billing.utils;

import com.zhidian.ad.billing.utils.MD5Util;

/**
 * Created by chenwanli on 2016/3/16.
 */
public class Md5SignUtils {

    public static String sign(long dateTime,Object... values){

        String valueStr="";
        if(values.length>0){

            for (Object value:values) {
                valueStr+=value+",";
            }

        }
        valueStr+=dateTime;
        return MD5Util.encode(valueStr);

    }
}
