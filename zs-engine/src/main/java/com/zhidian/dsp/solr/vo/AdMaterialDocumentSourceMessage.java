package com.zhidian.dsp.solr.vo;

import java.io.Serializable;

/**
 * 广告基本信息类
 * @author Administrator
 *
 */
public class AdMaterialDocumentSourceMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 用户ID */
	private Long createId;
	
	private Long meterialId;
	
	private Integer meterialType;
	
	private String imageHWs;
	
	private Integer tLen;
	
	private Integer dLen;

	public AdMaterialDocumentSourceMessage(Long createId, Long meterialId,
			Integer meterialType, String imageHWs, Integer tLen, Integer dLen) {
		super();
		this.createId = createId;
		this.meterialId = meterialId;
		this.meterialType = meterialType;
		this.imageHWs = imageHWs;
		this.tLen = tLen;
		this.dLen = dLen;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getMeterialId() {
		return meterialId;
	}

	public void setMeterialId(Long meterialId) {
		this.meterialId = meterialId;
	}

	public Integer getMeterialType() {
		return meterialType;
	}

	public void setMeterialType(Integer meterialType) {
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

}
