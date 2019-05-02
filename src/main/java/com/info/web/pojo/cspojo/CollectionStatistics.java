package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CollectionStatistics {
	private String status;  //催收状态
	private BigDecimal loanMoney;   //本金
	private BigDecimal loanPenalty;  //滞纳金
    private BigDecimal notRepayment;  //未还本金
    private BigDecimal yetRepayment;  //已还本金
    private BigDecimal yesterdayMoney;  //昨日新增本金
    private BigDecimal loanMoneyRate;   //本金还款率
    private Date createDate;  //创建时间
    private String dateStr;  //创建时间str

}
