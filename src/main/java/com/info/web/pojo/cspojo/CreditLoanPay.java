package com.info.web.pojo.cspojo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
  还款信息
*/

@Data
public class CreditLoanPay {
	private String id;
	/** 借款Id */
	private String loanId;
	/** 起始日 */
	private Date receivableStartdate;
	/** 应还日期 */
	private Date receivableDate;
	/** 应还金额 */
	private BigDecimal receivableMoney;
	/** 剩余应还本金 */
	private BigDecimal receivablePrinciple;
	/** 剩余应还罚息 */
	private BigDecimal receivableInterest;
	/** 实收金额 */
	private BigDecimal realMoney;
	/** 实收本金 */
	private BigDecimal realgetPrinciple;
	/** 实收罚息 */
	private BigDecimal realgetInterest;
	/** 减免金额 */
	private BigDecimal reductionMoney;
	/** 还款状态（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+） */
	private Integer status;
	/** 创建时间 */
	private Date createDate;
	/** 最后更新时间 */
	private Date updateDate;
	/** 待收本金（这里不用） */
	private BigDecimal restPrinciple;
	/** 分期方式（这里不用） */
	private Integer installmentMethod;
	/** （这里不用） */
	private String createBy;
	/** （这里不用） */
	private String updateBy;


}