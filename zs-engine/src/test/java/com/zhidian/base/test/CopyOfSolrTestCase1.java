package com.zhidian.base.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian.dsp.solr.SolrServerFactory;
import com.zhidian.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian.common.util.PropertiesUtil;

public class CopyOfSolrTestCase1 {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private HttpSolrServer cxHttpSolrServer;
	@Before
	public void init() { 
		cxHttpSolrServer = SolrServerFactory.getHttpSolrServer(PropertiesUtil.getInstance().getValue("solr.httpsolrserver.url"));
	}
	
	@Test
	public void testAdd() {
		try {
			cxHttpSolrServer.deleteByQuery("*:*");
			
			SolrInputDocument solrInputDocument1 = new SolrInputDocument();
			solrInputDocument1.addField("id", "10");
			solrInputDocument1.addField("type_s", "parent");
			solrInputDocument1.addField("BRAND_s", "Nike");
			
			SolrInputDocument child11 = new SolrInputDocument();
			child11.addField("id", "11");
			child11.addField("COLOR_s", "Red");
			child11.addField("SIZE_s", "XL");
			
			SolrInputDocument child12 = new SolrInputDocument();
			child12.addField("id", "12");
			child12.addField("COLOR_s", "Blue");
			child12.addField("SIZE_s", "XL");
			List<SolrInputDocument> listSolrInputDocument1 = new ArrayList<SolrInputDocument>();
			listSolrInputDocument1.add(child11);
			listSolrInputDocument1.add(child12);
			solrInputDocument1.addChildDocuments(listSolrInputDocument1);
			
			
			SolrInputDocument solrInputDocument2 = new SolrInputDocument();
			solrInputDocument2.addField("id", "20");
			solrInputDocument2.addField("type_s", "parent");
			solrInputDocument2.addField("BRAND_s", "Nike");
			
			SolrInputDocument child21 = new SolrInputDocument();
			child21.addField("id", "21");
			child21.addField("COLOR_s", "Red");
			child21.addField("SIZE_s", "M");
			
			SolrInputDocument child22 = new SolrInputDocument();
			child22.addField("id", "22");
			child22.addField("COLOR_s", "Blue");
			child22.addField("SIZE_s", "XL");
			List<SolrInputDocument> listSolrInputDocument2 = new ArrayList<SolrInputDocument>();
			listSolrInputDocument2.add(child21);
			listSolrInputDocument2.add(child22);
			solrInputDocument2.addChildDocuments(listSolrInputDocument2);
			
			
		    
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			solrInputDocument.addField("id", "30");
			solrInputDocument.addField("type_s", "parent");
			solrInputDocument.addField("BRAND_s", "Puma");
			
			SolrInputDocument child01 = new SolrInputDocument();
			child01.addField("id", "31");
			child01.addField("COLOR_s", "Red");
			child01.addField("SIZE_s", "XL");
			
			SolrInputDocument child02 = new SolrInputDocument();
			child02.addField("id", "32");
			child02.addField("COLOR_s", "Blue");
			child02.addField("SIZE_s", "M");
			List<SolrInputDocument> listSolrInputDocument = new ArrayList<SolrInputDocument>();
			listSolrInputDocument.add(child01);
			listSolrInputDocument.add(child02);
			solrInputDocument.addChildDocuments(listSolrInputDocument);
			
			List<SolrInputDocument> listSolrInputDocumentOK = new ArrayList<SolrInputDocument>();
			listSolrInputDocumentOK.add(solrInputDocument);
			listSolrInputDocumentOK.add(solrInputDocument1);
			listSolrInputDocumentOK.add(solrInputDocument2);
			
			cxHttpSolrServer.add(listSolrInputDocumentOK);
			cxHttpSolrServer.commit();
		} catch (SolrServerException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testCZSearchAdDocument() {
		SolrQuery query = new SolrQuery();// 查询全部
//		query.setQuery("+COLOR_s:Red +SIZE_s:XL");
//		query.setQuery("{!parent which='type_s:parent'}+COLOR_s:Red +SIZE_s:XL");
//		query.setQuery("+BRAND_s:Nike +_query_:"{!parent which=type_s:parent}+COLOR_s:Red +SIZE_s:XL");
//		query.setQuery("{!child of=type_s:parent}BRAND_s:Puma");
//		expand=true&expand.q=*:*&expand.field=_root_
		ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("q", "{!child of=type_s:parent}BRAND_s:Puma");
        params.add("expand", "true");
        params.add("expand.field", "_root_");
        params.add("expand.q", "*:*");
	        // attention! the default is 5 rows, so be carefull about that! - bty
	        // Integer.MAX_VALUE is not allowed as it is about +300
        params.add("expand.rows", "100");
        params.add("expand.fq", "*:*");
		query.add(params);
//		query.setQuery("{!parent which=type_s:parent}+COLOR_s:Red +SIZE_s:XL");
		try {
			QueryResponse response = cxHttpSolrServer.query(query);
			 SolrDocumentList  solrDocumentList = response.getResults();
			 System.out.println(solrDocumentList.size());
			 for(SolrDocument solrDocument : solrDocumentList) {
				 Map<String, Collection<Object>> values = solrDocument.getFieldValuesMap();
				 System.out.println(values);
			 }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * DocumentBean添加文檔
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void searchAdDocument1() {
		StringBuffer contentCondition = new StringBuffer("adBanner");
		String string = transformSolrMetacharactor(contentCondition.toString());
		System.out.println(AdDocumentManager.getInstance().searchAdDocumentId(string));
	}
	
	
	private String transformSolrMetacharactor(String input){
        StringBuffer sb = new StringBuffer();
        String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            matcher.appendReplacement(sb, "\\\\"+matcher.group());
        }
        matcher.appendTail(sb);
        return sb.toString(); 
    }
	
	/**
	 * 创建广告索引
	 * @param ad
	 * @return
	 */
	
	@Test
	public void testSolrManager() throws SolrServerException, IOException {
		AdDocumentManager adDocumentManager = AdDocumentManager.getInstance(); 
		adDocumentManager.deleteDocumentById("2");
	}
}
