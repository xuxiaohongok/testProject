package com.zhidian3g.nextad.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse.Ad;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse.Ad.ExposureUrl;

public class CYBITest {
	
	@Test
	public void testCYAPI() {
	     try {
	    	 download("http://localhost:8023/ExChangeApi/adShow.shtml?mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F0&requestId=cyAd1652632011402240&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-11_09:17:22&price=4000&cyRId=354354");
//	    	 download("http://dspapi.nextading.com/adShow.shtml?mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F0&requestId=cyAd1652632011402240&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-11_11:52:18&price=4000&cyRId=cyAd1652632011402240");
	    	 long startTime = System.currentTimeMillis();
//	    	 for(int i=0;i<1000;i++) {
//	    		 download("http://localhost:8089/ExChangeApi/cyWinCallBack.shtml?positionNumber=1&ct=changying&price=123&reqid=123&timeStamp=" + System.currentTimeMillis());
//	    	 }
	    	 long endTime = System.currentTimeMillis();
	    	 System.out.println("Time=" + (endTime - startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	private void upload() throws IOException {  
		String url = "http://www.baidu.com";  
		// **** 曝光检测地址信息 ****
		ExposureUrl exposureUrl = ExposureUrl.newBuilder()
									.setTimeMillis(10000)
									.addExposureUrl("ok")
									.build();
        
       Ad ad = Ad.newBuilder()
    		   // 最高竞价，1元=10000单位，例如，15000代表1.5元
    		   .setMaxCpm(15000)
    		   .setExtdata("extdata")
    		   .setIsCookieMatching(false)
    		   .setHtmlSnippet("")
    		   .setWidth(400)
    		   .setHeight(400)
    		   .setCategory(12)
    		   .setType(12)
    		   .setLandingPage("http://www.baidu.com")
    		   .setAdm("http://www.baidu.com")
    		   .setNurl("")
    		   .addTargetUrl("dfhadf")
    		   .setPackageName("com.test.test")
    		   .addExposureUrls(exposureUrl)
    		   .addClickUrls("www.baidu.com")
    		   .setInteractionType(2)
    		   .setDownloadName("测试包")
    		   .setDuration(12)
    		   .setType(1)
    		   .setTitle("标题")
    		   .setDescription1("描述1")
    		   .setDescription2("描述2")
    		   .setIconUrl("http://www.baidu.com")
    		   .build();
       
       BidResponse bidResponse = BidResponse.newBuilder().setId("nextAdTest")
        		.setDebugString("testDebug")
        		.setProcessingTimeMs(20)
        		.setDspId("nextAdDsp")
        		.addAd(ad)
        		.build();
        
        
        byte[] content = bidResponse.toByteArray();  
          
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
        
        System.out.println(bidResponse);  
        
        InputStream inStream=connection.getInputStream();
        try {
			System.out.println(new String(readInputStream(inStream), "utf-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        inStream.close();  
        outputStream.flush();  
        outputStream.close();  
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("end");
    }  
	
	
	private byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
	
    public void download(String url) throws IOException {          
        URL target = new URL(url);  
        HttpURLConnection conn = (HttpURLConnection) target.openConnection();  
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setRequestMethod("GET");  
        conn.setRequestProperty("Content-Type", "application/octet-stream");  
        conn.setRequestProperty("Accept", "application/x-protobuf");  
        conn.connect();  
        // check response code  
        int code = conn.getResponseCode();  
        System.out.println("code:" + code);  
//        System.out.println(conn.getContent());  
//        boolean success = (code >= 200) && (code < 300);  
          
//        InputStream in = success ? conn.getInputStream() : conn.getErrorStream();  
//        AddressBook addressBook = AddressBook.parseFrom(in);  
//        in.close();  
//        System.out.println(addressBook);  
    }  
      
	
}
