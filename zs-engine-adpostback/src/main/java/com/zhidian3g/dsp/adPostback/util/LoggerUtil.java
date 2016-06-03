package com.zhidian3g.dsp.adPostback.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian.common.util.JsonUtil;
import com.zhidian3g.dsp.adPostback.web.vo.AdOperationLogMessage;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by cjl on 2016/3/7.
 */
public class LoggerUtil {

    private static Logger winLog = LoggerFactory.getLogger("winLog");
    
    /**
     * 广告竞价成功
     * @param logMessage
     */
    public static void addWinNoticeLog(AdOperationLogMessage adOperationLogMessage) {
        winLog.info(JsonUtil.toJson(adOperationLogMessage));
    }

    private static Logger showLog = LoggerFactory.getLogger("showLog");
    /**
     * 广告曝光
     * @param logMessage
     */
    public static void addAdShowLog(AdOperationLogMessage adOperationLogMessage) {
    	showLog.info(JsonUtil.toJson(adOperationLogMessage));
    }

    /**
     * 广告点击
     * @param logMessage
     */
    private static Logger clickLog = LoggerFactory.getLogger("clickLog");
    public static void addAdClickLog(AdOperationLogMessage adOperationLogMessage) {
    	clickLog.info(JsonUtil.toJson(adOperationLogMessage));
    }
}
