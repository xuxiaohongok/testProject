package com.zhidian.remote.service.impl;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.zhidian.ad.service.AdSearchService;
import com.zhidian.remote.service.Condition;
import com.zhidian.remote.service.RemoteService;


/**
 * @描述: 用户Dubbo服务接口 .
 * @版本号: V1.0 .
 */
@Service("remoteService")
public class RemoteServiceImpl implements RemoteService{
	
	@Resource
	private AdSearchService adSearchService;
	
	@Override
	public String getAdMessage(String adMessage) {
		Logger logger = LoggerFactory.getLogger("apiRequestLog");
		logger.info("======ok===adMessage:" + adMessage);
		return adSearchService.adSearchHander(adMessage);
	}
	
	@Override
	public String getAdMessageV1(String adMessage) {
		return adSearchService.adSearchHanderV1(adMessage);
	}

	@Override
	public String addAdTempMessage(Integer adMessage, Condition condition) {
		if(adMessage == -1) {
//			TestAdTempControl.testDelAll();
		} else if(adMessage == 2) {
//			TestAdTempControl.testAddAd(condition);
		}
		return "ok";
	}

}
