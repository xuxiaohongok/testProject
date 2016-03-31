package com.zhidian3g.common.constant;

import com.zhidian3g.common.utils.PropertiesUtil;

public class PropertyConstant {
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	
	/**广告的回调地址**********/
	public static final String AD_WIN_POST_BACK_URL = propertiesUtil.getValue("ad_win_post_back_url");
	public static final String AD_SHOW_POST_BACK_URL = propertiesUtil.getValue("ad_show_post_back_url");
	public static final String AD_CLICK_POST_BACK_URL = propertiesUtil.getValue("ad_click_post_back_url");
	
	public static final String AD_SHOW_POST_BACK_URL_EXTEND  = propertiesUtil.getValue("ad_show_post_back_url1");
	public static final String AD_CLICK_POST_BACK_URL_EXTEND  = propertiesUtil.getValue("ad_click_post_back_url1");
	
	public static final String SERVER_LOGGER_INTERVAL_TIME = propertiesUtil.getValue("server_logger_interval_time");
	public static final String EVERYHOUR_LOG_NAMES = propertiesUtil.getValue("everyhour_log_names");
}
