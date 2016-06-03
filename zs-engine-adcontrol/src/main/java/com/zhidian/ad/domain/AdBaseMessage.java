package com.zhidian.ad.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 广告基本信息
 * @author Administrator
 *
 */
public class AdBaseMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Long id;
    
    private String name;
    
    private Long accountId;
    
    private Long adxType;
    
    private Integer terminalType;
    
    private String adPackageName;
    
    private int biddingType;
    
    private Long adPrice;
    
    private int adCategory;
    
    private Long totalBudget;
    
    private Long dayBudget;
    
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

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAdxType() {
		return adxType;
	}

	public void setAdxType(Long adxType) {
		this.adxType = adxType;
	}

	public Integer getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Integer terminalType) {
		this.terminalType = terminalType;
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

	public Long getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Long adPrice) {
		this.adPrice = adPrice;
	}

	public int getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(int adCategory) {
		this.adCategory = adCategory;
	}

	public Long getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(Long totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Long getDayBudget() {
		return dayBudget;
	}

	public void setDayBudget(Long dayBudget) {
		this.dayBudget = dayBudget;
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