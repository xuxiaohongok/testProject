package com.zhidian3g.nextad.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.AdSlot;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.AdSlot.Position;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.ChangyanId;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.Gender;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.Mobile;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.Mobile.OS;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse.Ad;


public class CYRequestTest {
	
	@Test
	public void testCYAPI() {
		try {
//			download("http://192.168.2.78:8089/ExChangeApi/adShow.shtml?mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=testiiiifhs2" +
//					"&requestId=testiiiifhs2&adId=6&adBlockKey=0&adSlotType=13&createId=4&landingPageId=4&height=320" +
//					"&width=50&requestAdDateTime=2016-04-06_16:28:55&price=4000&cyRId=testiiiifhs2");
//			for(int i=0; i<20;i++) {
//			for(;;) {
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						for(; ; ) {
////							for(int i=0; i<1; i++) {
//								try {
//									upload("testiiiifhs2");
//									Thread.sleep(10);
//								} catch (IOException e) {
//									e.printStackTrace();
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							}
//					}
//				}).start();
//			}
			
//			for(; ; ) {
			for(int i=0; i<3; i++) {
				upload("testiiiifhs2");
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private int count = 0;
	private void upload(String cyRequestId) throws IOException {  
//		String url = "http://bjdsp.zhidian3g.cn/cyDspTest.shtml";
//		String url = "http://dsp.zhidian3g.cn/cyDspTest.shtml";
//		String url = "http://zdsp.nextading.com/cyDspTest.shtml";
//		String url = "http://bjtestdsp.zhidian3g.cn/cyDspTest.shtml";
//		String url = "http://192.168.2.67:8089/cyDspTest.shtml";
		String url = "http://localhost:8089/ExChangeApi/cyDspTest.shtml";
//		String url = "http://zdsp.nextading.com/cyDspTest.shtml";
//		String url = "http://qdsp.nextading.com/cyDspTest.shtml";
//		String url = "http://dspapi.nextading.com/cyDspTest.shtml";
//		http://localhost:8189/ExChangeApi/cyWinCallBack.shtml?op=impr&ct=changying&price=%%PRICE%%&ext=testOk&reqid=%%ID%%
//		String url = "http://dspapi.nextading.com/adShow.shtml?mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C6899000012AE21C8C8BC01B7F&requestId=cyAd1652632011402240&adId=1&adBlockKey=0&adSlotType=1&createId=createFlyId1&landingPageId=landingPageId1&height=320&width=50&requestAdDateTime=2016-03-10%2011:48:47&price=123&cyRId=132";  
//		http://localhost:8189/ExChangeApi/adClick.shtml?positionNumber=3&ct=adClick
		// **** 曝光检测地址信息 ****
		count++;
		ChangyanId changyanId = ChangyanId.newBuilder()
								.setChangyanUserId(cyRequestId)
								.setChangyanUserIdVersion(1)
								.build();
		Position position = Position.FLOW;
		Integer[] adSlotTypeArray = {1, 13};
		int adSlotType = count%2==0 ? adSlotTypeArray[0] : adSlotTypeArray[1];
		AdSlot adSlot = AdSlot.newBuilder()
						.setAdslotType(13)
						.setHeight(320)
						.setWidth(50)
						.setPosition(position)
						.addCreativeType(0)
						.build();
		Mobile mobile = null;
		if(count%2==0) {
			mobile = Mobile.newBuilder()
		   			.setDeviceId("asdg")
		   			.setPlatform(OS.ANDROID)
		   			.build();
		} else {
			mobile = Mobile.newBuilder()
		   			.setDeviceId("asdg")
		   			.setPlatform(OS.ANDROID)
		   			.build();
		}
		
		
       BidRequest bidRequest = BidRequest.newBuilder()
				    		   .setId(cyRequestId)
				    		   .setIp("10.2.59.72")
				    		   .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
				    		   .setChangyanId(changyanId)
				    		   .setGender(Gender.MALE)
//				    		   .setMobile(mobile)
				    		   .addUserCategory(1)
				    		   .addAdslot(adSlot)
				    		   .addPageKeyword("zhidian")
				    		   .build();
        
        byte[] content = bidRequest.toByteArray();  
        URL targetUrl = new URL(url);  
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();  
        connection.setDoOutput(true);  
        connection.setDoInput(true);  
        connection.setRequestProperty("Content-Type", "application/octet-stream");  
        connection.setRequestProperty("Accept", "application/x-protobuf");  
        connection.setRequestMethod("POST");  
        connection.setRequestProperty("Connect-Length", Integer.toString(content.length));  
        connection.setFixedLengthStreamingMode(content.length);  
        OutputStream outputStream = connection.getOutputStream();  
        outputStream.write(content);  
        
        InputStream inStream=connection.getInputStream();
        BidResponse bidResponse = BidResponse.parseFrom(inStream);
        int adCount = bidResponse.getAdCount();
//        System.out.println("bidResponse=" + bidResponse);
        if(adCount == 0) {
        	System.out.println("===========获取不到广告===========");
        	return;
        } else {
        	System.out.println("===========有广告===========");
        }
        String requestId = bidResponse.getId();
       
        Ad  ad = bidResponse.getAd(0);
        String landingPage = ad.getLandingPage();
        System.out.println("landingPage=" + landingPage);
        String winUrl = ad.getNurl().replace("%%PRICE%%", "4000").replace("%%ID%%", requestId);
        String showUrl = ad.getExposureUrls(0).getExposureUrl(1).replace("%%PRICE%%", "4000").replace("%%ID%%", requestId);
        String clickUrl = ad.getClickUrls(0);
        String clickUrl1 = ad.getClickUrls(1);
        System.out.println("showUrl=" + showUrl);
//        System.out.println("clickUrl1=" + clickUrl1);
        inStream.close();  
        outputStream.flush();  
        outputStream.close();  
       
//        download(winUrl);
//        download(showUrl);
//        download(clickUrl);
//        download(clickUrl1);
        
        System.out.println(requestId + "=====end");
    }  
	
	public void download(String url) throws IOException {   
		System.out.println("url=" + url);
        URL target = new URL(url);  
        HttpURLConnection conn = (HttpURLConnection) target.openConnection();  
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setRequestMethod("GET");  
        conn.setRequestProperty("Content-Type", "application/octet-stream");  
        conn.setRequestProperty("Accept", "application/x-protobuf");  
        conn.connect();  
        int code = conn.getResponseCode();  
        System.out.println("code:" + code);  
    }  
	
      
	
}
