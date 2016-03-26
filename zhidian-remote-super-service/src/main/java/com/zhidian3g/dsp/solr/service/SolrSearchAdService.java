package com.zhidian3g.dsp.solr.service;

import com.zhidian3g.dsp.solr.document.SolrAd;
import com.zhidian3g.dsp.vo.solr.SearchAd;

public interface SolrSearchAdService {
	public SolrAd searchAdFormSolr(SearchAd ad);
}
