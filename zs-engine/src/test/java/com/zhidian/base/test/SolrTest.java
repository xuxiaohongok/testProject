package com.zhidian.base.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.solr.SolrDocumentContainer;
import com.zhidian.dsp.solr.document.DspAdDocument;
import com.zhidian.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian.dsp.vo.adcontrol.Ad;

public class SolrTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private HttpSolrServer httpSolrServer;
	private AdDocumentManager adDocumentManager;
	@Before
	public void init() { 
		adDocumentManager = AdDocumentManager.getInstance();
		httpSolrServer = (HttpSolrServer) adDocumentManager.getSolrServer();
	}
	
	@Test
	public void testSolrTest() {
		try {
			adDocumentManager.deleteAllDocument();
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			solrInputDocument.addField("id", "id10");
			solrInputDocument.addField("adId", 10);
			solrInputDocument.addField("adxType", "name10");
			
			SolrInputDocument child = new SolrInputDocument();
			child.addField("id", "12");
			child.addField("adId", 10);
			child.addField("adxType", "name10");
			child.addField("parentId", "id10");
			solrInputDocument.addChildDocument(child);
			httpSolrServer.add(solrInputDocument);
			httpSolrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testAdd() {
		adDocumentManager.deleteAllDocument();
		Integer id = RandomUtils.nextInt(1, 3);
		Ad ad = new Ad();
		ad.setId(id.longValue());
		ad.setAdCategory(id);
		ad.setShowType(id);
		ad.setAdxType(id);
		ad.setAdPlatform("os-androoid, phone");
		ad.setAdName("广告");
		ad.setAreas(getCity().get(id));
		ad.setImageURL("www.baidu.com");
		ad.setShowType(id);
		ad.setTimeZones("12:00, 18:00");
		ad.setTitle("广告测试" + id);
		ad.setWidth(300);
		ad.setHeight(600);
		ad.setAdType(1);
		
		DspAdDocument dspAdDocument = SolrDocumentContainer.createDspAdDocument(ad);
		adDocumentManager.createDocumentIndex(dspAdDocument);
		System.out.println("====end======");
	}
	
	@Test
	public void testGetAddAllDocument() {
		System.out.println("======" + adDocumentManager.getAllDocument().get(0).getAdCategory());
	}
	
	@Test
	public void testCZSearchAdDocument() {
		long startTime = System.currentTimeMillis();
		StringBuffer contentCondition = new StringBuffer("text:(adScreen AND 浙江省)");
		SolrQuery query = new SolrQuery();// 查询全部
		query.setFields("adId,showType,CMCCAreas");
		query.setRows(DspConstant.DEFAULT_ROW);
		StringBuffer fqSB = new StringBuffer(); 
//		fqSB.append("-adId:").append("(25555 || 25556) AND -text:中國移動123"); //
//		fqSB.append("-CMCCAreas:(广东省 || 湖北省 || 浙江省 || 云南省) AND -adId:(1 || 19)");
		StringBuffer fqAdId = new StringBuffer(); 
		fqAdId.append("-adId:(1 || 19)");
		String fq = fqSB.toString(); 
		System.out.println("fq:" + fq);
		query.addFilterQuery(fq);  //过滤 
		query.setQuery(contentCondition.toString());
		try {
			QueryResponse response = httpSolrServer.query(query);
			
//			List<CXResponseSearchMessage> list = response.getBeans(CXResponseSearchMessage.class);
//			System.out.println(list);
			SolrDocumentList docs = response.getResults();
//			System.out.println("文档个数：" + docs.getNumFound());
//			for(CXResponseSearchMessage doc : list ) {
//				System.out.println(doc);
////				System.out.println(doc.getFieldValue("id"));
//			}
			for(SolrDocument doc : docs) {
				System.out.println(doc);
//				System.out.println(doc.getFieldValue("id"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private Map<Integer, String> getCity() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "山西省,江苏省");
		map.put(2, "江苏省,浙江省");
		map.put(3, "浙江");
		map.put(4, "江西省");
		map.put(5, "福建省");
		map.put(6, "湖北省");
		map.put(7, "湖南省,广东省");
		map.put(8, "广东");
		map.put(9, "海南省");
		map.put(10, "广西壮族自治区");
		map.put(11, "四川,广东省");
		map.put(12, "云南");
		map.put(13, "贵州");
		map.put(14, "西藏自治区");
		map.put(15, "陕西");
		map.put(16, "甘肃");
		map.put(17, "青海");
		return map;
	}
	
	
	@Test
	public void testDelAll() {
		try {
			httpSolrServer.deleteByQuery("*:*");
			httpSolrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSolrManager() throws SolrServerException, IOException {
		AdDocumentManager adDocumentManager = AdDocumentManager.getInstance(); 
		adDocumentManager.deleteDocumentById("2");
	}
}

