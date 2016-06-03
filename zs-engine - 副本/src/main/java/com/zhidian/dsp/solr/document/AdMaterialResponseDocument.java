package com.zhidian.dsp.solr.document;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 广告索引文档
 * @author Administrator
 *
 */
public class AdMaterialResponseDocument {
	
	@Field
	private Long adId;
	
	@Field
	private Integer createId;
	
	@Field
	private Integer meterialId;

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public Integer getMeterialId() {
		return meterialId;
	}

	public void setMeterialId(Integer meterialId) {
		this.meterialId = meterialId;
	}
	
	
	
}
