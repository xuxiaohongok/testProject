package com.zhidian3g.dsp.vo.solr;

import java.util.Map;

import com.zhidian3g.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdCreateMaterialMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdImage;
import com.zhidian3g.dsp.vo.ad.RedisAdLandingPageMessage;

public class SearchAd {
	//广告基本信息
	private RedisAdBaseMessage redisAdBaseMessage;
	
	private RedisAdLandingPageMessage adLandingPageMessage;
	
	private RedisAdCreateMaterialMessage redisAdCreateMaterialMessage;
	
	private Map<String,RedisAdImage> redisAdImageMap;

	public RedisAdBaseMessage getRedisAdBaseMessage() {
		return redisAdBaseMessage;
	}

	public void setRedisAdBaseMessage(RedisAdBaseMessage redisAdBaseMessage) {
		this.redisAdBaseMessage = redisAdBaseMessage;
	}

	public RedisAdLandingPageMessage getAdLandingPageMessage() {
		return adLandingPageMessage;
	}

	public void setAdLandingPageMessage(
			RedisAdLandingPageMessage adLandingPageMessage) {
		this.adLandingPageMessage = adLandingPageMessage;
	}

	public RedisAdCreateMaterialMessage getRedisAdCreateMaterialMessage() {
		return redisAdCreateMaterialMessage;
	}

	public void setRedisAdCreateMaterialMessage(
			RedisAdCreateMaterialMessage redisAdCreateMaterialMessage) {
		this.redisAdCreateMaterialMessage = redisAdCreateMaterialMessage;
	}

	public Map<String, RedisAdImage> getRedisAdImageMap() {
		return redisAdImageMap;
	}

	public void setRedisAdImageMap(Map<String, RedisAdImage> redisAdImageMap) {
		this.redisAdImageMap = redisAdImageMap;
	}
}
