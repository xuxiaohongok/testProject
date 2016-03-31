package com.zhidian3g.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/spring.xml"})
public class WebServiceTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void testDeleteAd() {
//		System.out.println(adMatchingDMIService.deleteAdByAdId(1l));
		List<Long> listLong = new ArrayList<Long>();
		listLong.add(1l);
		listLong.add(2l);
	}
	
}
