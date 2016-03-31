package com.zhidian3g.nextad.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * InnoDB free: 160768 kB 实体 <br/>
 * 
 * @author xxh
 * @version 1.0 2014-08-18 14:51:10
 * @since JDK 1.5
 */
public class AdMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**  */
	private String adId;
	
	private String name;
	
	private Integer clickType;
	
	//广告类别
	private Integer adCategory; 
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getClickType() {
		return clickType;
	}

	public void setClickType(Integer clickType) {
		this.clickType = clickType;
	}


	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}
}
