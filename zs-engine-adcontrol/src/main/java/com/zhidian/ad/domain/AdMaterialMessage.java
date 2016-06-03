package com.zhidian.ad.domain;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdMaterialMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long createId;
	
	private Long materialId;
	
	private Integer materialType;
	
	private String title;
	
	private String description;
	
	private Map<String, AdImageMessage> mapAdImage;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
	public Integer getMaterialType() {
		return materialType;
	}
	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, AdImageMessage> getMapAdImage() {
		return mapAdImage;
	}

	public void setMapAdImage(Map<String, AdImageMessage> mapAdImage) {
		this.mapAdImage = mapAdImage;
	}
	
}
