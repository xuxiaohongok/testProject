package com.zhidian.dsp.solr.service;

import java.util.List;

import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;

/**
 * 查询更新索引文档接口
 * @author Administrator
 *
 */
public interface SolrDMIAdService {
	
	/**
	 * 添加或更新索引广告
	 * @param adBaseMessage
	 * @return
	 */
	public boolean addAdToSolr(AdBaseDocumentSourceMessage adBaseMessage);
	
	/**
	 * 广告删除索引广告
	 * @param adId
	 * @return
	 */
	public boolean delAdFromSolr(Long adId);
	
	/**
	 * 
	 * @param adId
	 * @return
	 */
	public boolean delAdMaterlialFormSolr(Long adId, Long createId, Long materialId);
	
	public boolean delAllAd();
	
	/**
	 * 获取广告集合id
	 * @return
	 */
	public List<Long> getAllAdId();
	
}
