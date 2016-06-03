package com.zhidian.ad.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdPutStrategyMessage implements Serializable{
	
	/**
	 */
	private static final long serialVersionUID = 1L;
	
	private Long adId;
	private Long parentId;
	private String targetItemIds;
	private Integer isInclude;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}
	
	public Long getAdId() {
		return adId;
	}
	public void setAdId(Long adId) {
		this.adId = adId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getTargetItemIds() {
		return targetItemIds;
	}
	public void setTargetItemIds(String targetItemIds) {
		this.targetItemIds = targetItemIds;
	}
	public Integer getIsInclude() {
		return isInclude;
	}
	public void setIsInclude(Integer isInclude) {
		this.isInclude = isInclude;
	}
	
}
