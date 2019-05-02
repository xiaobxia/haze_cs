package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 业务量统计-本金分布
 * @author Administrator
 *
 */
@Data
public class StatisticsDistribute {
	private String type;
    /**待催收 ，本金*/
    private BigDecimal waitMoney;
    /**待催收，滞纳金*/
    private BigDecimal waitPenalty;
    private BigDecimal waitRate;
    private BigDecimal waitPenaltyRate;
    private BigDecimal inMoney;
    private BigDecimal inPenalty;
    private BigDecimal inRate;
    private BigDecimal inPenaltyRate;
    /**承诺还款*/
    private BigDecimal promiseMoney;
    private BigDecimal promisePenalty;
    private BigDecimal promiseRate;
    private BigDecimal promisePenaltyRate;
    /**已还款*/
    private BigDecimal finishMoney;
    private BigDecimal finishPenalty;
    private BigDecimal finishRate;
    private BigDecimal finishPenaltyRate;
    /**本金*/
    private BigDecimal loanMoney;
    /**滞纳金*/
    private BigDecimal loanPenalty;
    /**催收类型*/
	private String csType;
    /**本金*/
	private BigDecimal ownnerMoney;
    /**本金比例*/
	private BigDecimal ownnerMoneyRate;
    /**滞纳金*/
	private BigDecimal penalty;
    /**滞纳金比例*/
	private BigDecimal penaltyRate;

}
