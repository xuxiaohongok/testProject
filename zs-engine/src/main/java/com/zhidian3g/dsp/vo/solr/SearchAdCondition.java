package com.zhidian3g.dsp.vo.solr;

public class SearchAdCondition {
	
	private String ip;
	
	private String adxType;
	
	//获取终端类型：电脑、移动端 1、移动端  2、PC端
	private String terminalType;
	
	private String osPlatform;
	
	private String adType;
	
	private String adCategory; 
	
	//不支持广告行业类型
	private String unSupportAdCategory;
	
	public SearchAdCondition(String adxType, String terminalType, String osPlatform, String adType, String ip) {
		super();
		this.terminalType = terminalType;
		this.adxType = adxType;
		this.adType = adType;
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAdxType() {
		return adxType;
	}

	public void setAdxType(String adxType) {
		this.adxType = adxType;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getOsPlatform() {
		return osPlatform;
	}

	public void setOsPlatform(String osPlatform) {
		this.osPlatform = osPlatform;
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

	public String getUnSupportAdCategory() {
		return unSupportAdCategory;
	}

	public void setUnSupportAdCategory(String unSupportAdCategory) {
		this.unSupportAdCategory = unSupportAdCategory;
	}
	
}
