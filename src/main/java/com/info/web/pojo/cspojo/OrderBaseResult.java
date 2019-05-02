package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderBaseResult {
	private String id;
	/** 借款编号*/
	private String loanId;
	/** 催收公司*/
	private String companyTile;
	/** 催收组*/
	private String collectionGroup;
	/** 借款人姓名*/
	private String realName;
	/** 身份证号码*/
	private String idCard;
	/** 借款人手机号*/
	private String phoneNumber;
	/** 借款金额*/
	private BigDecimal loanMoney;
	/** 逾期天数*/
	private Integer overdueDays;
	/** 滞纳金*/
	private BigDecimal loanPenlty;
	/** 催收状态*/
	private String collectionStatus;
	/** 应还时间*/
	private Date loanEndTime;
	/** 还款状态*/
	private Integer returnStatus;
	/** 已还金额*/
	private BigDecimal returnMoney;
	/** 最新还款时间*/
	private Date currentReturnDay;
	/** 最后催收时间*/
	private Date lastCollectionTime;
	/** 承诺还款时间*/
	private Date promiseRepaymentTime;
	/** 派单时间*/
	private Date dispatchTime;
	/**派单人*/
	private String dispatchName;
	/** 当前催收员*/
	private String currUserName;
	/** 上一催收员*/
	private String lastUserName;
	/**跟进准太*/
	private String topImportant;
	/** 短信次数 add by 170311*/
	private Integer msgCount;
	/**减免滞纳金金额*/
	private Long reductionMoney;
	private String csstatus;
	/**
	 * 最近一次催收记录内容
	 */
	private String csContent;


}
