package com.zhidian.ad.service;

import java.util.List;

import com.zhidian.ad.domain.AccountFeeMessage;
import com.zhidian.ad.domain.AdBaseMessage;
import com.zhidian.ad.domain.AdCreateRelationMessage;
import com.zhidian.ad.domain.AdImageMessage;
import com.zhidian.ad.domain.AdLandpageMessage;
import com.zhidian.ad.domain.AdMaterialMessage;
import com.zhidian.ad.domain.AdPutStrategyMessage;
import com.zhidian.ad.domain.AdTargetsMessage;

public interface AdService {
	
	/**
	 * 
	 * @param adId 获取广告的基本信息
	 * @return
	 */
	public List<Long> getAdList();
	
	public List<AdCreateRelationMessage> getAdCreateRelationMessageList(Long adId);
	
	public AdLandpageMessage getAdLandPageMessage(Long landPageId);
	
	public List<AdPutStrategyMessage> getAdPutStrategyMessage(Long adId);
	
	public AdTargetsMessage getAdTargetsMessage(String itemIds);
	
	public AdTargetsMessage getAdTargetsByParentIdMessage(Long parentId);
	
	public AdTargetsMessage getAdTargetsByParentIdMessage(Long parentId, String column);
	
	public AdMaterialMessage getAdMaterialMessage(Long crateId, Long materialId);
	
	public List<AdImageMessage> getAdImageMessage(Long crateId, Long materialId);

	public AdBaseMessage getAdBaseMessage(Long adId);
	
	public AccountFeeMessage getAccountFeeMessage(Long adId);
	
	/**
	 * 广告状态的设置
	 */
	public void updateAdStatus(Long adId, int putState);
	
	public void updateAdListStatus(List<Long> adId);
	
}
