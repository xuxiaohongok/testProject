package com.zhidian.common.contans;

/**
 * 
 * 广告操作状态码信息
 * @author Administrator
 *
 */
public class AdOpertionStatus {
	
	public final static Integer AD_OPERTIONS_IS_FAIL = 0;
	
	public final static Integer AD_OPERTIONS_IS_OK = 1;
	
	//不符合广告投放条件
	public final static Integer AD_CONDITION_INVALID = 2;
	//日期为空
	public final static Integer AD_DATE_IS_NULL = 3;
	//时间没到
	public final static Integer AD_DATE_IS_PREVIOUS = 4;
	//时间已过期
	public final static Integer AD_DATE_IS_OUT = 5;
	//广告投放策略无效
	public final static Integer AD_STRATEGY_INVALID = 6;
	//广告没有审核通过的素材包
	public final static Integer AD_HAS_NO_MATERIAL = 7;
	//广告没有对应账户
	public final static Integer AD_HAS_NO_ACCOUTN = 8;
	//广告账户欠费
	public final static Integer AD_ACCOUTN_IS_ARREARAGE = 9;
	//广告总预算用关
	public final static Integer AD_TOTAL_BUDGET_IS_OVER = 10;
	//广告日预算用关
	public final static Integer AD_TOTAY_BUDGET_IS_OVER = 11;
	
	//未知状态
	public final static Integer AD_OPERTION_UNKNOW = 98;
	//服务器异常
	public final static Integer SERVER_ERR = 99;
	
//	public final static Integer UPDATE_AD_PUT_STATUS = ;
	
	public static String getStatusMessage(Integer statusCode) {
		String statusMessage = null;
		switch (statusCode) {
		
		case 0:
			statusMessage = "fail";
			break;
		
		case 1:
			statusMessage = "ok";
			break;
		
		case 2:
			statusMessage = "广告审核不通过";
			break;
			
		case 3:
			statusMessage = "广告投放日期为空";
			break;
			
		case 4:
			statusMessage = "广告投放日期没到";
			break;
			
		case 5:
			statusMessage = "广告已过期";
			break;
			
		case 6:
			statusMessage = "广告投放受限于投放策略";
			break;
			
		case 7:
			statusMessage = "广告没有审核通过的素材包";
			break;
			
		case 8:
			statusMessage = "广告没有对应账户";
			break;

		case 9:
			statusMessage = "广告账户欠费";
			break;
			
		case 10:
			statusMessage = "广告总预算用关";
			break;
			
		case 11:
			statusMessage = "广告日预算用关";
			break;
			
		case 98:
			statusMessage = "未知状态";
			break;
			
		case 99:
			statusMessage = "服务器异常";
			break;
			
		default:
			statusMessage = "未知状态码=" + statusCode;
			break ;
		}
		
		return statusMessage;
	}
	
}
