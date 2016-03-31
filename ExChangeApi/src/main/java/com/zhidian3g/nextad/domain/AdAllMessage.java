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
public class AdAllMessage implements Serializable{

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
	
	private String createId;
	
	private String landingPageId;
	//落地页
	private String landingPage;
	//素材
	private String adm;
	
//	//广告媒体url
//	private String url;
	
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

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getLandingPageId() {
		return landingPageId;
	}

	public void setLandingPageId(String landingPageId) {
		this.landingPageId = landingPageId;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public String getAdm() {
		return adm;
	}

	public void setAdm(String adm) {
		this.adm = adm;
	}

//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
	
}
