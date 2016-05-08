package com.zhidian3g.dsp.vo.ad;

public class RedisAdCreatePackageMessage {
	//广告id
	private Long adId;
	//广告标题
	private String title;
	//广告素材地址
	private String imgURL;
	//广告行业类别
	private Integer adCategory;
	//广告落地页
	private String landingPage;
	//广告包名
	private String adPackage;
	//广告名称
	private String adName;
	
	private Integer createId;
	
	private Integer landingPageId;
	
	private Long adPrice;
	
	private String logoURL;
	
	private String iconURL;
	
	public Long getAdId() {
		return adId;
	}
	public void setAdId(Long adId) {
		this.adId = adId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	public Integer getAdCategory() {
		return adCategory;
	}
	public void setAdCategory(Integer adCategory) {
		this.adCategory = adCategory;
	}
	public String getLandingPage() {
		return landingPage;
	}
	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public String getAdPackage() {
		return adPackage;
	}
	public void setAdPackage(String adPackage) {
		this.adPackage = adPackage;
	}
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	public Integer getCreateId() {
		return createId;
	}
	public void setCreateId(Integer createId) {
		this.createId = createId;
	}
	public Long getAdPrice() {
		return adPrice;
	}
	public void setAdPrice(Long adPrice) {
		this.adPrice = adPrice;
	}
	public Integer getLandingPageId() {
		return landingPageId;
	}
	public void setLandingPageId(Integer landingPageId) {
		this.landingPageId = landingPageId;
	}
	public String getLogoURL() {
		return logoURL;
	}
	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	public String getIconURL() {
		return iconURL;
	}
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
}
