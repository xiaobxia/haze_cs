package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionStatisticsOrder {
	private String status;  //催收状态
	private Integer loanNum;   //总订单数
	private Integer penalty;  //滞纳金数
    private Integer notRepayment;  //未还订单数
    private Integer yetRepayment;  //已还订单数
    private Integer yesterday;  //昨日新增订单数
    private Double loanRate;   //还款率
    private Date createDate;  //创建时间
    private String dateStr;  //创建时间str
	
}
