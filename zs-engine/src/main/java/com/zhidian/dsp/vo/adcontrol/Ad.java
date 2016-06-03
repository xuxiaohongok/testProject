package com.zhidian.dsp.vo.adcontrol;


public class Ad {

	/**  */
	private Long id;

	/** 用户ID */
	private Integer adxType;
	
	private Integer adType;
	
	private Integer showType;
	
	private Integer adCategory;
	
	private String adName;
	
	//广告投放平台
	private String adPlatform;
	
	private Integer height;
	
	private Integer width;
	
	private String imageURL;
	
	private String timeZones;
	
	private String title;
	
	private String areas;

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

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getAdPlatform() {
		return adPlatform;
	}

	public void setAdPlatform(String adPlatform) {
		this.adPlatform = adPlatform;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getTimeZones() {
		return timeZones;
	}

	public void setTimeZones(String timeZones) {
		this.timeZones = timeZones;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}
}
