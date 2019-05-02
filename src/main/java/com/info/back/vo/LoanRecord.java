package com.info.back.vo;

import java.util.Date;

/**
 * Created by cqry_2016 on 2018/6/21
 */
public class LoanRecord {

    private String mmanloanOrderId;
    private Integer loanId;
    private String name;
    private Date loanTime;
    private Date lastRepayTime;
    private Integer repayedAmount;
    private Integer total;
    private Integer lateDays;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Date getLastRepayTime() {
        return lastRepayTime;
    }

    public void setLastRepayTime(Date lastRepayTime) {
        this.lastRepayTime = lastRepayTime;
    }

    public Integer getRepayedAmount() {
        return repayedAmount;
    }

    public void setRepayedAmount(Integer repayedAmount) {
        this.repayedAmount = repayedAmount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public String getMmanloanOrderId() {
        return mmanloanOrderId;
    }

    public void setMmanloanOrderId(String mmanloanOrderId) {
        this.mmanloanOrderId = mmanloanOrderId;
    }
}
