package com.zhidian3g.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获得某一日期的后天 n  天safsdfas
	 * 
	 * @param date
	 * @return String
	 */
	public static String getNextDate(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + n);
		return get(calendar.getTime(),"yyyy-MM-dd");
	}
	
	public static Date getNextDate(String dateString, int days) {
		Date endDate = DateUtil.getStringToDate(dateString);
		if(endDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.DAY_OF_MONTH, days);
			endDate = calendar.getTime();
		}
		return endDate;
	}

	/**
	 * 获得某一日期的前 n 天
	 * 
	 * @param date
	 * @return String
	 */
	public static String getPreviousDate(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - n);
		return get(calendar.getTime(),"yyyy-MM-dd");
	}

	/**
	 * 得到日期和时间 格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static final String getDateTime() {
		return get("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到日期 格式为 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static final String getDate() {
		return get("yyyy-MM-dd");
	}

	/**
	 * 得到时间 格式为 HH:mm:ss
	 * 
	 * @return
	 */
	public static final String getTime() {
		return get("HH:mm:ss");
	}

	/**
	 * 得到时间 格式为 自定义比如: yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static final String get(String str) {
		SimpleDateFormat f = new SimpleDateFormat(str);
		String time = f.format(new Date());
		return time;
	}
	
	/**
	 * 把字符串按格式转为时间 自定义比如: yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static final Date get(String date, String format){
		SimpleDateFormat ft = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = ft.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	
	/**
	 * 得到日期和星期 格式为：yyyy年MM月dd日 星期几
	 * 
	 * @return
	 */
	public static final String getDateWeek() {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
		String date = f.format(new Date());
		Calendar cal=Calendar.getInstance(); 
		String week=weekDays[cal.get(Calendar.DAY_OF_WEEK)-1];
		String result=date +" "+week;
		return result;
	}
	/**
	 * 得到时间 格式为 自定义比如: yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static final String get(Date date,String str) {
		SimpleDateFormat f = new SimpleDateFormat(str);
		String time = f.format(date);
		return time;
	}
	
	/**
	 * 得到时间 格式为 自定义比如: yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static final Date getDate(Date date,String format) {
		SimpleDateFormat ft = new SimpleDateFormat(format);
		String str = get(date, format);
		Date d = null;
		try {
			d = ft.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	

	/**
	 * 
	 * 计算两个日期差
	 *  今天距date还有多少天
	 * @param time1
	 * @return
	 */
	public static long countDate(String date) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String time2 = ft.format(new Date());
		try {
			Date date1 = ft.parse(date);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}
	
	/**
	 * 获得两个日期中最小的一个
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Date getMinDate(Date date1,Date date2){
		if(date1==null||date2==null){
			return null;
		}
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = ft.format(date1);
		String time2 = ft.format(date2);
		long day1 = countDate(time1);
		long day2 = countDate(time2);
		if(day1<day2){
			return date1;
		}else{
			return date2;
		}
	}
	
	/**
	 * 获得两个日期中最大的一个
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Date getMaxDate(Date date1,Date date2){
		if(date1==null||date2==null){
			return null;
		}
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = ft.format(date1);
		String time2 = ft.format(date2);
		long day1 = countDate(time1);
		long day2 = countDate(time2);
		if(day1>day2){
			return date1;
		}else{
			return date2;
		}
	}
	
	/**
	 * 将时间格式的字符串转变为LONG
	 * @param str
	 * @return
	 */
	public static Long getDateStringToLong(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		long nowtime = 0;   
		try{   
			nowtime = sdf.parse(str).getTime();   
		}catch(ParseException e){   
			System.out.println(e);
			return System.currentTimeMillis();
		}   
		return nowtime;
	}
	
	/**
	   * 提取一个月中的最后一天
	   * 
	   * @param day
	   * @return
	   */
	public static Date getLastDate(long day) {
	   Date date = new Date();
	   long date_3_hm = date.getTime() - 3600000 * 34 * day;
	   Date date_3_hm_date = new Date(date_3_hm);
	   return date_3_hm_date;
	}
	
	public static Date getStringToDate(String str){
		if(str != null && str.length()>0)
		{
			// 设定接收25JUL的日期格式
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		    // 将接收到的字符串转化为Date类型
		    try {
				return df1.parse(str);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 当前时间与给定的时间的间隔 单位为s
	 * @param timeStam
	 * @return 返回值单位为秒
	 */
	public static Long getTime(Long timeStamp) {
		if(timeStamp == null) return 0l;
		
		long nowTimeStamp = System.currentTimeMillis();
		return (nowTimeStamp - timeStamp)/1000l;
	}
	
	public static void main(String oarg[]) {
		System.out.println(countDate("2008-11-06"));
		System.out.println("yesterday is: " + getPreviousDate(new Date(),1));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();  
		Date date = new Date();
		c.setTime(date);
		c.add(c.DATE, 1);//添加一天
		c.add(c.HOUR, 1);//添加一个小时
		Date nowDate = c.getTime();
		System.out.println(simpleDateFormat.format(nowDate));
		
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 24); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		System.out.println(cal.getTimeInMillis() - System.currentTimeMillis());
		System.out.println(simpleDateFormat.format(new Date(cal.getTimeInMillis())));
	}
}
