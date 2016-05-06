package com.zhidian3g.dsp.solr.service;

import com.zhidian3g.dsp.vo.adcontrol.AdBaseMessage;

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
	public boolean addAdToSolr(AdBaseMessage adBaseMessage);
	
	/**
	 * 广告删除索引广告
	 * @param adId
	 * @return
	 */
	public boolean delAdToSolr(Long adId);
	
}
