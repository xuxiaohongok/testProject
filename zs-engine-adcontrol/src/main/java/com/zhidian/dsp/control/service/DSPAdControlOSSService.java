package com.zhidian.dsp.control.service;

import java.util.List;

import com.zhidian.dsp.control.service.vo.DspControlOssAdMaterial;

public interface DSPAdControlOSSService {
	
	/**
	 * 添加广告
	 * @param adId
	 * @return
	 */
	public String addAd(Long adId);
	
	/**
	 * 
	 * 添加广告素材
	 * @param adId
	 * @param createId
	 * @param materialId
	 * @param landId
	 * @return
	 */
	public String addAdMaterial(Long adId, Long createId, Long materialId, Long landId);
	
	/**
	 * 暂停广告
	 * @param adId
	 * @return
	 */
	public String delAd(Long adId);
	
	/**
	 * 广告素材暂停投放
	 * @param adId
	 * @param createId
	 * @param materialId
	 * @param landId
	 * @return
	 */
	public String delAdMaterial(DspControlOssAdMaterial dspControlOssAdMaterial);
	
	/**
	 * 批量素材广告暂停投放
	 * @param dspControlOssAdMaterial
	 * @return
	 */
	public String delAllAdMaterial(List<DspControlOssAdMaterial> dspControlOssAdMaterial);
	
	
}
