package com.zhidian.ad.billing.utils;

import com.zhidian.ad.billing.constant.ReturnCode;

import java.util.List;

/**
 * Created by chenwanli on 2016/3/17.
 */
public class RedisCheckUtils {

    public static String getResultErr(List<Object> results){
        if(results==null){
            return null;
        }

        String err="";
        for(Object result:results){
            if(result==null||result.toString().startsWith("-")){
                err+=result+"&&";
            }
        }
        if(err.length()>0){
            return err.substring(0,err.length()-2);
        }
        return null;

    }
}
