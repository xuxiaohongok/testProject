package dubbo.test;

import com.alibaba.fastjson.JSON;
import com.zhidian.remote.vo.request.AdRequestParam;

public class JsonToObject {

	public static void main(String[] args) {
		String test = "{" +
				"\"adCondition\":{\"adType\":1,\"showType\":6}," +
				"\"adSlot\":{\"bidMinimum\":0,\"height\":150,\"width\":960}," +
				"\"bidType\":1,\"ip\":\"114.244.128.102\"," +
				"\"mediaPlatformId\":222222," +
				"\"mobile\":{\"androidId\":\"0693d09b9def7167787033e33baeb99a2016f08f\",\"brand\":\"Meizu\",\"carrier\":\"46000\"," +
					"\"deviceOS\":2,\"deviceType\":2,\"idfa\":\"424224610848fbe4d6e761a0a4b4dcec23948af6\"," +
					"\"imei\":\"97b091fd8e21a14eeaa75dadb7ca46f0c0ae4eaa\"," +
					"\"mac\":\"424224610848fbe4d6e761a0a4b4dcec23948af6\",\"model\":\"m1note\",\"networkType\":1," +
					"\"uuId\":\"97b091fd8e21a14eeaa75dadb7ca46f0c0ae4eaa\"}," +
				"\"mobileApp\":{\"appId\":\"5d986240a4b4b5b8949e0c20903e6209\",\"appName\":\"AdViewTest\",\"appPackageName\":\"com.kyview.test\"}," +
				"\"serialNumber\":\"20160311-145759_bidreq_201-1125-6Rhb-1\",\"terminalType\":1}";
		AdRequestParam param = JSON.parseObject(test, AdRequestParam.class);
		System.out.println(param.getAdCondition().getAdType());
	}
}
