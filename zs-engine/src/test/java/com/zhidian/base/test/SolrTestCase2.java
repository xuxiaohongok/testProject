package com.zhidian.base.test;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.bro.code.solr.utils.SolrUtils;

import com.zhidian.dsp.solr.SolrServerFactory;
import com.zhidian.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian.common.util.PropertiesUtil;

public class SolrTestCase2 {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private HttpSolrServer cxHttpSolrServer;
	@Before
	public void init() { 
		cxHttpSolrServer = SolrServerFactory.getHttpSolrServer(PropertiesUtil.getInstance().getValue("solr.httpsolrserver.url"));
		try {
			cxHttpSolrServer.deleteByQuery("*:*");
			saveDocument("dad", "son", "daughter", "dandy");
			saveDocument("mum", "daughter", "girly");
			saveDocument("mum1", "daughter", "girly1");
			saveDocument("mother", "girl");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAdd() {
		 try {
			final List<SolrDocument> foundDocuments = querySolr("+relation_s:daughter", "id:m*", "relation_s:daughter");
			for(SolrDocument solrDocument : foundDocuments) {
				List<SolrDocument> childDocuments = solrDocument.getChildDocuments();
				System.out.println(solrDocument + "===" + childDocuments);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	 private void saveDocument(String id, String... children) {
	        final SolrInputDocument document = new SolrInputDocument();
	        document.addField("id", id);
	        document.addField("name", id);
	        document.addField("type_s", "parent");
	        for (final String child : children) {
	            final SolrInputDocument childDocument = new SolrInputDocument();
	            childDocument.addField("id", child);
	            childDocument.addField("parentId", id);
	            childDocument.addField("relation_s", child);
	            childDocument.addField("type_s", "child");
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
	
	 private List<SolrDocument> querySolr(String parentChildRestriction, String parentFilterRestriction,
	            String childRestriction) throws SolrServerException {
	        final ModifiableSolrParams params = baseParams(parentChildRestriction, parentFilterRestriction,
	                childRestriction);
	        return SolrUtils.expandResults(cxHttpSolrServer.query(params), "id");
	   }
	 
	    private static ModifiableSolrParams baseParams(String parentChildRestriction, String parentFilterRestriction,
	            String childRestriction) {
	        return SolrUtils.baseBlockJoinParams(SolrUtils.prepareParentSelector("type_s:parent", parentChildRestriction),
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
