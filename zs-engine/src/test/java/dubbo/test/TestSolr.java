package dubbo.test;

import org.junit.Test;

import com.zhidian3g.dsp.solr.documentmanager.AdDocumentManager;

public class TestSolr {
	
	@Test
	public void testOK() {
		AdDocumentManager solrServer = AdDocumentManager.getInstance();
		System.out.println(solrServer.searchAdDocumentId(""));
	}
}