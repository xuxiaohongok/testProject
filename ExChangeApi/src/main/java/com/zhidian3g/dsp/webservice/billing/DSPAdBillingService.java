 package com.zhidian3g.dsp.webservice.billing;

import javax.jws.WebService;

/**
 * 
 * 广告接收系统计费服务
 * @author Administrator
 *
 */
@WebService
public interface DSPAdBillingService {

	/**
	 * 0.广告无效 1.成功扣除  3.广告费用用关
	 * 扣除广告服务次数
	 * @param id
	 * @param fee
	 */
	Integer rebate(String md5, Long adId);
	
}
