import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.json.JSON;
import com.zhidian.remote.service.RemoteService;
import com.zhidian.remote.vo.request.AdRequestParam;
import com.zhidian.remote.vo.request.AssetParam;
import com.zhidian.remote.vo.request.DataParam;
import com.zhidian.remote.vo.request.ImageAdTypeParam;
import com.zhidian.remote.vo.request.ImgParam;
import com.zhidian.remote.vo.request.ImpParam;
import com.zhidian.remote.vo.request.MobileAppParam;
import com.zhidian.remote.vo.request.MobileParam;
import com.zhidian.remote.vo.request.NativeAdTypeParam;
import com.zhidian.remote.vo.request.PageParam;
import com.zhidian.remote.vo.request.TitleParam;


public class Test {
	public static void main(String[] args) {
		test2();
//		test3();
	}
	
	public static void test1(){
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			RemoteService hw = (RemoteService)context.getBean("ok");
			String testInfo = "{\"adCondition\":{\"adType\":\"1\"," +
					"\"showType\":6,\"unSupportAdType\":\"2,3\"}," +
					"\"adSlot\":{\"bidMinimum\":10000,\"height\":100,\"width\":640}," +
					"\"bidType\":2," +
					"\"ip\":\"114.244.128.102\"," +
					"\"mediaPlatformId\":2," +
					"\"mobile\":{\"androidId\":\"0693d09b9def7167787033e33baeb99a2016f08f\"," +
								"\"brand\":\"Meizu\",\"carrier\":\"46000\",\"deviceOS\":2," +
								"\"deviceType\":2,\"idfa\":\"424224610848fbe4d6e761a0a4b4dcec23948af6\"," +
								"\"imei\":\"97b091fd8e21a14eeaa75dadb7ca46f0c0ae4eaa\"," +
								"\"mac\":\"424224610848fbe4d6e761a0a4b4dcec23948af6\"," +
								"\"model\":\"m1note\"," +
								"\"networkType\":1," +
								"\"uuId\":\"97b091fd8e21a14eeaa75dadb7ca46f0c0ae4eaa\"}," +
					"\"mobileApp\":{\"appId\":\"5d986240a4b4b5b8949e0c20903e6209\",\"appName\":\"AdViewTest\"," +
									"\"appPackageName\":\"com.kyview.test\"}," +
					"\"serialNumber\":\"20160311-145759_bidreq_201-1125-6Rhb-1\",\"terminalType\":1}";
			
			long statTime = System.currentTimeMillis();
			for(int i=0;i<100;i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String adMessage = hw.getAdMessage(testInfo);
				System.out.println("==========adMessage=" + adMessage);
				Thread.sleep(1000*5);
			}
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - statTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void test2(){
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			RemoteService hw = (RemoteService)context.getBean("ok");
//				int count = 1;
//				Map<String, Object> map1 = new HashMap<String, Object>();
//				map1.put(\"OK\", \"OK\" + count);
//			String testInfo = "{\"adCondition\":{\"adType\":\"1\"," +
//					"\"showType\":7,\"unSupportAdType\":\"2,3\"}," +
//					"\"adSlot\":{\"bidMinimum\":10000,\"height\":100,\"width\":640}," +
//					"\"bidType\":2," +
//					"\"ip\":\"114.244.128.102\"," +
//					"\"mediaPlatformId\":2," +
//					"\"mobile\":{\"androidId\":\"0693d09b9def7167787033e33baeb99a2016f08f\"," +
//								"\"brand\":\"Meizu\",\"carrier\":\"46000\",\"deviceOS\":2," +
//								"\"deviceType\":2,\"idfa\":\"424224610848fbe4d6e761a0a4b4dcec23948af6\"," +
//								"\"imei\":\"97b091fd8e21a14eeaa75dadb7ca46f0c0ae4eaa\"," +
//								"\"mac\":\"424224610848fbe4d6e761a0a4b4dcec23948af6\"," +
//								"\"model\":\"m1note\"," +
//								"\"networkType\":1," +
//								"\"uuId\":\"97b091fd8e21a14eeaa75dadb7ca46f0c0ae4eaa\"}," +
//					"\"mobileApp\":{\"appId\":\"5d986240a4b4b5b8949e0c20903e6209\",\"appName\":\"AdViewTest\"," +
//									"\"appPackageName\":\"com.kyview.test\"}," +
//					"\"serialNumber\":\"20160311-145759_bidreq_201-1125-6Rhb-1\",\"terminalType\":1}";
			
			String testInfo = JSON.json(getAdRequestParam());
			long statTime = System.currentTimeMillis();
			for(int i=0;i<1;i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String adMessage = hw.getAdMessageV1(testInfo);
				System.out.println("==========adMessage=" + adMessage);
				Thread.sleep(1000*2);
			}
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - statTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void test3(){
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			RemoteService hw = (RemoteService)context.getBean("ok");
			
			String testInfo = returnTest();
			long statTime = System.currentTimeMillis();
			for(int i=0;i<1;i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String adMessage = hw.getAdMessageV1(testInfo);
				System.out.println("==========adMessage=" + adMessage);
				Thread.sleep(1000*2);
			}
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - statTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static String returnTest(){
		String test = FileContentTest.readString3();
		return test;
	}
	
	private static AdRequestParam getAdRequestParam() {
        AdRequestParam param = new AdRequestParam();
        param.setIp("127.0.0.1");
        param.setSerialNumber("201603230001");
        param.setUserId("youdao001");
        param.setBidType(2);
        param.setMediaPlatformId(3);
        param.setTerminalType(1);
        param.setFeeType(1);

//        PageParam page = new PageParam();
//        page.setMediaCategory(1);
//        page.setPageType(1);
//        page.setPageCategory(1);
//        param.setPage(page);

//        MobileAppParam mobileApp = new MobileAppParam();
//        mobileApp.setAppId("123456");
//        mobileApp.setAppCategory(1);
//        mobileApp.setAppName("天天酷跑");
//        mobileApp.setAppPackageName("com.zhidian3g");
//        param.setMobileApp(mobileApp);

        MobileParam mobile = new MobileParam();
        mobile.setUuId("ADFA1234");
        mobile.setDeviceOS(1);
        mobile.setDeviceType(1);
        mobile.setAndroidId("androidId");
        mobile.setImei("imei");
        mobile.setImsi("imsi");
        mobile.setMac("mac");
        mobile.setIdfa("idfa");
        mobile.setOrientation(0);
        mobile.setBrand("苹果");
        mobile.setModel("iphone7");
        mobile.setCarrier("移动");
        mobile.setNetworkType(1);
        param.setMobile(mobile);

        List<ImpParam> imps = new ArrayList<ImpParam>();
        
        ImpParam imp = new ImpParam();
        imp.setImpId("impId001");
        imp.setShowType(5);
        imp.setAdType("7");
        imp.setBidMinimum(15000L);
        
//        imp.setUnSupportAdType("2");
//        imp.setAdCategory("1");
        imp.setUnSupportAdCategory("5,6");
        imp.setClickType(1);
        
        
//        imp.setAdPosition(1);
//        imp.setAdMulit(1);

//        List<ImageAdTypeParam> imageAdType = new ArrayList<ImageAdTypeParam>();
//        ImageAdTypeParam imageAdTypeParam = new ImageAdTypeParam();
//        imageAdTypeParam.setHeight(320);
//        imageAdTypeParam.setWidth(50);
//        imageAdType.add(imageAdTypeParam);
//        imp.setImageAdType(imageAdType);

        NativeAdTypeParam nativeAdType = new NativeAdTypeParam();
        
       
        List<AssetParam> assets = new ArrayList<AssetParam>();
        AssetParam asset = new AssetParam();
        asset.setId(1);
        asset.setIsRequired(1);
        TitleParam title = new TitleParam();
        title.setLen(30);
        asset.setTitle(title);
        
        
        AssetParam asset2 = new AssetParam();
        asset2.setId(1);
        asset2.setIsRequired(1);
        ImgParam img2 = new ImgParam();
        img2.setW(160);
        img2.setH(120);
        asset2.setImg(img2);
        
        AssetParam asset3 = new AssetParam();
        asset3.setId(1);
        asset3.setIsRequired(1);
        DataParam data1 = new DataParam();
        data1.setTypeId(3);
        data1.setLen(30);
        data1.setName("DESCC");
        asset3.setData(data1);
        
        
        assets.add(asset);
        assets.add(asset2);
        assets.add(asset2);
        assets.add(asset3);
        
        System.out.println("=====assets:" + assets.size());
        
        nativeAdType.setAssets(assets);
        nativeAdType.setPlcmtcnt(2);
        
        imp.setNativeAdType(nativeAdType);

//    
        imps.add(imp);
        
//        imp.setImpId("0002");
//        imps.add(imp);
        param.setImps(imps);
        return param;
    }
}
