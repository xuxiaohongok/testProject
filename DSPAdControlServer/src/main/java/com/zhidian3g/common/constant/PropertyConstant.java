package com.zhidian3g.common.constant;

import com.zhidian3g.dsp.util.PropertiesUtil;

public class PropertyConstant {
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	public static String DSP_EVERYDAY_REFRESH_TIMER = propertiesUtil.getValue("dsp_everyday_refresh_timer");
	public static String DSP_ADCONTROL_COUNT_TIMER = propertiesUtil.getValue("dsp_adcontrol_count_timer");
}
