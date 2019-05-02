package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class Orders {
	/** BBD请求 */
	public static final String BBD = "BBD";
	/** 微信请求 */
	public static final String WECHART = "WECHART";
	/** 阿里请求 */
	public static final String ALIPAY = "ALIPAY";
	/**该笔请求的非成功状态*/
	public static final String STATUS_OTHER="1";
	/**该笔请求的成功状态*/
	public static final String STATUS_SUC="2";
	private Integer id;
	private Integer userId;
	private User user;
	private String orderType;
	private String orderNo;
	private String act;
	private String reqParams;
	private String returnParams;
	private Date notifyTime;
	private Date addTime;
	private String addIp;
	private Date updateTime;
	private String status;

}
