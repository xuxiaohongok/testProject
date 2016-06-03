package com.zhidian.dsp.solr.service;

import com.zhidian.dsp.vo.solr.SearchAd;
import com.zhidian.dsp.vo.solr.SearchAdCondition;
import com.zhidian.dsp.vo.solr.SearchAdMateriolCondition;

public interface SolrSearchAdService {
	public SearchAd searchAdFormSolr(SearchAdCondition ad, SearchAdMateriolCondition searchAdMateriolCondition);
}
