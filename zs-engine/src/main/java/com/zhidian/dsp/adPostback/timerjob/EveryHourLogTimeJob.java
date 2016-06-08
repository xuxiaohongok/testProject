package com.zhidian.dsp.adPostback.timerjob;

import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.dsp.constant.PropertieConstant;

/**
 * 
 * 日志切割定时任务
 * @author Administrator
 *
 */
public class EveryHourLogTimeJob{
	
	public void execute() {
		String[] logNames = PropertieConstant.EVERYHOUR_LOG_NAMES.split(",");
		for(String loggerName : logNames) {
			CommonLoggerUtil.addLog(loggerName.trim(), "");
		}
	}
	
}
