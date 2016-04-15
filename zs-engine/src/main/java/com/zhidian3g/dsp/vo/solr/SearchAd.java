package com.zhidian3g.dsp.vo.solr;

public class SearchAd {
	
	//创意广告
	private RedisNativeAd redisNativeAd;
	
	//图片广告
	private RedisImageAd redisImageAd;

	public RedisNativeAd getRedisNativeAd() {
		return redisNativeAd;
	}

	public void setRedisNativeAd(RedisNativeAd redisNativeAd) {
		this.redisNativeAd = redisNativeAd;
	}

	public RedisImageAd getRedisImageAd() {
		return redisImageAd;
	}

	public void setRedisImageAd(RedisImageAd redisImageAd) {
		this.redisImageAd = redisImageAd;
	}

}
