package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

/**
 * 状态流转表
 */
@Data
public class MmanLoanCollectionStatusChangeLog {
	//
	private String id;
	/** 催收订单ID */
	private String loanCollectionOrderId;

	private String loanTel;
	/** 操作前状态 */
	private String beforeStatus;
	/** 操作后状态 */
	private String afterStatus;
	/**
	操作类型(操作类型 1入催 2派单 3逾期等级转换 4转单 5委外 6取消委外 7委外成功 100催收完成 8月初分组
	9回收(重置催收状态为待催收) 字典)
	*/
	private String type;
	/** 创建时间 */
	private Date createDate;
	/** 操作人 */
	private String operatorName;
	/** 操作备注 */
	private String remark;
	/** 催收公司ID */
	private String companyId;
	/** 当前催收员id */
	private String currentCollectionUserId;
	/** 当前催收员等级 */
	private String currentCollectionUserLevel;
	/** 当前订单组的等级 */
	private String currentCollectionOrderLevel;
	/** 借款id */
	private String loanId;
	/** 还款id */
	private String repayId;
	
}