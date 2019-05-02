package com.info.back.vo;

import java.util.Date;

/**
 * Created by cqry_2016 on 2018/6/21
 */
public class RenewOrPayRecord {

    private Integer repayId;
    private Date handleTime;
    private String handleType;
    private Integer repayedAmount;
    private Integer leftAmount;
    private Integer lateDays;

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public Integer getRepayedAmount() {
        return repayedAmount;
    }

    public void setRepayedAmount(Integer repayedAmount) {
        this.repayedAmount = repayedAmount;
    }

    public Integer getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(Integer leftAmount) {
        this.leftAmount = leftAmount;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public Integer getRepayId() {
        return repayId;
    }

    public void setRepayId(Integer repayId) {
        this.repayId = repayId;
    }
}
