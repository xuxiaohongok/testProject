package com.zhidian.ad.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdImageMessage implements Serializable{
	//图片长
	private int height;
	//图片宽
	private int width;
	//广告素材地址
	private String imageURL;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
