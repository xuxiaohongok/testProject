package com.zhidian3g.dsp.vo.solr;

public class SearchAdCondition {
	
	private String ip;
	
	private String userId;
	
	private String adxType;
	
	//获取终端类型：电脑、移动端 1、移动端2、PC端
	private int terminalType;
	
	private String osType;
	
	//各个图片的长宽
	private String imageTypeHWs;
	
	private String showType;
	
	private String adType;
	
	private Integer adCategory; 
	
	//title 标题长度
	private Integer TH;
	
	//描述长度
	private Integer DH;
	
	public SearchAdCondition(String userId, String adxType, String showType,String adType, String osType, String ip) {
		super();
		this.userId = userId;
		this.adxType = adxType;
		this.showType = showType;
		this.adType = adType;
		this.osType = osType;
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAdxType() {
		return adxType;
	}

	public void setAdxType(String adxType) {
		this.adxType = adxType;
	}

	public int getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(int terminalType) {
		this.terminalType = terminalType;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getImageTypeHWs() {
		return imageTypeHWs;
	}

	public void setImageTypeHWs(String imageTypeHWs) {
		this.imageTypeHWs = imageTypeHWs;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}

	public Integer getTH() {
		return TH;
	}

	public void setTH(Integer tH) {
		TH = tH;
	}

	public Integer getDH() {
		return DH;
	}

	public void setDH(Integer dH) {
		DH = dH;
	}
	
}
