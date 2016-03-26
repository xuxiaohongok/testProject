package com.zhidian3g.dsp.solr.service.impl;

import org.springframework.stereotype.Service;

import com.zhidian3g.dsp.solr.document.SolrAd;
import com.zhidian3g.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian3g.dsp.solr.service.SolrSearchAdService;
import com.zhidian3g.dsp.vo.solr.SearchAd;

@Service
public class SolrSearchAdServiceImpl implements SolrSearchAdService {
	
	private AdDocumentManager adDocumentManager = AdDocumentManager.getInstance();
	
	@Override
	public SolrAd searchAdFormSolr(SearchAd ad) {
		String condition = "";
		adDocumentManager.searchAdDocumentId(condition);
		return null;
	}

}
