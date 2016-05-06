package com.zhidian.util;

import java.util.HashMap;
import java.util.Map;

public class ImageUrlCache {

	private static ImageUrlCache imageUrlCache;
	
	private static Map<String,String> imagerUrlsForWH = null;
	
	private ImageUrlCache(){
		imagerUrlsForWH = new HashMap<String, String>();
		imagerUrlsForWH.put("2_24038","http://d.zhidian3g.cn/zhidian-smart/adView/24038.jpg");
		imagerUrlsForWH.put("2_32050","http://d.zhidian3g.cn/zhidian-smart/adView/32050.jpg");
		imagerUrlsForWH.put("2_48075","http://d.zhidian3g.cn/zhidian-smart/adView/48075.jpg");
		imagerUrlsForWH.put("2_72090","http://d.zhidian3g.cn/zhidian-smart/adView/72090.jpg");
		imagerUrlsForWH.put("2_300250","http://d.zhidian3g.cn/zhidian-smart/adView/300250.jpg");
		imagerUrlsForWH.put("2_300300","http://d.zhidian3g.cn/zhidian-smart/adView/300300.jpg");
		imagerUrlsForWH.put("2_600500","http://d.zhidian3g.cn/zhidian-smart/adView/600500.jpg");
		imagerUrlsForWH.put("2_600600","http://d.zhidian3g.cn/zhidian-smart/adView/600600.jpg");
		imagerUrlsForWH.put("2_640100","http://d.zhidian3g.cn/zhidian-smart/adView/640100.jpg");
		imagerUrlsForWH.put("2_960150","http://d.zhidian3g.cn/zhidian-smart/adView/960150.jpg");
		
		imagerUrlsForWH.put("3_160120","http://d.zhidian3g.cn/zhidian-smart/youdao/160x120.jpg");
		imagerUrlsForWH.put("3_170130","http://d.zhidian3g.cn/zhidian-smart/youdao/170x130.jpg");
		imagerUrlsForWH.put("3_360360", "http://d.zhidian3g.cn/zhidian-smart/cy/1/360x360.jpg");
		
//		imagerUrlsForWH.put("4_250300","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/250300.jpg");
//		imagerUrlsForWH.put("4_300250","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/300250.jpg");
//		imagerUrlsForWH.put("4_32050","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/32050.jpg");
//		imagerUrlsForWH.put("4_46860","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/46860.jpg");
//		imagerUrlsForWH.put("4_500600","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/500600.jpg");
//		imagerUrlsForWH.put("4_600500","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/600500.jpg");
//		imagerUrlsForWH.put("4_640960","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/640960.jpg");
//		imagerUrlsForWH.put("4_640100","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/640100.jpg");
//		imagerUrlsForWH.put("4_72890","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/72890.jpg");
//		imagerUrlsForWH.put("4_750118","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/750118.jpg");
//		imagerUrlsForWH.put("4_936120","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/936120.jpg");
//		imagerUrlsForWH.put("4_960640","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/960640.jpg");
//		imagerUrlsForWH.put("4_1080170","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/1080170.jpg");
//		imagerUrlsForWH.put("4_1456180","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/1456180.jpg");
		
		imagerUrlsForWH.put("4_300250","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/300250.png");
		imagerUrlsForWH.put("4_32050","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/32050.png");
		imagerUrlsForWH.put("4_46860","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/46860.png");
		imagerUrlsForWH.put("4_600500","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/600500.png");
		imagerUrlsForWH.put("4_640960","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/640960.png");
		imagerUrlsForWH.put("4_72890","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/72890.png");
		imagerUrlsForWH.put("4_960640","http://d.zhidian3g.cn/zhidian-smart/zhangyou/test/2/960640.png");
		
		
		
		
		
		
		
		
	}
	
	public static ImageUrlCache getInstance(){
		if(imageUrlCache==null){
			synchronized (ImageUrlCache.class) {
				if(imageUrlCache==null){
					imageUrlCache = new ImageUrlCache();
				}
			}
		}
		return imageUrlCache;
	}
	
	/**
	 * key类型  adxId_wh   1、畅言   2、adview  3、有道 如：2_640100
	 * @param key
	 * @return
	 */
	public static String byAdxIdAndWHToImageUrl(String key){
		return imagerUrlsForWH.get(key);
	}
}
