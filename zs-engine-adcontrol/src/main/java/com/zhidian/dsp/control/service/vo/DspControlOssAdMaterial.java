package com.zhidian.dsp.control.service.vo;

import java.io.Serializable;

public class DspControlOssAdMaterial implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long adId;
	private Long createId;
	private Long materialId;
	private Long landId;
	public Long getAdId() {
		return adId;
	}
	public void setAdId(Long adId) {
		this.adId = adId;
	}
	public Long getCreateId() {
		return createId;
	}
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Long getLandId() {
		return landId;
	}
	public void setLandId(Long landId) {
		this.landId = landId;
	}
	
}
