package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CountCollectionManage {
	private Long id;
	//
	private String personId;
	/** 姓名 */
	private String personName;
	/** 催单公司id */
	private String companyId;
	/** 催单公司名称 */
	private String companyTitle;
	/** 催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+） */
	private String groupId;
	/** 催收员分组组名 */
	private String groupName;
	/** 订单分组组名 */
	private String groupOrderName;
	/** 本金 */
	private BigDecimal loanMoney;
	/** 已还本金 */
	private BigDecimal repaymentMoney;
	/** 未还本金 */
	private BigDecimal notYetRepaymentMoney;
	/** 本金还款率 */
	private BigDecimal repaymentReta;
	/** 迁徙率 */
	private BigDecimal migrateRate;
	/** 滞纳金总额 */
	private BigDecimal penalty;
	/** 已还滞纳金 */
	private BigDecimal repaymentPenalty;
	/** 待还滞纳金 */
	private BigDecimal notRepaymentPenalty;
	/** 滞纳金回款率 */
	private BigDecimal penaltyRepaymentReta;
	/** 订单总数 */
	private Integer orderTotal;
	/** 已处理订单数 */
	private Integer disposeOrderNum;
	/** 已还款订单数 */
	private Integer repaymentOrderNum;
	/** 订单还款率 */
	private BigDecimal repaymentOrderRate;
	/** 统计时间 */
	private Date countDate;
	
	private String riskOrderNum;
}
