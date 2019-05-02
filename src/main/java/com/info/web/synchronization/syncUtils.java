package com.info.web.synchronization;

import com.info.back.dao.ILocalDataDao;
import com.info.config.PayContents;
import com.info.web.pojo.cspojo.AuditCenter;
import com.info.web.pojo.cspojo.CreditLoanPay;
import com.info.web.util.HttpUtil;
import com.info.web.util.encrypt.AesUtil;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;

@Slf4j
public class syncUtils {


    /**
     * 检验是否存在减免状态并做出相应减免
     */
    public static void checkReduction(HashMap<String, Object> repaymentMap, ILocalDataDao localDataDao) {
        String payId = String.valueOf(repaymentMap.get("id"));
        //应还金额
        int receivableMoney = Integer.parseInt(String.valueOf(repaymentMap.get("repayment_amount")));
        //已还金额
        int realMoney = Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount")));

        try {
            log.info("start-reduction-payId:" + payId);
            AuditCenter auditCenter = localDataDao.getAuditCenterByPayId(payId);
            if (auditCenter != null) {

                if ("3".equals(auditCenter.getType()) && ("2".equals(auditCenter.getStatus()) || "5".equals(auditCenter.getStatus()))) {
                    //减免金额
                    int reductionMoney = auditCenter.getReductionMoney().intValue() * 100;
                    if (realMoney >= receivableMoney - reductionMoney && realMoney < receivableMoney) {
                        reductionMoney = receivableMoney - realMoney;

                        String sign = Md5coding.md5(AesUtil.encrypt(auditCenter.getLoanUserId() + auditCenter.getPayId() + reductionMoney + auditCenter.getId(), PayContents.XJX_WITHHOLDING_NOTIFY_KEY));
                        //2、发起请求
                        String withholdPostUrl = PayContents.XJX_JIANMIAN_URL + "/" + auditCenter.getLoanUserId() + "/" + auditCenter.getPayId() + "/" + reductionMoney + "/" + auditCenter.getId() + "/" + sign;

                        String xjxWithholdingStr = HttpUtil.getHttpMess(withholdPostUrl, "", "POST", "UTF-8");

                        log.info(xjxWithholdingStr);

                        log.info("payId:" + payId + "需要减免的金额：" + reductionMoney);
                    } else if (realMoney == receivableMoney) {
                        log.info("payId:" + payId + "钱已还完，无法减免");
                    } else {
                        log.info("未到减免资格，无法减免");
                    }
                    log.info("end-reduction-payId:" + payId + "redcutionMoney=" + reductionMoney);
                }
            }

        } catch (Exception e) {
            log.error("payId:" + payId, e);
        }
    }

    /**
     * 减免罚息    如果还款类型为6，还款状态2.则更新还款表的 减免滞纳金金额
     *
     * @param repayDetail 从库还款详情
     * @param id          还款id
     */
    public static void reductionmoney(HashMap<String, Object> repayDetail, String id, ILocalDataDao localDataDao) {
        int repaymentType = Integer.valueOf(String.valueOf(repayDetail.get("repayment_type")));
        int status = Integer.valueOf(String.valueOf(repayDetail.get("status")));
        if (repaymentType == 6 && status == 2) {
            CreditLoanPay creditLoanPay = new CreditLoanPay();
            //还款金额，从库中传过来
            int reductionMoney = Integer.parseInt(String.valueOf(repayDetail.get("true_repayment_money")));
            creditLoanPay.setId(id);
            creditLoanPay.setReductionMoney(new BigDecimal(reductionMoney / 100.00));
            localDataDao.updateCreditLoanPay(creditLoanPay);
        }
    }

}
