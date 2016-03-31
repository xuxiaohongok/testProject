package com.zhidian3g.common.constant;

public class RedisConstant {
	/******广告id的排序集合*********/
	public static String AD_IDS_KEY = "ad_ids_key";
	
	public static String AD_DEL_KEY = "ad_del_key";
	/******广告的基本信息key value 前缀*********/
	public static String AD_BASE = "ad_base_";
	/******广告展示类型信息key value 前缀 + adId + "_" + adSlotType*********/
	public static String AD_ADSLOT = "ad_adslot_";
	
	/**广告账户信息****************/
	public static String AD_ACCOUNT = "ad_account_";
	/**广告账户余额********/
	public static String AD_ACCOUNT_COST = "ad_account_cost";
	/**广告日预算key***********************/
	public static String AD_DAYBUDGET = "ad_daybudget";
	
	/*** 广告日预算key**/
	public static final String AD_DAYBUDGET_DATE = "ad_daybudget_date_";
	
	/*** 所有广告每天总的预算key保存**/
	public static final String INIT_AD_DAYBUDGET = "init_ad_daybudget";
	
	/**删除队列**********************/
	public static final String DEL_ADID = "del_adid";
	public static final String BACKUP_DEL_ADID = "backup_del_adid";
	
	/**hash*****/
	public static final String AD_ID_CONTROL_COUNT = "ad_id_control_count";
	
	/*******/
	public static final String AD_STOP_IDS = "ad_stop_ids";
}
