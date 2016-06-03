package com.zhidian3g.dsp.solr.documentmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.util.SolrUtil;
import com.zhidian3g.common.util.PropertiesUtil;
import com.zhidian3g.dsp.solr.SolrServerFactory;
import com.zhidian3g.dsp.solr.document.DspAdDocument;

public class AdDocumentManager extends DocumentManager<DspAdDocument>{
	private static final String SOLRURL = PropertiesUtil.getInstance().getValue("solr.httpsolrserver.url");
//	private static SolrServer solrServer = SolrServerFactory.getHttpSolrServer(SOLRURL);
	
	/**
	 * 私有构造器
	 */
	private AdDocumentManager(SolrServer solrServer) {
		super(solrServer);
	}
	
	
    private static class SolrDocumentManagerUtilHolder{
        private static AdDocumentManager instance = new AdDocumentManager(SolrServerFactory.getHttpSolrServer(SOLRURL));
    } 

    /**
     * 获取单列AdDocumentManager
     * @return
     */
	public static AdDocumentManager getInstance() {
		return SolrDocumentManagerUtilHolder.instance;
	}
	
	/**
	 * 查询文档
	 * @param condition
	 * @return
	 */
	private Logger logger = LoggerFactory.getLogger("stdout");
	public List<Long> searchAdDocumentId(String condition) {
		long startTime = System.currentTimeMillis();
		List<Long> adIdList = new ArrayList<Long>();
		try {
			SolrQuery query = new SolrQuery();// 查询全部
			query.setFields("adId");
			query.setRows(DspConstant.DEFAULT_ROW);
			query.setQuery(condition);
			QueryResponse response = solrServer.query(query);
			
			SolrDocumentList docs = response.getResults();
			logger.info("查询时间：" + response.getQTime() + "; 查询文档个数：" + docs.getNumFound());
			if(docs == null || docs.size() == 0) {
				logger.info("= 根据条件获取不到相应的广告 =");
				return adIdList;
			}
			
			for(SolrDocument doc : docs ) {
				adIdList.add((Long)doc.getFieldValue("adId"));
			}
			
		} catch (SolrServerException e) {
			SolrUtil.saveExceptionLogAndSendEmail(e);
			return adIdList;
		}
		
		logger.info("查询广告共有：" + adIdList  +";用时：" + ((System.currentTimeMillis() - startTime)/1000.0));
		return adIdList;
	}
	
	/**
	 * 带过滤条件查询
	 * @param condition
	 * @param fifterString
	 * @return
	 */
	public List<Long> searchAdDocumentId(String condition, String fifterString) {
		long startTime = System.currentTimeMillis();
		SolrQuery query = new SolrQuery();// 查询全部
		query.setRows(DspConstant.DEFAULT_ROW);
		if(fifterString != null) {
			query.addFilterQuery(fifterString);
		}
		
		query.setQuery(condition);
		List<Long> adIdList = getAdDocmentList(query);
		logger.info("查询广告共有：" + adIdList + ";用时：" + ((System.currentTimeMillis() - startTime)/1000.0));
		return adIdList;
	}

	private List<Long> getAdDocmentList(SolrQuery query) {
		List<Long> adIdList = new ArrayList<Long>();
		try {
			query.setFields("adId");
			QueryResponse response = solrServer.query(query);
			
			SolrDocumentList docs = response.getResults();
			logger.info("查询时间：" + response.getQTime() + "; 查询文档个数：" + docs.getNumFound());
			if(docs == null || docs.size() == 0) {
				logger.info("= 根据条件获取不到相应的广告 =");
				return adIdList;
			}
			
			for(SolrDocument doc : docs ) {
				adIdList.add((Long)doc.getFieldValue("adId"));
			}
			
			
//			List<AdMaterialResponseDocument> listAdDocumnt = response.getBeans(AdMaterialResponseDocument.class);
//			if(listAdDocumnt == null || listAdDocumnt.size() == 0) {
//				logger.info("= 根据条件获取不到相应的广告 =");
//				return null;
//			}
//			
//			logger.info("查询时间：" + response.getQTime());
//			for (AdMaterialResponseDocument adDocument : listAdDocumnt) {
//				adIdList.add(adDocument.getAdId());
//			}
			
			return adIdList;
		} catch (SolrServerException e) {
			SolrUtil.saveExceptionLogAndSendEmail(e);
			return adIdList;
		}
	}
	
}
