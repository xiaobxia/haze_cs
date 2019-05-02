package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
@Data
public class InstallmentPayInfoVo {

    /** 分期类型 */
    private String installmentType;
    /** 还款时间 */
    private Date repayTime;
    /** 还款总计 */
    private BigDecimal totalRepay;
    /** 服务费 */
    private BigDecimal serviceCharge;
    /** 分期本金 */
    private BigDecimal stagesOwnMoney;
    /** 分期滞纳金 */
    private BigDecimal stagesOverdueMoney;

}
