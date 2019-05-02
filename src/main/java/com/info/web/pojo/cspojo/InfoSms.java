package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class InfoSms {
	
	private Integer id;
	private String userName;
	private String userPhone;
	private Date addTime;
	private String smscontent;
	private String loanOrderId;

}
