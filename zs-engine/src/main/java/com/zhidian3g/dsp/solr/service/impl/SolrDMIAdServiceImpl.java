package com.zhidian3g.dsp.solr.service.impl;

import java.util.List;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.dsp.solr.document.AdBaseMessageDocument;
import com.zhidian3g.dsp.solr.document.AdMaterialDocument;
import com.zhidian3g.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian3g.dsp.solr.service.SolrDMIAdService;
import com.zhidian3g.dsp.vo.adcontrol.AdBaseMessage;
import com.zhidian3g.dsp.vo.adcontrol.AdMaterialMessage;

public class SolrDMIAdServiceImpl implements SolrDMIAdService {

	@Override
	public boolean addAdToSolr(AdBaseMessage adBaseMessage) {
		CommonLoggerUtil.addBaseLog("===============调用添加广告索引文档接口==========================");
		List<AdMaterialMessage> adMaterialMesList = adBaseMessage.getAdMaterialMessageList();
		//广告基本信息文档
		long adId = adBaseMessage.getId();
		AdBaseMessageDocument adBaseMessageDocument = new AdBaseMessageDocument();
		adBaseMessageDocument.setId("adId-" + adId);
		adBaseMessageDocument.setAdId(adBaseMessage.getId());
		adBaseMessageDocument.setAdxType( DspConstant.ADX_TYPE + adBaseMessage.getAdxType());
		adBaseMessageDocument.setAdType( DspConstant.AD_TYPE + adBaseMessage.getAdType());
		adBaseMessageDocument.setAdCategory( DspConstant.AD_CATEGORY + adBaseMessage.getAdCategory());
		adBaseMessageDocument.setTerminalType("terminalType" + adBaseMessage.getTerminalType());
		adBaseMessageDocument.setOsPlatform(adBaseMessage.getOsPlatform());
		adBaseMessageDocument.setTimeZones(adBaseMessage.getTimeZones());
		adBaseMessageDocument.setAreas(adBaseMessage.getAreas());
		
		//添加广告基本信息文档
		AdDspDocumentManager.getInstance().createDocumentIndex(adBaseMessageDocument);
		
		/**
		 * 添加素材文档信息
		 */
		for (final AdMaterialMessage adMaterialMessage : adMaterialMesList) {
			final AdMaterialDocument adMaterialDocument = new AdMaterialDocument();
			adMaterialDocument.setId("adId-" + adId + "-createId-" + adMaterialMessage.getCreateId() + "-meterialId-" + adMaterialMessage.getMeterialId());
			adMaterialDocument.setCreateId(adMaterialMessage.getCreateId());
			adMaterialDocument.setAdId(adBaseMessage.getId());
			adMaterialDocument.setMeterialId(adMaterialMessage.getMeterialId());
			
			//创意类型
			int meterIanType = adMaterialMessage.getMeterialType();
			adMaterialDocument.setMeterialType("meterialType-" + meterIanType + " meterialType-7");//附带原生广告
			
			//根据创意类型设置对应的索引属性值
			
			//1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
			if(meterIanType == 1) {
				adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
			} else if(meterIanType == 2 || meterIanType == 4) {//图文广告
				adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
				adMaterialDocument.settLen(adMaterialMessage.gettLen());
			} else if(meterIanType == 3) {//图文描述
				adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
				adMaterialDocument.settLen(adMaterialMessage.gettLen());
				adMaterialDocument.setdLen(adMaterialMessage.getdLen());
			} else if(meterIanType == 5) {
				adMaterialDocument.settLen(adMaterialMessage.gettLen());
			}
			
			//添加广告素材文档
			AdDspDocumentManager.getInstance().createDocumentIndex(adMaterialDocument);
		}
		CommonLoggerUtil.addBaseLog("===============成功添加索引广告==========================");
		return true;
	}

	@Override
	public boolean delAdToSolr(Long adId) {
		CommonLoggerUtil.addBaseLog("===============调用删除索引广告文档========adId=" + adId);
		AdDspDocumentManager.getInstance().deleteDocumentById("adId:" + adId);
		CommonLoggerUtil.addBaseLog("===============成功删除广告索引adId=" + adId);
		return true;
	}

}
