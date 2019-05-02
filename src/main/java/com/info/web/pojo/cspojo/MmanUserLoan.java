package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
  用户借款表
*/
@Data
public class MmanUserLoan {
	/** */
	private String id;
	/** 借款用户id*/
	private String userId;
	/** 借款订单号或借款编号*/
	private String loanPyId;
	/** 借款金额，元*/
	private BigDecimal loanMoney;
	/** 借款利率（如10）*/
	private String loanRate;
	/** 罚息金额*/
	private BigDecimal loanPenalty;
	/** 罚息率(如10，按天算)*/
	private String loanPenaltyRate;
	/** 放款时间（借款起始时间）*/
	private Date loanStartTime;
	/** 应还时间（借款结束时间）*/
	private Date loanEndTime;
	/** 借款状态（'4'-逾期,'5'-还款结束）*/
	private String loanStatus;
	/** 创建时间*/
	private Date createTime;
	/** 更新时间*/
	private Date updateTime;
	/** 删除标记（'1'-已删除，'0'正常）*/
	private String delFlag;
	/** 这里不用*/
	private String loanPenaltyTime;
	/** 这里不用*/
	private String loanSysStatus;
	/** 这里不用*/
	private String loanSysRemark;

	private Integer repayRenewalMark;

}