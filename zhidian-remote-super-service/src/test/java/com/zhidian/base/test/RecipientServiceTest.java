package com.zhidian.base.test;

import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian3g.dsp.solr.service.impl.SolrSearchAdServiceImpl;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;

@RunWith(JMockit.class)
public class RecipientServiceTest {

    @Tested
    private SolrSearchAdServiceImpl solrSearchAdServiceImpl;

//    @Injectable
//    private RecipientRepository recipientRepository;

    @Test
    public void should_return_success_when_add_recipient_not_exist() throws Exception {
    	Integer type = 1;
    	SearchAdCondition searchAd = new SearchAdCondition("userId1", DspConstant.ADX_TYPE + type, DspConstant.AD_SHOW_TYPE + type, "127.0.0.1");
    	searchAd.setLength(4);
    	solrSearchAdServiceImpl.searchAdFormSolr(searchAd);
    }
}