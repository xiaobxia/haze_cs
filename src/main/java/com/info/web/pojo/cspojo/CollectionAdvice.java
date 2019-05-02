package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionAdvice {
	//催收建议id
	private String id;
	//订单id
	private String orderId;
	//借款id
	private String loanId;
	//还款id
	private String payId;
	//借款人id
	private String userId;
	//后台操作人id
	private Integer backUserId; 
	//借款人姓名
	private String loanUserName;
	//借款人电话
	private String loanUserPhone;
	//风控标签id
	private String fengkongIds;
	//风控标签名称
	private String fkLabels;
	//后台操作人姓名
	private String userName;
	//创建时间
	private Date createDate;
	//催收建议status   1通过 2拒绝 3无建议
	private String status;

}
