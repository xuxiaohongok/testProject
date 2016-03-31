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
public class AdSlotMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer adSlotType;
	
	private String createId;
	
	private String landingPageId;
	//落地页
	private String landingPage;
	//素材
	private String adm;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getAdSlotType() {
		return adSlotType;
	}

	public void setAdSlotType(Integer adSlotType) {
		this.adSlotType = adSlotType;
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
}
