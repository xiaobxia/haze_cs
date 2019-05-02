package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
  催收订单表
*/
@Data
public class MmanLoanCollectionOrder {
	//
	private String id;
	/** 催收订单id */
	private String orderId;
	/** 借款用户ID */
	private String userId;
	/** 借款ID */
	private String loanId;
	/** 还款ID */
	private String payId;
	/** 派单人 */
	private String dispatchName;
	/** 派单时间 */
	private Date dispatchTime;
	/** 上一催收员ID */
	private String lastCollectionUserId;
	/** 当前催收员ID */
	private String currentCollectionUserId;
	/** 当前逾期等级（即逾期字典分组） */
	private String currentOverdueLevel;
	/** 逾期天数 */
	private Integer overdueDays;
	/** s1审批人ID */
	private String m1ApproveId;
	/** s1审批人操作状态（1，已操作过，0或null未操作过） */
	private String m1OperateStatus;
	/** s2审批人ID */
	private String m2ApproveId;
	/** s2审批人操作状态（1，已操作过，0或null未操作过） */
	private String m2OperateStatus;
	/** m1-m2审批人ID */
	private String m3ApproveId;
	/** m1-m2审批人操作状态（1，已操作过，0或null未操作过） */
	private String m3OperateStatus;
	/** m2-m3审批人ID */
	private String m4ApproveId;
	/** m2-m3审批人操作状态（1，已操作过，0或null未操作过） */
	private String m4OperateStatus;
	/** m3+审批人ID */
	private String m5ApproveId;
	/** m3+审批人操作状态（1，已操作过，0或null未操作过） */
	private String m5OperateStatus;
	/** m6审批人ID */
	private String m6ApproveId;
	/** 催收状态 */
	private String status;
	/** 承诺还款时间 */
	private Date promiseRepaymentTime;
	/** 最后催收时间 */
	private Date lastCollectionTime;
	/** 创建时间 */
	private Date createDate;
	/** 更新时间 */
	private Date updateDate;
	/** 操作人 */
	private String operatorName;
	/** 备注 */
	private String remark;
	/** 委外催收人id */
	private String outsidePersonId;
	/** 委外前催收状态 */
	private String beforeStatus;
	/** 委外机构ID */
	private String outsideCompanyId;
	/** 聚立信息报告审核 0 初始 1 申请 2同意 ，3拒绝 */
	private String jxlStatus;
	/** 是否进行续借出催 1启用 0不启用 */
	private String renewStatus;
	/** 聚信立 报告申请时间 */
	private Date jxlApplyTime;
	/** 聚信立报告审核时间 */
	private Date jxAuditIme;
	/** 留单状态 n:否 y：是 */
	private String csstatus;

	/** 减免滞纳金金额 */
	private Long reductionMoney;

	/** 用户信息 */
	private MmanUserInfo mmanUserInfo;

	/** 用户借款表 */
	private MmanUserLoan mmanUserLoan;

	/** 用户关系表 */
	private List<MmanUserRela> mmanUserRela;

	/** 当前催收员 */
	private MmanLoanCollectionPerson mmanLoanCollectionPerson;

	/** 上一个催收员 */
	private MmanLoanCollectionPerson mmanLoanCollectionPerson1;

	/** 催收记录表 */
	private List<MmanLoanCollectionRecord> mmanLoanCollectionRecord;

	/** 流转日志 */
	private List<MmanLoanCollectionStatusChangeLog> mmanLoanCollectionStatusChangeLog;

	/** 还款信息 */
	private CreditLoanPay creditLoanPay;

	/** 还款详情 */
	private List<CreditLoanPayDetail> creditLoanPayDetail;

	private String loanUserName;

	private String loanUserPhone;

	private BigDecimal realMoney;
	private String s1Flag;

	/**更新状态判断*/
	private String careStatus;

	/**
	 * 2018-06-21新增
	 */
	private String s1Date;
	private String s2Date;
	private String s3Date;
	private String m1m2Date;
	private Integer repayRenewalMark;

}