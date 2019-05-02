package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class SmsUser {
	private Integer id;
	private String userName;
	private String userPhone;
	/** 添加时间(发送时间)*/
	private Date addTime;
	private String smsContent;
	private Date updateTime;
	/** 借款编号*/
	private String loanOrderId;
	private String sendUserId;
	
}
