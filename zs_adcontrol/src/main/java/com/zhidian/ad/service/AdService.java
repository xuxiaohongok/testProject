package com.zhidian.ad.service;

import java.util.List;

import com.zhidian.ad.domain.Ad;

public interface AdService {
	
	/**
	 * 
	 * @param adId 获取广告的基本信息
	 * @return
	 */
	public List<Ad> getAdById();
	
	
			
}
