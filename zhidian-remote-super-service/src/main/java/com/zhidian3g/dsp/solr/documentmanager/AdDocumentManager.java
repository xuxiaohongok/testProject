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
import com.zhidian3g.common.mail.MailService;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.common.util.PropertiesUtil;
import com.zhidian3g.common.util.Utils;
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
	private int excetionCount = 0;
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
				return null;
			}
			
			for(SolrDocument doc : docs ) {
				adIdList.add((Long)doc.getFieldValue("adId"));
			}
			
		} catch (SolrServerException e) {
			saveExceptionLogAndSendEmail(e);
			return null;
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
			query.addFilterQuery(new String[]{fifterString});
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
				return null;
			}
			
			for(SolrDocument doc : docs ) {
				adIdList.add((Long)doc.getFieldValue("adId"));
			}
			
			
//			List<DspAdDocument> listAdDocumnt = response.getBeans(DspAdDocument.class);
//			if(listAdDocumnt == null || listAdDocumnt.size() == 0) {
//				logger.info("= 根据条件获取不到相应的广告 =");
//				return null;
//			}
//			
//			logger.info("查询时间：" + response.getQTime());
//			for (DspAdDocument adDocument : listAdDocumnt) {
//				adIdList.add(adDocument.getAdId());
//			}
			
			return adIdList;
		} catch (SolrServerException e) {
			excetionCount++;
			if(excetionCount == 3) {
				String exceptionString = CommonLoggerUtil.getExceptionString(e);
				MailService.send("广告适配器solr查询异常", "solr查询异常3次了==========   <br>" + exceptionString);
				CommonLoggerUtil.addExceptionLog(e);
				Utils.sleepTime(5);
				/***异常出现三次重启服务器***********************/
				System.exit(0);
			}
			
			saveExceptionLogAndSendEmail(e);
			return null;
		}
	}
	
	/**
	 * 
	 * solr异常同时发送邮件报警
	 * @param e
	 */
	private Integer solrExceptionCount = 0;
	private void saveExceptionLogAndSendEmail(Exception e) {
		solrExceptionCount++;
		String exception = CommonLoggerUtil.getExceptionString(e);
		CommonLoggerUtil.addExceptionLog(e);
		MailService.send(DspConstant.IP_ADRESS + " Dsp适配器系统solr异常", "sorl异常=============<br>" + exception);
		Utils.sleepTime(10);
		if(solrExceptionCount == 3) {
			System.exit(0);
		}
	}
}
