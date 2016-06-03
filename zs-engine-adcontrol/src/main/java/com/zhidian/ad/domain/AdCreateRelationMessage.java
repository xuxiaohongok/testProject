package com.zhidian.ad.domain;

public class AdCreateRelationMessage {
	private Long adId;
	private Long createId;
	private Long materialId;
	private Long landPageId;
	private Integer materialType;
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
	public Long getLandPageId() {
		return landPageId;
	}
	public void setLandPageId(Long landPageId) {
		this.landPageId = landPageId;
	}
	public Integer getMaterialType() {
		return materialType;
	}
	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}
}
