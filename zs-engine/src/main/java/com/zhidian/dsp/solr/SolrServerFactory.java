package com.zhidian.dsp.solr;

import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrServerFactory {

	/**
	 * 
	 * 获取单机的Solr服务器
	 * @return
	 */
	public static HttpSolrServer getHttpSolrServer(String solrURL) {
		HttpSolrServer httpSolrServer = null;
		try {
			httpSolrServer = new HttpSolrServer(solrURL);
			httpSolrServer.setMaxRetries(3); // defaults to 0. > 1 not recommended.错误重试次数
			httpSolrServer.setConnectionTimeout(60000);
			httpSolrServer.setParser(new BinaryResponseParser());
			httpSolrServer.setSoTimeout(60000); // socket read timeout
			httpSolrServer.setDefaultMaxConnectionsPerHost(100);
			httpSolrServer.setMaxTotalConnections(300);
			httpSolrServer.setFollowRedirects(false); // defaults to false
			httpSolrServer.setAllowCompression(true);
			httpSolrServer.setRequestWriter(new BinaryRequestWriter());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpSolrServer;
	}
	
}
