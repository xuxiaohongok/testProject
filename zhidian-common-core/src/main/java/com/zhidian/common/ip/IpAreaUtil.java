package com.zhidian.common.ip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhidian.common.util.CommonLoggerUtil;

/**
 * 如果用IP得到省份，应将IP缓存起来
 * 用户下次来获则不需要到库中查询
 * 通过LBS查
 * 通过IP查询
 * 通过手机号码查询
 *
 */
public class IpAreaUtil {
	
	//定义一个没有省字的列表
	private static List<String> list = new ArrayList<String>();
	private static Map<String, String> map = new HashMap<String, String>();
	
	
    static {
    	list.add("上海市");
    	list.add("内蒙古");
    	list.add("北京市");
    	list.add("天津市");
    	list.add("重庆市");
    	list.add("内蒙古");
    	
    	list.add("广西");
    	map.put("广西", "广西壮族自治区");
    	list.add("西藏");
    	map.put("西藏", "西藏自治区");
    	list.add("宁夏");
    	map.put("宁夏", "宁夏回族自治区");
    	list.add("新疆");
    	map.put("新疆", "新疆维吾尔自治区");
    	list.add("香港");
    	map.put("香港", "香港特别行政区");
    	list.add("澳门");
    	map.put("澳门", "澳门特别行政区");
    	
	}
    
    private static String getProvince(String address){
    	//System.out.println(address);
    	for (String name: list) {
			if(address.indexOf(name)!=-1){
				if(name.length()==2){
					return map.get(name);
				}
				return name;
			}
		}
    	return "";
    }
    
    private static IPSeeker ips = IPSeeker.getInstance();
    public static String getGGCity(String ip){
    	String address = "";
    	IPSeeker ips = IPSeeker.getInstance();
		address = ips.getAddress(ip);
		
		if(address.indexOf("省") !=-1 && address.indexOf("市")!=-1) {
			return address.substring(0, address.indexOf("市") + 1);
		} else if(address.indexOf("市")!= -1 && address.indexOf("区") != -1) {
			return address.substring(0, address.indexOf("区") + 1);
		} else if(address.indexOf("市")!= -1) {
			return address.substring(0, address.indexOf("市") + 1);
		}
		
		return address;
    }
    
    /**
     * 通过ip获取省份
     * @param ip
     * @return
     */
    public static String getAddress(String ip){
    	ip = ip.trim();
    	String address = "";
		try {
			address = ips.getAddress(ip);
			if(address==null){
				address = "";
			}
			//叫移动全省的直接返回
			if(address.indexOf("移动全省")!=-1){
				return "";
			}
			
			if(address.indexOf("省")!=-1){
				address = address.substring(0, address.indexOf("省")+1);
			}else{
				address = getProvince(address);
			}
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e, "IP转地址出错");
			return "";
		}
		
		return address;
    }

	public static void main(String[] args) {
		System.out.println("========" + IpAreaUtil.getAddress("110.195.179.225"));
	}
}
