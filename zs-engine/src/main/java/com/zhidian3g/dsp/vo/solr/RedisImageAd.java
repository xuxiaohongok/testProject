package com.zhidian3g.dsp.vo.solr;

public class RedisImageAd {
	//广告id
	private Long adId;
	//图片宽度
	private Integer width;
	//图片高度
	private Integer height;
	//广告行业类别
	private Integer adCategory;
	
	private Integer clickType;
	//广告落地页
	private String landingPage;
	
	private Integer createId;
	
	private Integer landingPageId;
	
	//广告包名
	private String adPackage;
	
	private String adDownload;
	
	//广告素材
	private String adMUrl;

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}

	public Integer getClickType() {
		return clickType;
	}

	public void setClickType(Integer clickType) {
		this.clickType = clickType;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public Integer getLandingPageId() {
		return landingPageId;
	}

	public void setLandingPageId(Integer landingPageId) {
		this.landingPageId = landingPageId;
	}

	public String getAdPackage() {
		return adPackage;
	}

	public void setAdPackage(String adPackage) {
		this.adPackage = adPackage;
	}

	public String getAdDownload() {
		return adDownload;
	}

	public void setAdDownload(String adDownload) {
		this.adDownload = adDownload;
	}

	public String getAdMUrl() {
		return adMUrl;
	}

	public void setAdMUrl(String adMUrl) {
		this.adMUrl = adMUrl;
	}

}
