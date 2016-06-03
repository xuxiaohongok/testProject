package com.zhidian.ad.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhidian.ad.domain.AccountFeeMessage;
import com.zhidian.ad.domain.AdBaseMessage;
import com.zhidian.ad.domain.AdCreateRelationMessage;
import com.zhidian.ad.domain.AdImageMessage;
import com.zhidian.ad.domain.AdLandpageMessage;
import com.zhidian.ad.domain.AdMaterialMessage;
import com.zhidian.ad.domain.AdPutStrategyMessage;
import com.zhidian.ad.domain.AdTargetsMessage;
import com.zhidian.ad.service.AdService;
import com.zhidian.common.repository.BaseRepository;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.dsp.util.AdControlLoggerUtil;

@Service
public class AdServiceImpl implements AdService {
	@Resource
	private BaseRepository baseRepository;

	@Override
	public List<Long> getAdList() {
		return baseRepository.findList("getAdList");
	}
	
	@Override
	public List<AdCreateRelationMessage> getAdCreateRelationMessageList(Long adId) {
		return baseRepository.findList(AdCreateRelationMessage.class, "getAdCreateRelationMessageList", adId);
	}

	@Override
	public AdLandpageMessage getAdLandPageMessage(Long landPageId) {
		return baseRepository.findOne(AdLandpageMessage.class, "getAdLandPageMessage", landPageId);
	}

	@Override
	public List<AdPutStrategyMessage> getAdPutStrategyMessage(Long adId) {
		return baseRepository.findList(AdPutStrategyMessage.class, "getAdPutStrategyMessage", adId);
	}

	@Override
	public AdTargetsMessage getAdTargetsMessage(String itemIds) {
		return baseRepository.findOne(AdTargetsMessage.class, "getAdTargetsMessage", itemIds);
	}

	@Override
	public AdMaterialMessage getAdMaterialMessage(Long createId,Long materialId) {
		if(createId == null || materialId == null) {
			CommonLoggerUtil.addBaseLog("==获取素材信息参数为空==========");
			return null;
		}
		
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("createId", createId);
		map.put("materialId", materialId);
		return baseRepository.findOne(AdMaterialMessage.class, "getAdMaterialMessage", map);
	}

	@Override
	public List<AdImageMessage> getAdImageMessage(Long createId, Long materialId) {
		if(createId == null || materialId == null) {
			CommonLoggerUtil.addBaseLog("==获取素材信息参数为空==========");
			return null;
		}
		
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("createId", createId);
		map.put("materialId", materialId);
		return baseRepository.findList(AdImageMessage.class, "getAdImageMessage", map);
	}

	@Override
	public AdTargetsMessage getAdTargetsByParentIdMessage(Long parentId) {
		return getAdTargetsByParentIdMessage(parentId, "name");
	}
	
	@Override
	public AdTargetsMessage getAdTargetsByParentIdMessage(Long parentId, String column) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("parentId", parentId);
		return baseRepository.findOne(AdTargetsMessage.class, "getAdTargetsByParentIdMessage", map);
	}

	@Override
	public AdBaseMessage getAdBaseMessage(Long adId) {
		return baseRepository.findOne(AdBaseMessage.class, "getAdBaseMessage", adId);
	}

	@Override
	public AccountFeeMessage getAccountFeeMessage(Long accountId) {
		return baseRepository.findOne(AccountFeeMessage.class, "getAccountFeeMessage", accountId);
	}

	@Override
	public void updateAdStatus(Long adId, int putState) {
		AdControlLoggerUtil.addTimeLog("更改广告投放状态adId=" + adId + ";putState=" + putState);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adId", adId);
		map.put("putState", putState);
		baseRepository.updateMessage("updateAdStatus", map);
	}

	@Override
	public void updateAdListStatus(List<Long> adIdList) {
		baseRepository.updateMessage("updateAdListStatus", adIdList);
	}
	

}
