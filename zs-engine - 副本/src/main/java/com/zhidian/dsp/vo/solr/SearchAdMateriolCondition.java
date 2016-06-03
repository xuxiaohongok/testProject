package com.zhidian.dsp.vo.solr;

public class SearchAdMateriolCondition {
	
	private Integer adType;
	
	//素材类型
	private Integer meterialType;
	
	//标题的长度
	private Integer tLen;
	
	//描述的长度
	private Integer dLen;
	
	//图片的长宽
	private String imageHW;
	
	
	
	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Integer getMeterialType() {
		return meterialType;
	}

	public void setMeterialType(Integer meterialType) {
		this.meterialType = meterialType;
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

	public String getImageHW() {
		return imageHW;
	}

	public void setImageHW(String imageHW) {
		this.imageHW = imageHW;
	}
	
}
