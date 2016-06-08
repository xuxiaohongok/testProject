package com.zhidian3g.dsp.adPostback.constant;

import com.zhidian.common.util.PropertiesUtil;

public class PropertyConstant {
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	
	/**定时切割日志文件**********/
	public static final String EVERYHOUR_LOG_NAMES = propertiesUtil.getValue("everyhour_log_names");
}
