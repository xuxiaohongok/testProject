package com.zhidian.dsp.solr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 广告基本信息类
 * @author Administrator
 *
 */
public class AdBaseDocumentSourceMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	
	

	/** 用户ID */
	private Integer adxType;
	
	private Integer adCategory;
	
	private Long adPrice;
	
	//终端类型（1、移动 2、PC端）
	private Integer terminalType;
	
	//运营商
	private String carrier;
	
	//网络类型0	未知  1 WiFi 2 2g 3 3g 4 4g
	private String networkType;
	
	//广告投放平台
	private String osPlatform;
	
	private String timeZones;
	
	private String areas;
	
	private List<AdMaterialDocumentSourceMessage> adMaterialMessageList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAdxType() {
		return adxType;
	}

	public void setAdxType(Integer adxType) {
		this.adxType = adxType;
	}

	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}

	public Long getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Long adPrice) {
		this.adPrice = adPrice;
	}

	public Integer getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Integer terminalType) {
		this.terminalType = terminalType;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getOsPlatform() {
		return osPlatform;
	}

	public void setOsPlatform(String osPlatform) {
		this.osPlatform = osPlatform;
	}

	public String getTimeZones() {
		return timeZones;
	}

	public void setTimeZones(String timeZones) {
		this.timeZones = timeZones;
	}

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}

	public List<AdMaterialDocumentSourceMessage> getAdMaterialMessageList() {
		return adMaterialMessageList;
	}

	public void setAdMaterialMessageList(
			List<AdMaterialDocumentSourceMessage> adMaterialMessageList) {
		this.adMaterialMessageList = adMaterialMessageList;
	}

}
