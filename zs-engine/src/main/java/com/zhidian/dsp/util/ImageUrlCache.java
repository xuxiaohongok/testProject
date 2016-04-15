package com.zhidian.dsp.util;

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
