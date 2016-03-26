import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.zhidian.remote.service.RemoteService;


public class Test {
	public static void main(String[] args) {
//		try {
//			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
//			RemoteService hw = (RemoteService)context.getBean("ok");
//			int count = 1;
//			Map<String, Object> map1 = new HashMap<String, Object>();
//			map1.put("OK", "OK" + count);
//			hw.getAdMessage(JSONUtils.toJSONString(map1));
//			long statTime = System.currentTimeMillis();
//			for(int i=0;i<1;i++) {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("OK", "OK" + count);
//				String adMessage = hw.getAdMessage(JSONUtils.toJSONString(map));
//				System.out.println("==========adMessage=" + adMessage);
//			}
//			long endTime = System.currentTimeMillis();
//			System.out.println(endTime - statTime);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		test();
		
	}
	
	
	public static void test(){
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			RemoteService hw = (RemoteService)context.getBean("ok");
//				int count = 1;
//				Map<String, Object> map1 = new HashMap<String, Object>();
//				map1.put(\"OK\", \"OK\" + count);
			String testInfo = "{\"adCondition\":{\"adType\":\"1\"," +
					"\"showType\":7,\"unSupportAdType\":\"2,3\"}," +
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
			for(int i=0;i<1;i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String adMessage = hw.getAdMessage(testInfo);
				System.out.println("==========adMessage=" + adMessage);
				System.out.println(JSON.parseObject(adMessage).getClass());
//				Thread.sleep(1000*60);
			}
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - statTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
