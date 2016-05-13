package com.zhidian.ad.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Ad implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Long id;
    
    private String name;
    
    private int adxId;
    
    private String packageName;
    
    private int biddingType;
    
    private int adPrice;
    
    private int adCategory;
    
    private long totalBudget;
    
    private long dayBudget;
    
    private int channelId;
    
    private Date putStartTime;
    
    private Date putEndTime;
    
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

	public int getAdxId() {
		return adxId;
	}

	public void setAdxId(int adxId) {
		this.adxId = adxId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getBiddingType() {
		return biddingType;
	}

	public void setBiddingType(int biddingType) {
		this.biddingType = biddingType;
	}

	public int getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(int adPrice) {
		this.adPrice = adPrice;
	}

	public int getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(int adCategory) {
		this.adCategory = adCategory;
	}

	public long getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(long totalBudget) {
		this.totalBudget = totalBudget;
	}

	public long getDayBudget() {
		return dayBudget;
	}

	public void setDayBudget(long dayBudget) {
		this.dayBudget = dayBudget;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public Date getPutStartTime() {
		return putStartTime;
	}

	public void setPutStartTime(Date putStartTime) {
		this.putStartTime = putStartTime;
	}

	public Date getPutEndTime() {
		return putEndTime;
	}

	public void setPutEndTime(Date putEndTime) {
		this.putEndTime = putEndTime;
	}

}