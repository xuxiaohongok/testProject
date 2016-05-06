package com.zhidian3g.dsp.solr.document;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 广告索引文档
 * @author Administrator
 *
 */
public class DspAdDocument {
	
	@Field
	private String id;
	
	@Field
	private String adxType;
	
	@Field
	private Long adId;
	
	/**广告投放平台***********/
	@Field
	private String adPlatform;
	
	@Field
	private String adHW;
	
	@Field
	private String showType;
	
	@Field
	private String adType;
	
	@Field
	private String adCategory;
	
	@Field
	private String timeZones;
	
	@Field
	private Integer length;
	
	@Field
	private String areas;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAdPlatform() {
		return adPlatform;
	}

	public void setAdPlatform(String adPlatform) {
		this.adPlatform = adPlatform;
	}

	public String getAdHW() {
		return adHW;
	}

	public void setAdHW(String adHW) {
		this.adHW = adHW;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
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

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}
	
}
