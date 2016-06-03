package com.zhidian3g.dsp.solr.service;

import com.zhidian3g.dsp.vo.solr.SearchAd;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;
import com.zhidian3g.dsp.vo.solr.SearchAdMateriolCondition;

public interface SolrSearchAdService {
	public SearchAd searchAdFormSolr(SearchAdCondition ad, SearchAdMateriolCondition searchAdMateriolCondition);
}
