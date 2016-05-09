package com.zhidian3g.dsp.vo.solr;

public class SearchAdCondition {
	
	private String ip;
	
	private Integer adxType;
	
	//获取终端类型：电脑、移动端 1、移动端  2、PC端
	private Integer terminalType;
	
	private String osPlatform;
	
	private Integer adType;
	
	private String adCategory; 
	
	//不支持广告行业类型
	private String unSupportAdCategory;
	
	public SearchAdCondition(Integer adxType, Integer terminalType, String osPlatform, Integer adType, String ip) {
		super();
		this.adxType = adxType;
		this.terminalType = terminalType;
		this.osPlatform = osPlatform;
		this.adType = adType;
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getAdxType() {
		return adxType;
	}

	public void setAdxType(Integer adxType) {
		this.adxType = adxType;
	}

	public Integer getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Integer terminalType) {
		this.terminalType = terminalType;
	}

	public String getOsPlatform() {
		return osPlatform;
	}

	public void setOsPlatform(String osPlatform) {
		this.osPlatform = osPlatform;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public String getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(String adCategory) {
		this.adCategory = adCategory;
	}

	public String getUnSupportAdCategory() {
		return unSupportAdCategory;
	}

	public void setUnSupportAdCategory(String unSupportAdCategory) {
		this.unSupportAdCategory = unSupportAdCategory;
	}

}
