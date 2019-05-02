package com.info.web.pojo.cspojo;


import lombok.Data;

/**
  系统用户绑定的银行卡
*/
@Data
public class SysUserBankCard {
	/** */
	private String id;
	/** 用户id*/
	private String userId;
	/** 卡号*/
	private String bankCard;
	/** 开户行*/
	private String depositBank;
	/** 银行机构号*/
	private String bankInstitutionNo;
	/** 开户支行*/
	private String branchBank;
	/** 姓名*/
	private String name;
	/** 银行预留手机号*/
	private String mobile;
	/** 身份证*/
	private String idCard;
	/** 信用卡CVN*/
	private String cvn;
	/** 代付总行代码*/
	private String bankCode;
	/** 开户行城市代码*/
	private String cityCode;
	/** 开户行城市名称*/
	private String cityName;

}