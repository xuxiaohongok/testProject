package com.zhidian.ad.billing.constant;

/**
 * Created by chenwanli on 2016/3/17.
 */
public enum LoggerEnum {

    EXCEPTION_LOG("exceptionLog"), API_REQUEST_LOG("apiRequestLog"), ERR_REQUEST_LOG("errRequestLog");


    private String loggerName;

    private LoggerEnum(String loggerName) {
        this.loggerName = loggerName;
    }


    @Override
    public String toString() {
        return loggerName;
    }
}
