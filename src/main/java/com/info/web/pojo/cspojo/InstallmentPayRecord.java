package com.info.web.pojo.cspojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
@Data
public class InstallmentPayRecord implements Serializable {
    private String id;

    /**
     * 还款批次
     */
    private String repayBatches;

    /**
     * 应还时间
     */
    private Date repayTime;

    /**
     * 应还金额
     */
    private BigDecimal repayMoney;

    /**
     * 还款状态（0：还款成功  1：逾期未还）
     */
    private String repayStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 借款订单id（mman_loan_collection_order的主键）
     */
    private String loanOrderId;

    /**
     * 借款人姓名
     */
    private String loanUserName;

    /**
     * 借款人电话
     */
    private String loanUserPhone;

    /**
     * 操作状态（0：代扣，1：无代扣）
     */
    private String operationStatus;

    private String dateNew;

    private static final long serialVersionUID = 1L;

}