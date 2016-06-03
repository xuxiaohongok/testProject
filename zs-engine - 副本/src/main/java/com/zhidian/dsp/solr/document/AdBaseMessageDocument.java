package com.zhidian.dsp.solr.document;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 广告基本信息索引文档
 * @author Administrator
 *
 */
public class AdBaseMessageDocument {
	
	@Field
	private String id;
	
	@Field
	private String documentType;
	
	@Field
	private String adxType;
	
	@Field
	private Long adId;
	
	@Field
	private String terminalType;
	
	/**移动广告投放手机系统平台***********/
	@Field
	private String osPlatform;
	
	@Field
	private String networkType;
	
	@Field
	private String adCategory;
	
	@Field
	private String timeZones;
	
	@Field
	private String areas;
	
	@Field
	private Long adPrice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getAdxType() {
		return adxType;
	}

	public void setAdxType(String adxType) {
		this.adxType = adxType;
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getOsPlatform() {
		return osPlatform;
	}

	public void setOsPlatform(String osPlatform) {
		this.osPlatform = osPlatform;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(String adCategory) {
		this.adCategory = adCategory;
	}

	public String getTimeZones() {
		return timeZones;
	}

	public void setTimeZones(String timeZones) {
		this.timeZones = timeZones;
	}

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}

	public Long getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Long adPrice) {
		this.adPrice = adPrice;
	}
	

}
