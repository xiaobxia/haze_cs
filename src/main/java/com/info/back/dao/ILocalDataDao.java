package com.info.back.dao;

import com.info.web.pojo.cspojo.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ILocalDataDao {

    /**
     * 验证是否重复插入
     */
    int checkLoan(HashMap<String, String> map);

    /**
     * 验证用户是否存在
     */
    int checkUserInfo(HashMap<String, String> map);

    /**
     * 验证用户联系人是否存在
     */
    int checkUserRela(HashMap<String, String> map);

    /**
     * 保存借款表
     */
    void saveMmanUserLoan(MmanUserLoan mmanUserLoan);

    /**
     * 更新借款表
     */
    void updateMmanUserLoan(MmanUserLoan mmanUserLoan);

    /**
     * 保存还款表
     */
    void saveCreditLoanPay(CreditLoanPay creditLoanPay);

    /**
     * 更新还款表
     */
    void updateCreditLoanPay(CreditLoanPay creditLoanPay);

    /**
     * 删除还款详情表
     */
    void delCreditLoanPayDetail(HashMap<String, String> map);

    /**
     * 从查询还款详情表
     */
    List<String> selectCreditLoanPayDetail(HashMap<String, String> map);

    /**
     * 查询order
     */
    HashMap<String, Object> selectOrderByDetail(HashMap<String, String> map);

    /**
     * 保存还款详情表
     */
    void saveCreditLoanPayDetail(CreditLoanPayDetail creditLoanPayDetail);

    /**
     * 保存用户信息
     */
    void saveMmanUserInfo(HashMap<String, Object> userInfo);

    /**
     * 保存用户联系人
     */
    void saveMmanUserRela(MmanUserRela mmanUserRela);

    /**
     * 保存银行卡信息
     */
    void saveSysUserBankCard(SysUserBankCard ysUserBankCard);

    /**
     * 更新银行卡
     */
    void updateSysUserBankCard(SysUserBankCard ysUserBankCard);

    /**
     * 更新order状态
     */
    void updateOrderStatus(HashMap<String, Object> map);

    /**
     * 更新代扣记录
     */
    void updateWithHold(HashMap<String, String> map);

    /**
     * 保存流转日志
     */
    void saveLoanChangeLog(MmanLoanCollectionStatusChangeLog loanChangeLog);

    /**
     * 查询订单状态
     */
    MmanLoanCollectionOrder selectLoanOrder(HashMap<String, Object> map);

    /**
     * 查询催收员
     */
    BackUser selectBackUser(HashMap<String, Object> map);

    /**
     * 计算当前payId的还款详情实收本金总额和实收罚息总额
     */
    CreditLoanPaySum sumRealMoneyAndPenlty(String payId);

    /**
     * 获取当前还款详情表count
     */
    int getDetailCount(String payId);

    /**
     * 根据payId获取审核信息 3-24
     */
    AuditCenter getAuditCenterByPayId(String payId);

}
