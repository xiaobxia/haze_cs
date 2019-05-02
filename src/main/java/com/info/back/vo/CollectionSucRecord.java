package com.info.back.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CollectionSucRecord {
    private Integer id;

    private Date lateDate;

    private Date rechargeDate;

    private Integer loanId;

    private String loanName;

    private String loanTel;

    private Integer rechargeStatus;

    private BigDecimal loanMoney;

    private BigDecimal lateFee;

    private BigDecimal reducMoney;

    private BigDecimal repayedAmount;

    private Integer lateDay;

    private String collectName;

    private String dispatchName;

    private Date createDate;

    private Date updateDate;

}