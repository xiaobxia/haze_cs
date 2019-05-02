package com.info.back.sms.zt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类型描述：时间
 * @author 杨康
 * @date 日期：2016-5-3  时间：下午3:15:48
 * @version 1.0
 */
public class TimeUtil {
	
	/**
	 * 计算指定日期距今多少年
	 * @param times 指定日期
	 * @return 年
	 */
	public static int getTime(String times){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String time=sdf.format(new Date());
		String t1 = time.replace('-','/');
		String t2 = times.replace('-','/');

		Date dt1= null;
		Date dt2 = null;
		try {
			dt1 = DateFormat.getDateTimeInstance().parse(t1);
			dt2 = DateFormat.getDateTimeInstance().parse(t2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long i= (dt1.getTime() - dt2.getTime())/(1000*60*60*24);
		return (int) (i/365);
	}
	
	/**
	 * 返回当前时间
	 * @return
	 */
	public static String getNowTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(new Date());
		return time;
	}
	
	/**
	 * 返回当前时间
	 * @return
	 */
	public static String getNowTime(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String time=sdf.format(new Date());
		return time;
	}
}
