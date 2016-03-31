package com.zhidian.dsp.constant;

import com.zhidian3g.common.util.PropertiesUtil;

public class PropertieConstant {
	
	/**配置文件常量***************************************/
	private static final PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	/**
	 * 加密解密内部使用的key
	 */
	public static final String NEXTAD_INNER_KEY= propertiesUtil.getValue("server.encrypt.key");
	
	/**
	 * 图片服务器 - 默认的图片 
	 */
	public static final String IMAGES_SERVER_DEFAULT = propertiesUtil.getValue("server.image_default");
	
	/**
	 * 服务器URL
	 */
	public static final String SERVER_URL = propertiesUtil.getValue("server.server_url");
	
	/**
	 * 图片服务器
	 */
	public static final String IMAGES_SERVER = propertiesUtil.getValue("server.image");
		
	/**
	 * 点击下载安装 
	 */
	public static final String CLICK_DOWNLOAD_INSTALL_SERVER = propertiesUtil.getValue("server.download_install");
	
	/**ip地址****/
	public static final String IP_ADRESS = propertiesUtil.getValue("server.ip.adress");
	
	/**广告的回调地址**********/
	/**竞价成功的回调地址*******/
	public static final String AD_WIN_POST_BACK_URL = propertiesUtil.getValue("ad_win_post_back_url");
	/**曝光回调地址*************/
	public static final String AD_SHOW_POST_BACK_URL = propertiesUtil.getValue("ad_show_post_back_url");
	/**点击回调地址**************************/
	public static final String AD_CLICK_POST_BACK_URL = propertiesUtil.getValue("ad_click_post_back_url");
	
}
