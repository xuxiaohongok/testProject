package com.zhidian3g.dsp.adPostback.timerjob;

import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian3g.dsp.adPostback.constant.PropertyConstant;

/**
 * 
 * 日志切割定时任务
 * @author Administrator
 *
 */
public class EveryHourLogTimeJob{
	
	public void execute() {
		System.out.println("==============ok========");
		String[] logNames = PropertyConstant.EVERYHOUR_LOG_NAMES.split(",");
		for(String loggerName : logNames) {
			CommonLoggerUtil.addLog(loggerName, "");
		}
	}
	
	
}
