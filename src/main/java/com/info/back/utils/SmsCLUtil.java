package com.info.back.utils;

/**
 * 发短信常量类
 * @author wayne
 */
public class SmsCLUtil {
	
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("sms.properties");
	/**
	 * 发送短信的请求地址
	 */
	public static final String APIURL = propertiesLoader.getProperty("apiurl");
	/**
	 * 用户账号
	 */
	public static final String USER = propertiesLoader.getProperty("account");
	
	/**
	 * 密钥
	 */
	public static final String APPKEY = propertiesLoader.getProperty("pswd");
	

}
