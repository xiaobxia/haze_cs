package com.info.back.vo;

import com.info.web.util.DateUtil;

import java.util.Date;

/**
 * Created by cqry_2016 on 2018/4/10
 */
public class PerformanceResult {
    private String csName;
    private String mobile;
    private Integer amount;
    private Integer totalFee;
    private Date countDate;
    private Integer overdueDays;
    private String groupLevel;
    private Integer reductionMoney = 0;
    private String mapK;
    private Integer[] mapV;

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTotalFee() {
        return overdueDays * 30;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Date getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }

    public String getMapK() {
        return String.join("", DateUtil.getDateFormat(countDate, "yyyy-MM-dd"), csName);
    }

    public void setMapK(String mapK) {
        this.mapK = mapK;
    }

    public Integer[] getMapV() {
        return new Integer[]{amount, getTotalFee()};
    }

    public void setMapV(Integer[] mapV) {
        this.mapV = mapV;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Integer getReductionMoney() {
        return reductionMoney;
    }

    public void setReductionMoney(Integer reductionMoney) {
        this.reductionMoney = reductionMoney;
    }
}
