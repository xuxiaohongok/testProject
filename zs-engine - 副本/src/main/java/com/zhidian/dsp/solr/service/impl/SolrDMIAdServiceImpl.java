package com.zhidian.dsp.solr.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.solr.document.AdBaseMessageDocument;
import com.zhidian.dsp.solr.document.AdMaterialDocument;
import com.zhidian.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian.dsp.solr.service.SolrDMIAdService;
import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;
import com.zhidian.dsp.solr.vo.AdMaterialDocumentSourceMessage;
import com.zhidian.dsp.util.SolrUtil;

@Service("solrDMIAdService")
public class SolrDMIAdServiceImpl implements SolrDMIAdService {

	@Override
	public boolean addAdToSolr(AdBaseDocumentSourceMessage adBaseMessage) {
		CommonLoggerUtil.addBaseLog("===============调用添加广告索引文档接口==========================");
		try {
			List<AdMaterialDocumentSourceMessage> adMaterialMesList = adBaseMessage.getAdMaterialMessageList();
			//广告基本信息文档
			Long adId = adBaseMessage.getId();
			AdBaseMessageDocument adBaseMessageDocument = new AdBaseMessageDocument();
			adBaseMessageDocument.setId("adId-" + adId);
			adBaseMessageDocument.setDocumentType("adBaseType");
			adBaseMessageDocument.setAdId(adBaseMessage.getId());
			adBaseMessageDocument.setAdxType( DspConstant.ADX_TYPE + adBaseMessage.getAdxType());
			adBaseMessageDocument.setAdCategory( DspConstant.AD_CATEGORY + adBaseMessage.getAdCategory());
			adBaseMessageDocument.setAdPrice(adBaseMessage.getAdPrice());
			adBaseMessageDocument.setTerminalType("terminalType" + adBaseMessage.getTerminalType());
			adBaseMessageDocument.setOsPlatform(adBaseMessage.getOsPlatform());
			adBaseMessageDocument.setNetworkType(adBaseMessage.getNetworkType());
			adBaseMessageDocument.setTimeZones(adBaseMessage.getTimeZones());
			adBaseMessageDocument.setAreas(adBaseMessage.getAreas());
			
			//添加广告基本信息文档
			AdDspDocumentManager.getInstance().createDocumentIndex(adBaseMessageDocument);
			
			if(adMaterialMesList == null || adMaterialMesList.size() == 0) {
				CommonLoggerUtil.addBaseLog("==没有素材信息=============");
				return true;
			}
			
			/**
			 * 添加素材文档信息
			 */
			for (final AdMaterialDocumentSourceMessage adMaterialMessage : adMaterialMesList) {
				final AdMaterialDocument adMaterialDocument = new AdMaterialDocument();
				adMaterialDocument.setId("adId-" + adId + "-createId-" + adMaterialMessage.getCreateId() + "-meterialId-" + adMaterialMessage.getMeterialId());
				adMaterialDocument.setDocumentType("materialType");
				adMaterialDocument.setCreateId(adMaterialMessage.getCreateId().intValue());
				adMaterialDocument.setAdId(adBaseMessage.getId());
				adMaterialDocument.setMeterialId(adMaterialMessage.getMeterialId().intValue());
				
				//创意类型
				int meterIanType = adMaterialMessage.getMeterialType();
				adMaterialDocument.setMeterialType(DspConstant.METERIALTYPE + meterIanType);//附带原生广告
				
				//根据创意类型设置对应的索引属性值
				
				//1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
				int adType = 1;
				if(meterIanType == 1) {
					adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
				} else if(meterIanType == 2 || meterIanType == 4) {//图文广告
					adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
					adMaterialDocument.settLen(adMaterialMessage.gettLen());
					adType = 2;
				} else if(meterIanType == 3) {//图文描述
					adType = 2;
					adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
					adMaterialDocument.settLen(adMaterialMessage.gettLen());
					adMaterialDocument.setdLen(adMaterialMessage.getdLen());
				} else if(meterIanType == 5) {
					adType = 11;
					adMaterialDocument.settLen(adMaterialMessage.gettLen());
				}
				adMaterialDocument.setAdType(DspConstant.AD_TYPE + adType + " " + DspConstant.AD_TYPE + 7);
				//添加广告素材文档
				AdDspDocumentManager.getInstance().createDocumentIndex(adMaterialDocument);
			}
			CommonLoggerUtil.addBaseLog("===============成功添加索引广告==========================");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean delAdFromSolr(Long adId) {
		CommonLoggerUtil.addBaseLog("===============调用删除索引广告文档========adId=" + adId);
		try {
			AdDspDocumentManager.getInstance().deleteDocumentByQuery("adId:" + adId);
			CommonLoggerUtil.addBaseLog("===============成功删除广告索引adId=" + adId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean delAdMaterlialFormSolr(Long adId, Long createId,
			Long materialId) {
		CommonLoggerUtil.addBaseLog("===============调用删除广告素材文档========adId=" + adId);
		try {
			String materialSolrId = "adId-" + adId + "-createId-" + createId + "-meterialId-" + materialId;
			AdDspDocumentManager.getInstance().deleteDocumentById("id:" + materialSolrId);
			CommonLoggerUtil.addBaseLog("===============成功删除广告索引adId=" + adId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<Long> getAllAdId() {
		CommonLoggerUtil.addBaseLog("===============获取线上投放广告的索引========");
		List<Long> adList = null;
		try {
			adList = AdDspDocumentManager.getInstance().searchAdDocumentId("documentType:adBaseType");
			CommonLoggerUtil.addBaseLog("===============线上投放广告的索引有========" + adList);
		} catch (Exception e) {
			SolrUtil.saveExceptionLogAndSendEmail(e);
		}
		return adList;
	}

	@Override
	public boolean delAllAd() {
		CommonLoggerUtil.addBaseLog("===============移除所有的索引文档========");
		return AdDspDocumentManager.getInstance().deleteAllDocument();
	}

}
