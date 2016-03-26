package com.zhidian.dsp.constant;

import com.zhidian3g.common.util.PropertiesUtil;

public class DspConstant {
	
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
	
	

	/**dsp常量*********************************************/
	/** 广告类型 */
	public static final Integer TYPE_AD = 1;

	/** 应用类型 */
	public static final Integer TYPE_APP = 2;
	
	/**媒体类别前缀****/
	public static final String ADX_TYPE = "adxType";
	/**广告穿越类型前缀***************/
	public static final String AD_TYPE = "adType";
	/**广告展示类型前缀*******************/
	public static final String AD_SHOW_TYPE = "showType";
	/**广告行业类别前缀**************************/
	public static final String AD_CATEGORY = "adCategory";
	
	public static final Integer DEFAULT_ROW = 10000;

	//原生广告展示类型
	public static final Integer NATIVE_AD_TYPE = 5;
	
	public static final Integer IMAGE_TYPE = null;
	


}
