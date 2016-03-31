package com.zhidian3g.dsp.ad;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

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
	
	private String title;
	
	//广告类别
	private Integer adCategory; 
	
	private long adAccountCost;
	
	private long adDayBudget;
	
	private long adPrice;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}

	public long getAdAccountCost() {
		return adAccountCost;
	}

	public void setAdAccountCost(long adAccountCost) {
		this.adAccountCost = adAccountCost;
	}

	public long getAdDayBudget() {
		return adDayBudget;
	}

	public void setAdDayBudget(long adDayBudget) {
		this.adDayBudget = adDayBudget;
	}

	public long getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(long adPrice) {
		this.adPrice = adPrice;
	}
	
}
