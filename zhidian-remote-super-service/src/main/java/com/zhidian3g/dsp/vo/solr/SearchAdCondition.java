package com.zhidian3g.dsp.vo.solr;

import com.zhidian.dsp.constant.DspConstant;


public class SearchAdCondition {
	
	private String userId;
	
	private String adxType;
	
	private String adHW;
	
	private String showType;
	
	private String adType;
	
	private Integer length;
	
	private String ip;
	
	public SearchAdCondition(String userId, String adxType, String showType, String ip) {
		super();
		this.userId = userId;
		this.adxType = adxType;
		this.showType = showType;
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

	public String getAdHW() {
		return adHW;
	}

	public void setAdHW(String adHW) {
		this.adHW = adHW;
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

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
