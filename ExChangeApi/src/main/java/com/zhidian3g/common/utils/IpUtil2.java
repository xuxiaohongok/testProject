package com.zhidian3g.common.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class IpUtil2 {

	/**
	 * 获得真实的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for-pound");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getIP() {
		Enumeration<?> netInterfaces;
		List<NetworkInterface> netlist = new ArrayList<NetworkInterface>();
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();// 获取当前环境下的所有网卡
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				if (ni.isLoopback())
					continue;// 过滤 lo网卡
				netlist.add(0, ni);// 倒置网卡顺序
			}
			/**
			 * 用上述方法获取所有网卡时，得到的顺序与服务器中用ifconfig命令看到的网卡顺序相反，
			 * 因此，想要从第一块网卡开始遍历时，需要将Enumeration<?>中的元素倒序
			 */

			for (NetworkInterface list : netlist) { // 遍历每个网卡

				Enumeration<?> cardipaddress = list.getInetAddresses();// 获取网卡下所有ip

				while (cardipaddress.hasMoreElements()) {// 将网卡下所有ip地址取出
					InetAddress ip = (InetAddress) cardipaddress.nextElement();
					if (!ip.isLoopbackAddress()) {
						if (ip.getHostAddress().equalsIgnoreCase("127.0.0.1")) {// guo<span></span>
							// return ip.getHostAddress();
							continue;
						}
						if (ip instanceof Inet6Address) { // 过滤ipv6地址 add by
															// liming 2013-9-3
							// return ip.getHostAddress();
							continue;
						}
						if (ip instanceof Inet4Address) { // 返回ipv4地址
							return ip.getHostAddress();
						}
					}
					return ip.getLocalHost().getHostAddress();// 默认返回
				}

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		String[] split = "xx".split(",");
		System.out.println(split);
		String ips = "172.16.141.12,172.16.141.2,172.16.141.3,172.16.141.4,172.16.141.5";
		System.out.println(ips.contains(getIP()));
	}

}
