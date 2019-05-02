package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionWithholdingRecord {
	private String id;
	//借款用户id
	private String loanUserId;
	//借款人姓名
	private String loanUserName;
	//借款人手机号码
	private String loanUserPhone;
	//当前订单状态
	private String orderStatus;
	//欠款金额
	private String arrearsMoney;
	//当前已还金额
	private String hasalsoMoney;
	//扣款金额
	private String deductionsMoney;
	//状态 0 初始值  1 成功 2 失败
	private Integer status;
	//操作用户ID
	private String operationUserId;
	//创建时间
	private Date createDate;
	//修改时间
	private Date updateDate;
	//订单编号
	private String orderId;

}
