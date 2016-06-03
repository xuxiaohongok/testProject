package com.zhidian3g.dsp.vo.adcontrol;

import java.util.List;

/**
 * 广告基本信息类
 * @author Administrator
 *
 */
public class AdBaseMessage {

	/**  */
	private Long id;

	/** 用户ID */
	private Integer adxType;
	
	private Integer adType;
	
	private Integer showType;
	
	private Integer adCategory;
	
	//终端类型（1、移动 2、PC端）
	private Integer terminalType;
	
	//广告投放平台
	private String osPlatform;
	
	private String timeZones;
	
	private String areas;
	
	private List<AdMaterialMessage> adMaterialMessageList;

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

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
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

	public List<AdMaterialMessage> getAdMaterialMessageList() {
		return adMaterialMessageList;
	}

	public void setAdMaterialMessageList(
			List<AdMaterialMessage> adMaterialMessageList) {
		this.adMaterialMessageList = adMaterialMessageList;
	}
}
