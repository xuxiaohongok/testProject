package com.zhidian.remote.service;

import java.io.Serializable;

public class Condition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int adxType;
	Long adId;
	int height;
	int width;
	String imageURL;
	String landingPageURL;
	public Condition(int adxType, Long adId, int height, int width,
			String imageURL, String landingPageURL) {
		super();
		this.adxType = adxType;
		this.adId = adId;
		this.height = height;
		this.width = width;
		this.imageURL = imageURL;
		this.landingPageURL = landingPageURL;
	}
	public int getAdxType() {
		return adxType;
	}
	public Long getAdId() {
		return adId;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public String getImageURL() {
		return imageURL;
	}
	public String getLandingPageURL() {
		return landingPageURL;
	}
}