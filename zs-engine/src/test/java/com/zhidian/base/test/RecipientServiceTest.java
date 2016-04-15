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
    	SearchAdCondition searchAd = new SearchAdCondition("ok", DspConstant.ADX_TYPE + 2, DspConstant.AD_SHOW_TYPE + 2, DspConstant.AD_TYPE+1,DspConstant.OS_ANDROID, "127.0.0.1");
    	for(;;) {
    		solrSearchAdServiceImpl.searchAdFormSolr(searchAd);
    	}
    }
}