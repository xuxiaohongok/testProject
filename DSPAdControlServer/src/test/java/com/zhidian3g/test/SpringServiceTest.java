//package com.zhidian3g.test;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.zhidian3g.nextad.dao.AdMapper;
//import com.zhidian3g.nextad.domain.Account;
//import com.zhidian3g.nextad.domain.Ad;
//import com.zhidian3g.nextad.domain.AdImage;
//import com.zhidian3g.nextad.domain.AdPushMessage;
//import com.zhidian3g.nextad.domain.AdTask;
//import com.zhidian3g.nextad.service.AdService;
//import com.zhidian3g.nextad.service.AppService;
//import com.zhidian3g.nextad.util.DateUtil;
//import com.zhidian3g.nextad.webservice.adcontrol.AdControlHandlerService;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/spring/spring.xml"})
//public class SpringServiceTest extends AbstractJUnit4SpringContextTests {
//	
//	@Resource
//	private AdService adService;
//	
//	@Resource
//	private AppService appService;
//	
//	@Resource
//	private AdControlHandlerService adControlHandlerService;
//	
//	
//	@Resource
//	private AdMapper adMapper;
//	
//	
//	@Test
//	public void testOk() {
//		List<AdPushMessage> adPushMessageList = adService.getPushAdList(null);
//		for(AdPushMessage adPushMessage : adPushMessageList) {
//			
//		}
//	}
//	
//	@Test
//	public void testAdBanner() {
//		System.out.println(adService.getAdBannerByAdId(100l));
//	}
//	
//	@Test
//	public void testAdCreen() {
//		System.out.println(adService.getAdScreenByAdId(157l));
//	}
//	
//	
//	@Test
//	public void testGetEveryDay() {
//		adControlHandlerService.everyDayRefreshAdIndex();
//	}
//	
//	@Test
//	public void testGetDevemer() {
//		System.out.println(adService.getAdDeveloperIncome(146l));
//	}
//	
//	@Test
//	public void testGetPushList() {
//		List<Long> adList = new ArrayList<Long>();
//		adList.add(136l);
//		adList.add(96l);
//		System.out.println(adMapper.getEveryDayPushAdList(adList));
//	}
//	
//	@Test
//	public void testGetAdHistoryAdTask() {
//		adService.getHistoryAdTasks(102l);
//	}
//	
//	@Test
//	public void testAdWallStopDay() {
//		System.out.println(adService.isStopAdWall(107l));
//	}
//	
//	@Test
//	public void testAdListByList() {
//		System.out.println(adService.getAdList(2));
//	}
//	
//	@Test
//	public void testPackageNameByAdId() {
//		System.out.println(adService.getPackageNameById(99l));
//	}
//	
//	@Test
//	public void testAdControlServerServiceDel() {
//		adControlHandlerService.deleteAdByAdId(1000040l);
//	}
//	
//	@Test
//	public void testGetAdAppList() {
//		List<Long> adIdsList = new ArrayList<Long>();
//		adIdsList.add(136L);
//		adIdsList.add(96L);
//		System.out.println(adService.getAdAppList(adIdsList));
//	}
//	
//	@Test
//	public void testAdImageByAdId() {
//		List<AdImage>  adImages = adService.getAdImages(895l, 9);
//		System.out.println(adImages.size());
//	}
//	
//	@Test
//	public void testGetAdWallByAdId() {
//		System.out.println(adService.getAdWallById(113l));
//	}
//	
//	@Test
//	public void testAddPushByAdId() {
//		System.out.println(adService.getAdPushById(136l));
//	}
//	
//	@Test
//	public void testGetWallTask() {
//		List<AdTask> adTaskList = adService.getAdTasks(3l);
//		for(AdTask adTask : adTaskList) {
//			System.out.println(adTask.getNo() + ";" +adTask.getTaskName() + ";" + adTask.getDeveloperCost()+ ";"
//								+ adTask.getMonitorRange()+ ";" + adTask.getTaskStartTime());
//		}
//	}
//	
//	@Test
//	public void testGetAdImageList() {
//		List<AdImage> list =adService.getAdImages(136l, 4);
//		for(AdImage adImage : list ) {
//			System.out.println(adImage.getImageUrl());
//		}
//		
//	}
//	
//	@Test
//	public void testAdControlServerUpdate() {
//		adService.setAdState(1000041l, 3);
//	}
//	
//	@Test
//	public void testAccount() {
//		Account account = adService.getAccount(1000001l);
//		System.out.println(account);
//	}
//	
//	@Test
//	public void testAd() {
//		List<Ad> listAd = adService.getAdList(2);
//		System.out.println(listAd.size() + "==" + listAd);
//	}
//	
//	@Test
//	public void testAdById1() {
//		Ad ad = adService.getAdById(132l);
//		System.out.println("ad=" + ad);
//	}
//	
//	@Test
//	public void testAdById() {
//		Ad ad = adService.getAdById(1000040l);
//		/**
//		 * 广告开始时间未到，不生成
//		 * 大于开始时间，或都等于
//		 */
//		String nowDateString = DateUtil.getDateTime();
//		Integer adState = 0;
//		
//		Date startDate = ad.getStartDate();
//		String startDateString = DateUtil.get(startDate, "yyyy-MM-dd") + " 00:00:00";
//		if(DateUtil.compare_date(nowDateString, startDateString)) {
//			System.out.println("================1");
//			adState = 2;
//		}
//	
//		/**
//		 * 广告结束日时间已超过，不生成
//		 */
//		Date endDate = ad.getEndDate();
//		if(endDate!=null){
//			String endDateString = DateUtil.get(endDate, "yyyy-MM-dd") + " 23:59:59";
//			if(DateUtil.compare_date(endDateString, nowDateString)) {
//				adState = 3;
//			}
//		} 
//		
//		System.out.println("adState2=" + adState);
//	}
//	
//
//	@Test
//	public void testUpdateRAd() {
//		List<Long> adIdList = new ArrayList<Long>();
//		adIdList.add(1000000l);
//		adIdList.add(2l);
//		adService.recoverAdState(adIdList);
//	}
//	
//	@Test
//	public void testGetAllAdList() {
//		System.out.println(adService.getEveryDayAdList());
//	}
//	
//	@Test
//	public void testApp() {
//		System.out.println(appService.getAppDetailMessageByPid("4B479153C99FD170F85EE08815D117E0"));
//	}
//	
//	@Test
//	public void testAdListAdId() {
//		List<Long> listId = new ArrayList<Long>();
//		for(long i=1; i<=10; i++) {
//			listId.add(i);
//		}
//		
//		System.out.println(adService.getAdsByListAdId(listId).size());
//	}
//	
//}
