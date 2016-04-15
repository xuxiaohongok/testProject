package com.zhidian3g.dsp.solr.service;

import com.zhidian3g.dsp.vo.solr.SearchAd;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;

public interface SolrSearchAdService {
	public SearchAd searchAdFormSolr(SearchAdCondition ad);
}
