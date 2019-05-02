package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MmanLoanCollectionOrderdeduction {
    private String id;

    private String loanrealname;

    private String loanuserphone;

    private BigDecimal returnmoney;

    private BigDecimal deductionmoney;

    private Date createtime;

    private String deductionremark;

}