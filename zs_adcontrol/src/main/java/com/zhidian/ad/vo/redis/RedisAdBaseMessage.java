package com.zhidian.ad.vo.redis;

public class RedisAdBaseMessage {
	//广告id
	private Long adId;
	//渠道
	private int adxType;
	//广告行业类别
	private Integer adCategory;
	//广告报名
	private String adName;
	//点击类型
	private Integer clickType;
	//广告包名
	private String adPackageName;
	//广告价格
	private long adPrice;
	
	//广告的扩展信息
	private String extendMessage;
	
	public Long getAdId() {
		return adId;
	}
	public void setAdId(Long adId) {
		this.adId = adId;
	}
	public int getAdxType() {
		return adxType;
	}
	public void setAdxType(int adxType) {
		this.adxType = adxType;
	}
	public Integer getAdCategory() {
		return adCategory;
	}
	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	public Integer getClickType() {
		return clickType;
	}
	public void setClickType(Integer clickType) {
		this.clickType = clickType;
	}
	public String getAdPackageName() {
		return adPackageName;
	}
	public void setAdPackageName(String adPackageName) {
		this.adPackageName = adPackageName;
	}
	public long getAdPrice() {
		return adPrice;
	}
	public void setAdPrice(long adPrice) {
		this.adPrice = adPrice;
	}
	public String getExtendMessage() {
		return extendMessage;
	}
	public void setExtendMessage(String extendMessage) {
		this.extendMessage = extendMessage;
	}
}
