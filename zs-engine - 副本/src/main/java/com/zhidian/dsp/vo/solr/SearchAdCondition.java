package com.zhidian.dsp.vo.solr;

import com.zhidian.dsp.constant.DspConstant;

public class SearchAdCondition {
	
	private String area;
	
	private Integer adxType;
	
	private Long adPrice;
	
	//获取终端类型：电脑、移动端 1、移动端  2、PC端
	private Integer terminalType;
	
	private String osPlatform;
	
	private String networkType;
	
	private String adCategory; 
	
	//不支持广告行业类型
	private String unSupportAdCategory;
	
	public SearchAdCondition(Integer adxType, Long adPrice, Integer terminalType, Integer osPlatform,Integer networkType, String area) {
		super();
		this.adxType = adxType;
		this.adPrice = adPrice;
		this.terminalType = terminalType;
		this.osPlatform = getOSString(osPlatform);
		this.networkType = getNetworkTypeString(networkType);
		this.area = area;
	}

	public Long getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Long adPrice) {
		this.adPrice = adPrice;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
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
	
	/**
	 * 获取系统类型
	 * @param osTypeId
	 * @return
	 */
	private String getOSString(int osTypeId) {
		if(osTypeId == 1) {
			return DspConstant.OS_IOS;
		} else if(osTypeId == 2) {
			return DspConstant.OS_ANDROID;
		} else if(osTypeId == 3) {
			return DspConstant.OS_WINDOWS;
		}
		return null;
	}
	
	/**
	 * 获取网络类型
	 * @param osTypeId
	 * @return
	 */
	private String getNetworkTypeString(Integer networkType) {
		if(networkType != null) {
			if(networkType == 1) {
				return "WiFi";
			} else if(networkType == 2) {
				return "2G";
			} else if(networkType == 3) {
				return "3G";
			} else if(networkType == 4) {
				return "4G";
			} 
		}
		
		return null;
	}

}
