package com.zhidian.base.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class VerificationTest {

	@Test
	public void test() throws IOException, SolrServerException {
		ConcurrentUpdateSolrServer server = new ConcurrentUpdateSolrServer(
				"http://localhost:8080/solr/testBlockIndexDoument", 4, 2);
		server.setParser(new XMLResponseParser());
		server.deleteByQuery("*:*");

		List<SolrInputDocument> list = new ArrayList<SolrInputDocument>();

		list.add(createChild("10", "1", "first", "parent_1"));
		list.add(createChild("11", "1", "second", "parent_1"));
		list.add(createChild("12", "1", "second", "parent_1"));
		list.add(createChild("13", "1", "third", "parent_1"));
		list.add(createChild("14", "1", "third", "parent_1"));
		list.add(createChild("15", "1", "third", "parent_1"));

		SolrInputDocument parent1 = new SolrInputDocument();
		parent1.addField("type", "parent");
		parent1.addField("data_source", "parent_1");
		parent1.addField("id", 1);
		parent1.addChildDocuments(list);

		SolrInputDocument parent2 = new SolrInputDocument();
		parent2.addField("type", "parent");
		parent2.addField("data_source", "parent_2");
		parent2.addField("id", 2);
		List<SolrInputDocument> list2 = new ArrayList<SolrInputDocument>();

		list2.add(createChild("20", "2", "first", "parent_2"));
		list2.add(createChild("21", "2", "first", "parent_2"));
		list2.add(createChild("22", "2", "second", "parent_2"));

		parent2.addChildDocuments(list2);

		server.add(Arrays.asList(parent1, parent2), 0);

		server.commit();

		checkQueryEquals(server, "third", "parent_1", "parent_2");

		checkQueryEquals(server, "third", "parent_1");

	}

	private void checkQueryEquals(ConcurrentUpdateSolrServer server,
			String request, String... expected) throws SolrServerException {
		checkQuery(server, "q={!parent which=type:parent}search_field:" + request, null, expected);
		checkQuery(server, "search_field:" + request,
				"{!collapse field=parentId}", expected);
		checkQuery(server, "q={!join from=parentId to=id}search_field:"
				+ request, null, expected);
	}

	private void checkQuery(ConcurrentUpdateSolrServer server, String q,
			String fq, String... expected) throws SolrServerException {
		SolrQuery query = new SolrQuery();
		query.setQuery(q);
		if (fq != null) {
			query.setFilterQueries(fq);
		}
		QueryResponse response = server.query(query);
		System.out.println("getResults=" + response.getResults());
		checkResult(response.getResults(), expected);
	}

	private void checkResult(SolrDocumentList solrDocumentList,
			String... expected) {
//		Assert.assertEquals(expected.length, solrDocumentList.size());
		checkResult(Iterables.transform(solrDocumentList,
				new Function<SolrDocument, String>() {
					public String apply(SolrDocument input) {
						return (String) input.getFieldValue("data_source");
					}
				}), expected);
	}

	public static void checkResult(Iterable<String> actualIds,
			String... expectedIds) {
//		Assert.assertEquals("Actual values " + Iterables.toString(actualIds),
//				expectedIds.length, Iterables.size(actualIds));
//		for (String expected : expectedIds) {
//			Assert.assertTrue("Expected " + expected + ", but not found in "
//					+ Iterables.toString(actualIds),
//					Iterables.contains(actualIds, expected));
//		}
		System.out.println(Arrays.asList(expectedIds) + "===" + actualIds);
		for (String actualId : actualIds) {
//			Assert.assertTrue("Expected " + actualId + ", but not found in "
//					+ Arrays.toString(expectedIds),
//					Iterables.contains(Arrays.asList(expectedIds), actualId));
		}
	}

	private SolrInputDocument createChild(String id, String parentId,
			String text, String parentData) {
		SolrInputDocument child = new SolrInputDocument();
		child.addField("id", id);
		child.addField("parentId", parentId);
		child.addField("search_field", text);
		child.addField("data_source", parentData);
		return child;
	}

}
