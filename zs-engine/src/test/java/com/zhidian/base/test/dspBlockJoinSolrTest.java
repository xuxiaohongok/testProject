package com.zhidian.base.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;
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

import at.bro.code.solr.utils.SolrUtils;

import com.zhidian.base.CareateReource;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian3g.common.util.PropertiesUtil;
import com.zhidian3g.dsp.solr.SolrServerFactory;
import com.zhidian3g.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian3g.dsp.vo.adcontrol.Ad;

public class dspBlockJoinSolrTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private HttpSolrServer cxHttpSolrServer;
	@Before
	public void init() { 
		cxHttpSolrServer = SolrServerFactory.getHttpSolrServer(PropertiesUtil.getInstance().getValue("solr.httpsolrserver.url"));
		try {
//			cxHttpSolrServer.deleteByQuery("*:*");
//			cxHttpSolrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private List<CareateReource> getChildList(long adId) {
		List<CareateReource> list = new ArrayList<CareateReource>();
		Integer id = RandomUtils.nextInt(1, 5);
		for(int i=0; i<id; i++) {
			String title = "testasdgasdgsadgasdgasdg";
			title = title.substring(RandomUtils.nextInt(0, 3), RandomUtils.nextInt(5, title.length()));
			String[] imageHWSArray = {"360*360", "320*320", "600*600"};
			list.add(new CareateReource(adId + "-createId"+i, id, i, RandomUtils.nextInt(1, 5), imageHWSArray[RandomUtils.nextInt(0, 3)], title, null));
		}
		return list;
	}

	@Test
	public void testAdd() {
		 try {
			 saveDocument(1);
			 saveDocument(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDel() {
		 try {
			 cxHttpSolrServer.deleteByQuery("parentId:2");
			 cxHttpSolrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch() {
		 try {
//			List<SolrDocument> foundDocuments  = querySolr("meterialId:3+imageHWs:320*320", "adxType AND os-androoid AND adCategory", "meterialId:3+imageHWs:320*320");
			String childCondition = "meterialId:3 +imageHWs:320*320";
			String ok = SolrUtils.prepareParentSelector("docType:parent", childCondition);
			String parentCondition = "adxType AND os-androoid AND adCategory2 AND 江苏";
			
//			final ModifiableSolrParams params = new ModifiableSolrParams();
//	        params.add("q", ok);
//	        params.add("fq", parentCondition);
//	        params.add("expand", "true");
//	        params.add("expand.field", "_root_");
//	        params.add("expand.q", "*:*");
//	        
//	        // attention! the default is 5 rows, so be carefull about that! - bty
//	        params.add("expand.rows", "100");
//	        params.add("expand.fq", "meterialType:3");
	        
	        SolrQuery query = new SolrQuery();// 查询全部
			query.setRows(DspConstant.DEFAULT_ROW);
			query.setParam("expand", "true");
			query.setParam("expand.field", "_root_");
	        query.setParam("expand.q", "*:*");
	        query.setParam("expand.rows", "100");
	        query.setParam("expand.fq", childCondition);
//	        query.setQuery(ok);
//	        query.addFilterQuery(parentCondition);
	        
			query.setQuery(parentCondition);
			query.addFilterQuery(ok);
			QueryResponse queryResponse1 = cxHttpSolrServer.query(query);
			long startTime = System.currentTimeMillis();
			QueryResponse queryResponse = cxHttpSolrServer.query(query);
//	        for(int i=0; i<100;i++) {
//	        	QueryResponse queryResponse = cxHttpSolrServer.query(query);
//	        	List<SolrDocument> foundDocuments1 = expandResults(queryResponse, "id");
//	        	
//	        	for(SolrDocument solrDocument : foundDocuments1) {
//	        		List<SolrDocument> childDocumentList = solrDocument.getChildDocuments();
//	        		System.out.println("parentDocument:" + solrDocument);
//	        		for(SolrDocument childDocument : childDocumentList) {
//	        			System.out.println("childDocument:" + childDocument);
//	        		}
//	        		System.out.println("=================================");
//	        	}
//	        }
	        long endTime =  System.currentTimeMillis();
	        System.out.println("ms:" + (endTime - startTime) + queryResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static List<SolrDocument> expandResults(QueryResponse queryResponse, String idField) {
        final SolrDocumentList results = queryResponse.getResults();
        final List<SolrDocument> documents = new ArrayList<SolrDocument>();
        final Map<String, SolrDocumentList> expandedResults = queryResponse.getExpandedResults();
        for (int i = 0; i < results.getNumFound(); i++) {
            final SolrDocument solrDocument = results.get(i);
            final SolrDocumentList children = expandedResults.get(solrDocument.get(idField));
            if (children != null) {
                solrDocument.addChildDocuments(children);
            }

            // TODO possibly add grandchildren?

            documents.add(solrDocument);
        }
        return documents;
    }
	
	 private void saveDocument(Integer id) {
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
		 
			String parentId = "id" + ad.getId();
	        final SolrInputDocument document = new SolrInputDocument();
	        document.addField("id", parentId);
	        document.addField("docType", "parent");
	        document.addField("adxType", DspConstant.ADX_TYPE + ad.getAdxType());
	        document.addField("adType", DspConstant.AD_TYPE + ad.getAdxType());
	        document.addField("adCategory", DspConstant.AD_CATEGORY + ad.getAdCategory());
	        document.addField("adPlatform", ad.getAdPlatform());
	        document.addField("timeZones", ad.getTimeZones());
	        document.addField("areas", ad.getAreas());
	        List<CareateReource> children = getChildList(ad.getId());
	        int i=1;
	        for (final CareateReource child : children) {
	            final SolrInputDocument childDocument = new SolrInputDocument();
	            childDocument.addField("id", id + "-createId-" + child.getCreateId() + "-" + i++);
	            childDocument.addField("parentId", parentId);
	            childDocument.addField("createId", child.getCreateId());
	            childDocument.addField("docType", "child");
	            
	            childDocument.addField("meterialId", child.getMeterialId());
	            childDocument.addField("meterialType", child.getMeterialType());
	            childDocument.addField("imageHWs", child.getImageHWs());
	            childDocument.addField("tLen", child.getTitle().length());
	            document.addChildDocument(childDocument);
	        }
	        
	        try {
				cxHttpSolrServer.add(document);
				cxHttpSolrServer.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
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
	
	 private List<SolrDocument> querySolr(String parentChildRestriction, String parentFilterRestriction,
	            String childRestriction) throws SolrServerException {
	        final ModifiableSolrParams params = baseParams(parentChildRestriction, parentFilterRestriction,
	                childRestriction);
	        return SolrUtils.expandResults(cxHttpSolrServer.query(params), "id");
	   }
	 
	//("+relation_s:daughter", "id:m*", "relation_s:d*")
	    private static ModifiableSolrParams baseParams(String parentChildRestriction, String parentFilterRestriction,
	            String childRestriction) {
	        return SolrUtils.baseBlockJoinParams(SolrUtils.prepareParentSelector("docType:parent", parentChildRestriction),
	                parentFilterRestriction, childRestriction);
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
