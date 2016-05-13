package com.zhidian.ad.vo.redis;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RedisAdImage {
	//图片长
	private int height;
	//图片宽
	private int width;
	//广告素材地址
	private String imgURL;
	
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
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
}
