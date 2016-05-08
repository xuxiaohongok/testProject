package com.zhidian3g.dsp.solr.service;

import java.util.Map;

import com.zhidian3g.dsp.vo.solr.SearchAdCondition;
import com.zhidian3g.dsp.vo.solr.SearchAdMateriolCondition;

public interface SolrSearchAdService {
	public Map<String, Long> searchAdFormSolr(SearchAdCondition ad, SearchAdMateriolCondition searchAdMateriolCondition);
}
