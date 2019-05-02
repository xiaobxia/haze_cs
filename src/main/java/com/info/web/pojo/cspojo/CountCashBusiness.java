package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;


@Data
public class CountCashBusiness {
	private Long id;
	// 日期
	private Date reportDate;
	// 到期金额
	private Long expireAmount;
	// 放款总额
	private Long moneyAmountCount;
	// 7天期限放款总额
	private Long sevendayMoenyCount;
	// 14天期限放款总额
	private Long fourteendayMoneyCount;
	// 逾期金额总量
	private Long overdueAmount;
	// 7天逾期总额
	private Long overdueRateSevenAmount;
	// 14天逾期总额
	private Long overdueRateFourteenAmount;
	// 更新时间
	private Date createdAt;

}
