package com.zhidian3g.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 获取某月份总的天数
	 * @param month
	 * @return
	 */
	public static int getDaysFormMonth(int month) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.set(Calendar.YEAR, 2015);
		aCalendar.set(Calendar.MONTH, 2-1);
		int day=aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

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
	 * 得到时间 格式为 自定义比如: yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static final Date getDate(Date date) {
		return getDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static final String getDateStringByTimeString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = sdf.format(new Date(time));
		return dateString;
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
	 *
	 * 	获取指定日期与今天相差几天 yyyy-MM-dd
	 * @param time1间隙
	 * @return
	 */
	public static long getDifDate(String date) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String time2 = ft.format(new Date());
		try {
			Date date1 = ft.parse(date);
			Date date2 = ft.parse(time2);
			quot = date2.getTime() - date1.getTime();
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
	
	/**
	 * 获取当前时间与凌晨0点的间隔时间，时间为s
	 * @return
	 */
	public static int getDifferentTimeMillis() {
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 24); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0);
		long currenTimeMillis = System.currentTimeMillis();
		Long differentTime = (cal.getTimeInMillis() - currenTimeMillis)/1000;
		return differentTime.intValue();
	}
	
	public static int getNextHourDifferentTimeMillis() {
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0);
		long currenTimeMillis = System.currentTimeMillis();
		Long differentTime = (cal.getTimeInMillis() - currenTimeMillis)/1000;
		return differentTime.intValue();
	}
	
	public static void test() {
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		int second = c.get(Calendar.SECOND); 
		System.out.println(year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second); 
	}
	
	  public static int compare_date(String DATE1, String DATE2) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	                System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	
	public static void main(String oarg[]) {
//		System.out.println(countDate("2015-3-06"));
		System.out.println(getNextHourDifferentTimeMillis());
	     System.out.println((1445356800000l - 1443715200000l)/1000/60/60/60);
	     System.out.println(getDateStringByTimeString(System.currentTimeMillis()));
//		System.out.println("yesterday is: " + getPreviousDate(new Date(),1));
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Calendar c = Calendar.getInstance();  
//		Date date = new Date();
//		c.setTime(date);
//		c.add(c.DATE, 1);//添加一天
//		c.add(c.HOUR, 1);//添加一个小时
//		Date nowDate = c.getTime();
//		System.out.println(simpleDateFormat.format(nowDate));
//		
//		Calendar cal = Calendar.getInstance(); 
//		cal.set(Calendar.HOUR_OF_DAY, 24); 
//		cal.set(Calendar.SECOND, 0); 
//		cal.set(Calendar.MINUTE, 0); 
//		cal.set(Calendar.MILLISECOND, 0); 
//		System.out.println(cal.getTimeInMillis());
//		System.out.println(System.currentTimeMillis());
//		System.out.println(simpleDateFormat.format(new Date(cal.getTimeInMillis())));
//		System.out.println(getDifferentTimeMillis());
//		System.out.println((int)getDifferentTimeMillis());
	}
}
