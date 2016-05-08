package com.zhidian3g.dsp.vo.solr;

import com.zhidian3g.dsp.vo.ad.AdLandingPageMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdCreatePackageMessage;
import com.zhidian3g.dsp.vo.adcontrol.AdMaterialMessage;

public class SearchAd {
	//广告基本信息
	private RedisAdBaseMessage redisAdBaseMessage;
	
	private AdLandingPageMessage adLandingPageMessage;
	
	private RedisAdCreatePackageMessage redisAdCreatePackageMessage;

	public RedisAdBaseMessage getRedisAdBaseMessage() {
		return redisAdBaseMessage;
	}

	public void setRedisAdBaseMessage(RedisAdBaseMessage redisAdBaseMessage) {
		this.redisAdBaseMessage = redisAdBaseMessage;
	}

	public AdLandingPageMessage getAdLandingPageMessage() {
		return adLandingPageMessage;
	}

	public void setAdLandingPageMessage(AdLandingPageMessage adLandingPageMessage) {
		this.adLandingPageMessage = adLandingPageMessage;
	}

	public RedisAdCreatePackageMessage getRedisAdCreatePackageMessage() {
		return redisAdCreatePackageMessage;
	}

	public void setRedisAdCreatePackageMessage(
			RedisAdCreatePackageMessage redisAdCreatePackageMessage) {
		this.redisAdCreatePackageMessage = redisAdCreatePackageMessage;
	}

	
}
