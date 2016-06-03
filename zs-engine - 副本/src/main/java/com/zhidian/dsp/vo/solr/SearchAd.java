package com.zhidian.dsp.vo.solr;

import java.util.Map;

import com.zhidian.dsp.vo.ad.AdImageMessage;
import com.zhidian.dsp.vo.ad.AdLandpageMessage;
import com.zhidian.dsp.vo.ad.AdMaterialMessage;
import com.zhidian.dsp.vo.ad.RedisAdBaseMessage;

public class SearchAd {
	//广告基本信息
	private RedisAdBaseMessage redisAdBaseMessage;
	
	private AdLandpageMessage adLandingPageMessage;
	
	private AdMaterialMessage adMaterialMessage;
	
	private Map<String, AdImageMessage> redisAdImageMap;

	public RedisAdBaseMessage getRedisAdBaseMessage() {
		return redisAdBaseMessage;
	}

	public void setRedisAdBaseMessage(RedisAdBaseMessage redisAdBaseMessage) {
		this.redisAdBaseMessage = redisAdBaseMessage;
	}

	public AdLandpageMessage getAdLandingPageMessage() {
		return adLandingPageMessage;
	}

	public void setAdLandingPageMessage(AdLandpageMessage adLandingPageMessage) {
		this.adLandingPageMessage = adLandingPageMessage;
	}

	public AdMaterialMessage getAdMaterialMessage() {
		return adMaterialMessage;
	}

	public void setAdMaterialMessage(AdMaterialMessage adMaterialMessage) {
		this.adMaterialMessage = adMaterialMessage;
	}

	public Map<String, AdImageMessage> getRedisAdImageMap() {
		return redisAdImageMap;
	}

	public void setRedisAdImageMap(Map<String, AdImageMessage> redisAdImageMap) {
		this.redisAdImageMap = redisAdImageMap;
	}

}
