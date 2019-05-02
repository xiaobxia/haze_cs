package com.info.back.vo;

import java.math.BigDecimal;

/**
 * Created by cqry_2016 on 2018/4/12
 */
public class PerformanceTotalResult {
    private Integer totalSucPrinc;
    private Integer totalRenewalCount;
    private Integer totalFee;
    private Integer totalInOrder;
    private Integer totalSucOrder;
    private Integer totalSysOrder;
    private Integer totalHandOrder;
    private double sucRate;

    public Integer getTotalSucPrinc() {
        return totalSucPrinc;
    }

    public void setTotalSucPrinc(Integer totalSucPrinc) {
        this.totalSucPrinc = totalSucPrinc;
    }

    public Integer getTotalRenewalCount() {
        return totalRenewalCount;
    }

    public void setTotalRenewalCount(Integer totalRenewalCount) {
        this.totalRenewalCount = totalRenewalCount;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getTotalInOrder() {
        return getTotalSysOrder() + getTotalHandOrder();
    }

    public void setTotalInOrder(Integer totalInOrder) {
        this.totalInOrder = totalInOrder;
    }

    public Integer getTotalSucOrder() {
        return totalSucOrder;
    }

    public void setTotalSucOrder(Integer totalSucOrder) {
        this.totalSucOrder = totalSucOrder;
    }

    public double getSucRate() {
        if(this.getTotalInOrder() > 0 && this.getTotalSucOrder() > 0) {
            sucRate = new BigDecimal(this.getTotalSucOrder()).divide(new BigDecimal(this.getTotalInOrder()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue();
        }
        return sucRate;
    }

    public void setSucRate(double sucRate) {
        this.sucRate = sucRate;
    }

    public Integer getTotalSysOrder() {
        return totalSysOrder == null ? 0 : totalSysOrder;
    }

    public void setTotalSysOrder(Integer totalSysOrder) {
        this.totalSysOrder = totalSysOrder;
    }

    public Integer getTotalHandOrder() {
        return totalHandOrder == null ? 0 : totalHandOrder;
    }

    public void setTotalHandOrder(Integer totalHandOrder) {
        this.totalHandOrder = totalHandOrder;
    }
}
