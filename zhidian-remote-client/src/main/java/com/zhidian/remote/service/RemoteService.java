package com.zhidian.remote.service;




/**com.zhidian.remote.service.RemoteService.
 * @描述: 用户Dubbo服务接口 .
 * @版本号: V1.0 .
 */
public interface RemoteService {
	
	/**
	 * 
	 * 单个广告位引擎接口 v1.0
	 * @param adMessage
	 * @return
	 */
	public String getAdMessage(String adMessage);
	
	/**
	 * 
	 * 多个广告位的引擎接口 v1.1
	 * @param adMessage
	 * @return
	 */
	public String getAdMessageV1(String adMessage);
	
	public String addAdTempMessage(Integer adMessage, Condition condition);
	
}
