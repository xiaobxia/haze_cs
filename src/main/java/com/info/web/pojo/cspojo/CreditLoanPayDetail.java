package com.info.web.pojo.cspojo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
  还款详细表
*/
@Data
public class CreditLoanPayDetail {
	// 
	private String id;
	// 还款id
	private String payId;
	// 实收本金
	private BigDecimal realMoney;
	// 实收罚息
	private BigDecimal realPenlty;
	// 剩余应还本金
	private BigDecimal realPrinciple;
	// 剩余应还利息
	private BigDecimal realInterest;
	// 还款方式
	private String returnType;
	// 备注
	private String remark;
	// 更新时间
	private Date updateDate;
	// (这里不用)
	private String createBy;
	// (这里不用)
	private String updateBy;
	// (这里不用)
	private Date createDate;
	// 收款银行ID(这里不用)
	private String bankId;
	// 银行流水(这里不用)
	private String bankFlownum;
	// 实收时间(这里不用)
	private Date realDate;
	// 催收员ID
	private String currentCollectionUserId;
	//纪录2-11号S1组订单到S2人手上的标志
	private String s1Flag;

}