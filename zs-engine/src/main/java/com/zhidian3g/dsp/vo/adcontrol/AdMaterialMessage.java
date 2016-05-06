package com.zhidian3g.dsp.vo.adcontrol;

/**
 * 广告基本信息类
 * @author Administrator
 *
 */
public class AdMaterialMessage {

	/** 用户ID */
	private Integer createId;
	
	private Integer meterialId;
	
	private Integer meterialType;
	
	private String imageHWs;
	
	private Integer tLen;
	
	private Integer dLen;

	public AdMaterialMessage(Integer createId, Integer meterialId,
			Integer meterialType, String imageHWs, Integer tLen, Integer dLen) {
		super();
		this.createId = createId;
		this.meterialId = meterialId;
		this.meterialType = meterialType;
		this.imageHWs = imageHWs;
		this.tLen = tLen;
		this.dLen = dLen;
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
