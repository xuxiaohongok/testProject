package com.zhidian3g.dsp.solr.document;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 广告索引文档
 * @author Administrator
 *
 */
public class AdMaterialDocument {
	
	@Field
	private String id;
	
	@Field
	private long adId;
	
	@Field
	private Integer createId;
	
	@Field
	private Integer meterialId;
	
	@Field
	private String meterialType;
	
	@Field
	private String imageHWs;
	
	/**广告投放平台***********/
	@Field
	private Integer tLen;
	
	@Field
	private Integer dLen;
	
	@Field
	private List<String> text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public Integer getMeterialId() {
		return meterialId;
	}

	public void setMeterialId(Integer meterialId) {
		this.meterialId = meterialId;
	}

	public String getMeterialType() {
		return meterialType;
	}

	public void setMeterialType(String meterialType) {
		this.meterialType = meterialType;
	}

	public String getImageHWs() {
		return imageHWs;
	}

	public void setImageHWs(String imageHWs) {
		this.imageHWs = imageHWs;
	}

	public Integer gettLen() {
		return tLen;
	}

	public void settLen(Integer tLen) {
		this.tLen = tLen;
	}

	public Integer getdLen() {
		return dLen;
	}

	public void setdLen(Integer dLen) {
		this.dLen = dLen;
	}

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}
	
}
