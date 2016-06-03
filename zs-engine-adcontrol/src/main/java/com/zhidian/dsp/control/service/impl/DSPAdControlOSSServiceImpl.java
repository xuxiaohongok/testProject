package com.zhidian.dsp.control.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhidian.dsp.control.service.DSPAdControlOSSService;
import com.zhidian.dsp.control.service.vo.DspControlOssAdMaterial;
import com.zhidian.dsp.util.AdControlLoggerUtil;

@Service
public class DSPAdControlOSSServiceImpl implements DSPAdControlOSSService{

	@Resource
	private AdHanderService adHanderService;
	
	
	@Override
	public String addAd(Long adId) {
		AdControlLoggerUtil.addTimeLog("===调用添加广告接口=========" + adId);
		adHanderService.addAd(adId);
		return null;
	}

	@Override
	public String addAdMaterial(Long adId, Long createId, Long materialId, Long landId) {
		return null;
	}

	@Override
	public String delAd(Long adId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delAdMaterial(DspControlOssAdMaterial dspControlOssAdMaterial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delAllAdMaterial(List<DspControlOssAdMaterial> dspControlOssAdMaterial) {
		return null;
	}
	
	

}
