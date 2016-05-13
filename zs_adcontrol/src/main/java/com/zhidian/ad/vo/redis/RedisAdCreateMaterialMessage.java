package com.zhidian.ad.vo.redis;

public class RedisAdCreateMaterialMessage {

	/** 用户ID */
	private Integer createId;
	
	private Integer meterialId;
	
	private Integer meterialType;
	
	private String title;
	
	private String detail;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
