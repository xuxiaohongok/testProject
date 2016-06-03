package com.zhidian.dsp.vo.ad;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RedisAdBaseMessage {
	
	//广告id
	private Long id;
	
	private String name;
	
	private Long accountId;
	
	private Long adPrice;
	
	//渠道
	private Long adxType;
	//广告行业类别
	private Integer adCategory;
	//广告包名
	private String adPackageName;
	
	private int biddingType;
	
	//广告的扩展信息
	private String extendMessage;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Long adPrice) {
		this.adPrice = adPrice;
	}

	public Long getAdxType() {
		return adxType;
	}

	public void setAdxType(Long adxType) {
		this.adxType = adxType;
	}

	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}

	public String getAdPackageName() {
		return adPackageName;
	}

	public void setAdPackageName(String adPackageName) {
		this.adPackageName = adPackageName;
	}
	
	public int getBiddingType() {
		return biddingType;
	}

	public void setBiddingType(int biddingType) {
		this.biddingType = biddingType;
	}

	public String getExtendMessage() {
		return extendMessage;
	}

	public void setExtendMessage(String extendMessage) {
		this.extendMessage = extendMessage;
	}
}
	
