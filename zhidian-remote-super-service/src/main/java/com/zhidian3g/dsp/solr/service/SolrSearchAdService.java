package com.zhidian3g.dsp.solr.service;

import java.util.Map;

import com.zhidian3g.dsp.vo.solr.SearchAdCondition;

public interface SolrSearchAdService {
	public Map<String, Object> searchAdFormSolr(SearchAdCondition ad);
}
