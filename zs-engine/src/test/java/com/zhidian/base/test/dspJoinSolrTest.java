package com.zhidian.base.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian3g.common.util.PropertiesUtil;
import com.zhidian3g.dsp.solr.SolrServerFactory;
import com.zhidian3g.dsp.solr.document.AdBaseMessageDocument;
import com.zhidian3g.dsp.solr.document.AdMaterialResponseDocument;
import com.zhidian3g.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian3g.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian3g.dsp.vo.adcontrol.AdBaseMessage;
import com.zhidian3g.dsp.vo.adcontrol.AdMaterialMessage;

public class dspJoinSolrTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private HttpSolrServer cxHttpSolrServer;

	@Before
	public void init() {
		cxHttpSolrServer = SolrServerFactory.getHttpSolrServer(PropertiesUtil
				.getInstance().getValue("solr.httpsolrserverjoin.url"));
		 try {
//		 cxHttpSolrServer.deleteByQuery("*:*");
//		 cxHttpSolrServer.commit();
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
	}

	private List<AdMaterialMessage> getChildList(long adId) {
		List<AdMaterialMessage> list = new ArrayList<AdMaterialMessage>();
		Integer createId = RandomUtils.nextInt(1, 5);
		for (int meterialId = 0; meterialId < createId; meterialId++) {
			String title = "testasdgasdgsadgasdgasdg";
			title = title.substring(RandomUtils.nextInt(0, 3),
					RandomUtils.nextInt(5, title.length()));
			String[] imageHWSArray = { "360-360", "320-320", "600-600" };

			list.add(new AdMaterialMessage(createId, meterialId, RandomUtils
					.nextInt(3, 4), imageHWSArray[RandomUtils.nextInt(0, 3)] + " " + imageHWSArray[RandomUtils.nextInt(0, 3)],
					title.length(), null));
		}
		return list;
	}

	@Test
	public void testAdd() {
		try {
			saveDocument(1);
			saveDocument(2);
			saveDocument(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDel() {
		try {
			cxHttpSolrServer.deleteByQuery("*:*");
			cxHttpSolrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSearch() {
		try {
//			String childCondition = "meterialType:4 +imageHWs:360*360 +tLen:12";
			String childCondition = "meterialType:meterialType3";
			String parentCondition = "adxType AND os-androoid AND adCategory2 AND 江苏";
			testSolrAd(parentCondition, childCondition);
			long startTime = System.currentTimeMillis();
			testSolrAd(parentCondition, childCondition);
			long endTime = System.currentTimeMillis();
			System.out.println("ms:" + (endTime - startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSearch1() {
		try {
			String childCondition = "meterialType:meterialType3";
			long end = System.currentTimeMillis();
			String parentCondition = "adxType1 AND os-androoid";
			String adCategory = "1";
			StringBuffer fifterConditionStringBuffer = new StringBuffer();
			if(StringUtils.isNotBlank(adCategory)) {
				String[] adCategoryArray = adCategory.split(",");
				fifterConditionStringBuffer.append("adCategory:(");
				int len = adCategoryArray.length;
				for(int i=0; i<len; i++) {
					if(i == 0) {
						fifterConditionStringBuffer.append(DspConstant.AD_CATEGORY + adCategoryArray[i]); 
					} else {
						fifterConditionStringBuffer.append(" OR " + DspConstant.AD_CATEGORY + adCategoryArray[i]);
					}
				}
				fifterConditionStringBuffer.append(")");
				System.out.println("adCategory==" + fifterConditionStringBuffer);
			}
			List<Long> adIdList = AdDspDocumentManager.getInstance().searchAdDocumentId(parentCondition, fifterConditionStringBuffer.toString());
			System.out.println("adIdList=" + adIdList);
//			Map<Long, SolrDocumentList> map = new HashMap<Long, SolrDocumentList>();
//			List<GroupCommand> adMeterList = AdDspDocumentManager.getInstance().searchAdMeterMessage(childCondition);
//			
//			for (GroupCommand groupCommand : adMeterList) {
//				List<Group> groups = groupCommand.getValues();
//				for (Group group : groups) {
//					SolrDocumentList solrDocumentList = group.getResult();
//					Long adId = (Long) solrDocumentList.get(0).getFieldValue("adId");
//					if(adIdList.contains(adId)) {
//						map.put(adId, solrDocumentList);
//					}
//				}
//			}
//			
//			//优化广告逻辑
//			Set<Long> adIdSet = map.keySet();
//			Long adId = 2l;
//			SolrDocumentList solrDocumentList =  map.get(adId);
//			SolrDocument solrDocument = chooseSolrDocument(solrDocumentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 随机获取广告id集合的一个ID
	 * @param result
	 */
	private SolrDocument chooseSolrDocument(SolrDocumentList solrDocumentList){
		int random = (int)Math.rint(Math.random() * (solrDocumentList.size() - 1));
		SolrDocument solrDocument = solrDocumentList.get(random);
		logger.info("获取的广告创意包广告=" + solrDocument);
		return solrDocument;
	}

	private void test(String childCondition) throws SolrServerException {
		SolrQuery query1 = new SolrQuery();// 查询全部
		query1.setParam(GroupParams.GROUP, true);
		query1.setParam(GroupParams.GROUP_FIELD, "adId");
		query1.setParam(GroupParams.GROUP_LIMIT, "10");
		query1.setFields("adId", "createId", "meterialId", "imageHWs");
		query1.setQuery(childCondition);
		QueryResponse queryResponse1 = cxHttpSolrServer.query(query1);
		GroupResponse groupResponse = queryResponse1.getGroupResponse();
		List<GroupCommand> groupList = groupResponse.getValues();
		for (GroupCommand groupCommand : groupList) {
			List<Group> groups = groupCommand.getValues();
			for (Group group : groups) {
				SolrDocumentList solrDocumentList = group.getResult();
				Long adId = (Long) solrDocumentList.get(0).getFieldValue("adId");
			}
		}
	}
	

	private void testSolrAd(String parentCondition, String childCondition)
			throws Exception {
		long time = System.currentTimeMillis();
		SolrQuery query = new SolrQuery();// 查询全部
		query.setFields("adId");
		query.setQuery(parentCondition);
		QueryResponse queryResponse = cxHttpSolrServer.query(query);
		List<Long> adIdList = new ArrayList<Long>();
		for (SolrDocument solrDocument : queryResponse.getResults()) {
			adIdList.add((Long) solrDocument.getFieldValue("adId"));
		}
		long time2 = System.currentTimeMillis();
		SolrQuery query1 = new SolrQuery();// 查询全部
		query1.setFields("adId", "createId", "meterialId");
		query1.setQuery(childCondition);
		QueryResponse queryResponse1 = cxHttpSolrServer.query(query1);
		List<AdMaterialResponseDocument> listAdCreateResoureDocument = queryResponse1
				.getBeans(AdMaterialResponseDocument.class);
		Map<Long, List<AdMaterialResponseDocument>> adDocumentMap = new HashMap<Long, List<AdMaterialResponseDocument>>();
		// 搜集符合广告的素材
		for (AdMaterialResponseDocument adCreateResoureDocument : listAdCreateResoureDocument) {
			Long adId = adCreateResoureDocument.getAdId();
			if (adIdList.contains(adCreateResoureDocument.getAdId())) {
//				 adDocumentMap.put(adId, value);
			} else {
				System.out.println("不符合条件的广告=" + adCreateResoureDocument);
			}
		}
		long time1 = System.currentTimeMillis();
		System.out.println("time2=" + (time1 - time2));
	}


	private void saveDocument(Integer id) {
		AdBaseMessage adBaseMessage = new AdBaseMessage();
		adBaseMessage.setId(id.longValue());
		adBaseMessage.setAdCategory(id);
		adBaseMessage.setShowType(id);
		adBaseMessage.setAdxType(1);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setOsPlatform("os-androoid, os-phone");
		adBaseMessage.setAreas(getCity().get(id));
		adBaseMessage.setShowType(id);
		adBaseMessage.setTimeZones("12-00, 18-00");
		adBaseMessage.setAdType(1);

		List<AdMaterialMessage> adMaterialMesList = getChildList(adBaseMessage.getId());

		// 广告基本信息文档
		long adId = adBaseMessage.getId();
		AdBaseMessageDocument adBaseMessageDocument = new AdBaseMessageDocument();
		adBaseMessageDocument.setId("adId-" + adId);
		adBaseMessageDocument.setAdId(adBaseMessage.getId());
		adBaseMessageDocument.setTerminalType("terminalType" + adBaseMessage.getTerminalType());
		adBaseMessageDocument.setAdxType(DspConstant.ADX_TYPE + adBaseMessage.getAdxType());
		adBaseMessageDocument.setAdType(DspConstant.AD_TYPE + adBaseMessage.getAdType());
		adBaseMessageDocument.setAdCategory(DspConstant.AD_CATEGORY + adBaseMessage.getAdCategory());
		adBaseMessageDocument.setOsPlatform(adBaseMessage.getOsPlatform());
		adBaseMessageDocument.setTimeZones(adBaseMessage.getTimeZones());
		adBaseMessageDocument.setAreas(adBaseMessage.getAreas());
		// 添加广告基本信息文档
		AdDspDocumentManager.getInstance().createDocumentIndex(adBaseMessageDocument);
//		for (final AdMaterialMessage adMaterialMessage : adMaterialMesList) {
//			final AdMaterialDocument adMaterialDocument = new AdMaterialDocument();
//			adMaterialDocument.setId("adId-" + adId + "-createId-" + adMaterialMessage.getCreateId() + "-meterialId-" + adMaterialMessage.getMeterialId());
//			adMaterialDocument.setCreateId(adMaterialMessage.getCreateId());
//			adMaterialDocument.setAdId(adBaseMessage.getId());
//			adMaterialDocument.setMeterialId(adMaterialMessage.getMeterialId());
//
//			// 创意类型
//			int meterIanType = adMaterialMessage.getMeterialType();
//			adMaterialDocument.setMeterialType("meterialType" + meterIanType
//					+ " meterialType7");
//
//			// 根据创意类型设置对应的索引属性值
//
//			// 1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
//			if (meterIanType == 1) {
//				adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
//			} else if (meterIanType == 2 || meterIanType == 4) {// 图文广告
//				adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
//				adMaterialDocument.settLen(adMaterialMessage.gettLen());
//			} else if (meterIanType == 3) {// 图文描述
//				adMaterialDocument.setImageHWs(adMaterialMessage.getImageHWs());
//				adMaterialDocument.settLen(adMaterialMessage.gettLen());
//				adMaterialDocument.setdLen(adMaterialMessage.getdLen());
//			} else if (meterIanType == 5) {
//				adMaterialDocument.settLen(adMaterialMessage.gettLen());
//			}
//			// 添加广告素材文档
//			AdDspDocumentManager.getInstance().createDocumentIndex(adMaterialDocument);
//		}
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

	private String transformSolrMetacharactor(String input) {
		StringBuffer sb = new StringBuffer();
		String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matcher.appendReplacement(sb, "\\\\" + matcher.group());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 创建广告索引
	 * 
	 * @param ad
	 * @return
	 */

	@Test
	public void testSolrManager() throws SolrServerException, IOException {
		AdDocumentManager adDocumentManager = AdDocumentManager.getInstance();
		adDocumentManager.deleteDocumentById("2");
	}
}
