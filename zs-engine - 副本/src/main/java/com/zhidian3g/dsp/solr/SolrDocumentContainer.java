package com.zhidian3g.dsp.solr;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian3g.dsp.solr.document.DspAdDocument;
import com.zhidian3g.dsp.vo.adcontrol.Ad;

/**
 * 索引文档添加类
 * @author Administrator
 *
 */
public class SolrDocumentContainer {
	
	/**
	 *  创建广告索引文档
	 * @param ad
	 * @return
	 */
	public static DspAdDocument createDspAdDocument(Ad ad){
		if(ad == null) return null;
		String title = ad.getTitle();
		DspAdDocument adDocument = new DspAdDocument();
		adDocument.setId("id" + ad.getId());
		adDocument.setAdxType(DspConstant.ADX_TYPE + ad.getAdxType());
		adDocument.setAdId(ad.getId());
		adDocument.setAdHW(ad.getHeight() + "*" + ad.getWidth());
		adDocument.setAdType(DspConstant.AD_TYPE + ad.getAdxType());
		adDocument.setAdCategory(DspConstant.AD_CATEGORY + ad.getAdCategory());
		adDocument.setAdPlatform(ad.getAdPlatform());
		adDocument.setAreas(ad.getAreas());
		adDocument.setLength(title.length());
		adDocument.setTimeZones(ad.getTimeZones());
		adDocument.setAdType(DspConstant.AD_TYPE + ad.getAdType());
    	return adDocument;
    }
	
}
