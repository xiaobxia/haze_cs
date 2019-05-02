package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditLoanPaySum {
	//实收本金总额
	private BigDecimal sumRealMoney;
	//实收罚息总额
	private BigDecimal sumRealPenlty;

}
