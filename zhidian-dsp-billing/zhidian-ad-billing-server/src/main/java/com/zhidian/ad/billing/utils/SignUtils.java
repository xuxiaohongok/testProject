package com.zhidian.ad.billing.utils;

import ch.qos.logback.core.util.TimeUtil;
import com.zhidian.ad.billing.constant.Constant;
import com.zhidian.ad.billing.constant.LoggerEnum;
import com.zhidian.ad.billing.constant.ReturnCode;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenwanli on 2016/3/16.
 */
public class SignUtils {

    public static int checkSign(String sign,long dateTime,Object... values){
        if(System.currentTimeMillis()-dateTime> Constant.SIGN_OVERTIME_SIZE){
            return ReturnCode.SIGN_OVERTIME_ERROR_CODE;
        }

        String valueStr="";
        if(values.length>0){
            for (Object value:values) {
                valueStr+=value+",";
            }
        }
        valueStr+=dateTime;
        if(sign.equals(MD5Util.encode(valueStr))){
            return ReturnCode.SUCCESS_CODE;
        }else{
            return ReturnCode.SIGN_ERROR_CODE;
        }

    }
}
