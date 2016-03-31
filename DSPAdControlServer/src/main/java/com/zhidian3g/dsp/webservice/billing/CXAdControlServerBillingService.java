package com.zhidian3g.dsp.webservice.billing;

import javax.jws.WebService;

/**
 * 
 * 广告接收系统计费服务
 * @author Administrator
 *
 */
@WebService
public interface CXAdControlServerBillingService {
	
	
	/**
	 * 初始化广告费用
	 * @param id 广告ID
	 * @param dayBudget 广告日预算量
	 */
	String addAdOrRefreshBudget(String md5, Long adId, Long dayBudget);
	
}
