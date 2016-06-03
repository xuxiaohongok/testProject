package com.zhidian.dsp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;

/**
 * 
 * 控制平台日志
 * @author Administrator
 *
 */
public class AdControlLoggerUtil extends CommonLoggerUtil {
	private static Logger timeLogger = LoggerFactory.getLogger("timeLog");
	/**
	 * 
	 * 添加时间日志
	 * @param message
	 */
	public static void addTimeLog(String message) {
		timeLogger.info(DateUtil.getDateTime() + ":::" + message);
	}

}
