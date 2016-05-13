package com.zhidian3g.nextad.test;


import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhidian.ad.domain.Ad;
import com.zhidian.ad.service.AdService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context.xml"})
public class SpringServiceTestCase extends AbstractJUnit4SpringContextTests {
	@Resource
	private AdService adService;
	
	@Test
	public void testDataBase() {
		List<Ad> adList = adService.getAdById();
		System.out.println(adList);
	}
	
}
