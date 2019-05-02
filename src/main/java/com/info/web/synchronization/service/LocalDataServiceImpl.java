package com.info.web.synchronization.service;

import com.info.back.dao.*;
import com.info.back.exception.BusinessException;
import com.info.back.service.TaskJobMiddleService;
import com.info.back.utils.BackConstant;
import com.info.back.utils.IdGen;
import com.info.back.vo.CollectionSucRecord;
import com.info.back.vo.OrderLifeCycle;
import com.info.config.PayContents;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.*;
import com.info.web.synchronization.dao.IDataDao;
import com.info.web.synchronization.syncUtils;
import com.info.web.util.BussinessLogUtil;
import com.info.web.util.DateUtil;
import com.info.web.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LocalDataServiceImpl implements ILocalDataService {


    @Resource
    private ILocalDataDao localDataDao;
    @Resource
    private IDataDao dataDao;
    @Resource
    private TaskJobMiddleService taskJobMiddleService;
    @Resource
    private IBackUserDao backUserDao;

    @Resource
    private ICollectionSucRecordDao sucRecordDao;

    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;

    @Resource
    private IOrderLifeCycleDao orderLifeCycleDao;

    @Resource
    private ICreditLoanPayDao creditLoanPayDao;

    @Resource
    private IAuditCenterDao auditCenterDao;

    /**
     * 保存数据到本地
     */
    @Override
    public void saveDateToLocal(HashMap<String, Object> repaymentMap, HashMap<String, Object> userInfo,
                                HashMap<String, Object> borrowOrder, HashMap<String, Object> cardInfo,
                                List<HashMap<String, Object>> userContactsList, List<HashMap<String, Object>> repaymentDetalList) {
        if (null != repaymentMap) {
            //还款id
            String id = String.valueOf(repaymentMap.get("id"));
            //userid
            String userId = String.valueOf(repaymentMap.get("user_id"));
            //orderid借款id
            String assetOrderId = String.valueOf(repaymentMap.get("asset_order_id"));
            if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(assetOrderId)) {
                //保存用户借款表
                log.info("saveMmanUserLoan-执行时间start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                saveMmanUserLoan(borrowOrder, repaymentMap);
                log.info("saveMmanUserLoan-执行时间end" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));

                //保存用户还款表
                log.info("saveCreditLoanPay-执行时间start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                saveCreditLoanPay(repaymentMap);
                log.info("saveCreditLoanPay-执行时间end" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));

                if (CollectionUtils.isNotEmpty(repaymentDetalList)) {
                    //保存用户还款详情表
                    log.info("saveCreditLoanPayDetail-执行时间start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                    saveCreditLoanPayDetail(repaymentMap, id, repaymentDetalList);
                    log.info("saveCreditLoanPayDetail-执行时间end" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
//
                //保存用户表 用户信息不存在
                if (checkUserInfo(userId)) {
                    log.info("saveMmanUserInfo-start借款loanId-" + assetOrderId + "执行时间start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                    String userFrom = String.valueOf(userInfo.get("user_from") == null ? "" : userInfo.get("user_from").toString());
                    if (StringUtils.isEmpty(userFrom)) {
                        userFrom = "0";
                    }
                    userInfo.put("user_from", userFrom);
                    //保存用户信息
                    this.localDataDao.saveMmanUserInfo(userInfo);
                    log.info("saveMmanUserInfo-借款loanId-" + assetOrderId + "执行时间end" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                    //保存通讯录
                    log.info("saveMmanUserRela-start借款loanId-" + assetOrderId + "执行时间start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                    saveMmanUserRela(userInfo, userContactsList);
                    log.info("saveMmanUserRela-执行时间end" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                    //保存用户银行卡
                    log.info("saveSysUserBankCard-start借款loanId-" + assetOrderId + "执行时间start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                    saveSysUserBankCard(cardInfo);
                    log.info("saveSysUserBankCard-执行时间end" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                } else {
                    //通讯录不存在
                    if (checkUserReal(userId)) {
                        saveMmanUserRela(userInfo, userContactsList);
                    }
                    //更新银行卡
                    updateSysUserBankCard(cardInfo);
                }
            }
        }
    }

    /**
     * 更新本地数据-逾期
     */
    @Override
    public void updateDateToLocalForOverdue(HashMap<String, Object> repaymentMap, List<HashMap<String, Object>> repaymentDetalList, HashMap<String, Object> borrowOrder) {
        if (null != repaymentMap) {
            //还款id
            String id = String.valueOf(repaymentMap.get("id"));
            //orderid借款id
            String assetOrderId = String.valueOf(repaymentMap.get("asset_order_id"));
            HashMap<String, Object> map = new HashMap<>(3);
            map.put("ORDER_ID", assetOrderId);
            map.put("USER_ID", repaymentMap.get("user_id"));
            MmanLoanCollectionOrder order = this.localDataDao.selectLoanOrder(map);
            if (null != order) {
                //更新用户还款表
                updateCreditLoanPay(repaymentMap);
                //保存用户还款详情表
                saveCreditLoanPayDetail(repaymentMap, id, repaymentDetalList);
                log.info("repaymentMap: {}", repaymentMap);
                BigDecimal realMoney = new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount"))) / 100.00);
                //更新用户借款表 逾期
                updateMmanUserLoan(borrowOrder, repaymentMap, Constant.STATUS_OVERDUE_FOUR);
                map.put("REAL_MONEY", realMoney);
                this.localDataDao.updateOrderStatus(map);
            }
        }
    }

    /**
     * 保存用户借款表
     */
    @Override
    public void saveMmanUserLoan(HashMap<String, Object> borrowOrder, HashMap<String, Object> repaymentMap) {
        MmanUserLoan mmanUserLoan = new MmanUserLoan();
        mmanUserLoan.setId(String.valueOf(borrowOrder.get("id")));
        mmanUserLoan.setUserId(String.valueOf(borrowOrder.get("user_id")));
        //第三方订单号
        mmanUserLoan.setLoanPyId(String.valueOf(borrowOrder.get("out_trade_no")));
        mmanUserLoan.setLoanMoney(new BigDecimal(Integer.parseInt(String.valueOf(borrowOrder.get("money_amount"))) / 100));
        mmanUserLoan.setLoanRate(String.valueOf(borrowOrder.get("apr")));
        mmanUserLoan.setLoanPenalty(new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("plan_late_fee"))) / 100));
        mmanUserLoan.setLoanPenaltyRate(String.valueOf(repaymentMap.get("late_fee_apr")));
        mmanUserLoan.setLoanStatus(Constant.STATUS_OVERDUE_FOUR);
        //getDateTimeFormat
        mmanUserLoan.setLoanEndTime(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        mmanUserLoan.setLoanStartTime(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("credit_repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        mmanUserLoan.setCreateTime(new Date());
        mmanUserLoan.setUpdateTime(new Date());
        //0正常1：删除
        mmanUserLoan.setDelFlag("0");
        this.localDataDao.saveMmanUserLoan(mmanUserLoan);
    }

    /**
     * 更新用户借款表
     */
    @Override
    public void updateMmanUserLoan(HashMap<String, Object> borrowOrder, HashMap<String, Object> repaymentMap, String status) {
        MmanUserLoan mmanUserLoan = new MmanUserLoan();
        mmanUserLoan.setId(String.valueOf(borrowOrder.get("id")));
        mmanUserLoan.setUserId(String.valueOf(borrowOrder.get("user_id")));
        //第三方订单号
        mmanUserLoan.setLoanPyId(String.valueOf(borrowOrder.get("out_trade_no")));
        mmanUserLoan.setLoanMoney(new BigDecimal(Integer.parseInt(String.valueOf(borrowOrder.get("money_amount"))) / 100));
        mmanUserLoan.setLoanRate(String.valueOf(borrowOrder.get("apr")));
        mmanUserLoan.setLoanPenalty(new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("plan_late_fee"))) / 100));
        mmanUserLoan.setLoanPenaltyRate(String.valueOf(repaymentMap.get("late_fee_apr")));
        mmanUserLoan.setLoanStatus(status);
        //getDateTimeFormat
        mmanUserLoan.setLoanEndTime(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        mmanUserLoan.setLoanStartTime(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("credit_repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        mmanUserLoan.setUpdateTime(new Date());
        this.localDataDao.updateMmanUserLoan(mmanUserLoan);
    }

    /**
     * 保存用户还款表
     */
    @Override
    public void saveCreditLoanPay(HashMap<String, Object> repaymentMap) {
        CreditLoanPay creditLoanPay = new CreditLoanPay();
        creditLoanPay.setId(String.valueOf(repaymentMap.get("id")));
        creditLoanPay.setLoanId(String.valueOf(repaymentMap.get("asset_order_id")));
        creditLoanPay.setReceivableStartdate(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("credit_repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        //应还时间
        creditLoanPay.setReceivableDate(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        creditLoanPay.setReceivableMoney(new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("repayment_amount"))) / 100));
        creditLoanPay.setRealMoney(new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount"))) / 100));
        //还款状态
        creditLoanPay.setStatus(getPayStatus(String.valueOf(repaymentMap.get("status"))));
        creditLoanPay.setCreateDate(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("created_at")), "yyyy-MM-dd HH:mm:ss"));
        creditLoanPay.setUpdateDate(new Date());
        creditLoanPay = operaRealPenlty(repaymentMap, creditLoanPay);
        this.localDataDao.saveCreditLoanPay(creditLoanPay);
    }

    /**
     * 更新用户还款表
     */
    @Override
    public void updateCreditLoanPay(HashMap<String, Object> repaymentMap) {
        CreditLoanPay creditLoanPay = new CreditLoanPay();
        creditLoanPay.setId(String.valueOf(repaymentMap.get("id")));
        creditLoanPay.setLoanId(String.valueOf(repaymentMap.get("asset_order_id")));
        creditLoanPay.setReceivableStartdate(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("credit_repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        //应还时间
        creditLoanPay.setReceivableDate(DateUtil.getDateTimeFormat(String.valueOf(repaymentMap.get("repayment_time")), "yyyy-MM-dd HH:mm:ss"));
        creditLoanPay.setReceivableMoney(new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("repayment_amount"))) / 100.00));
        creditLoanPay.setRealMoney(new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount"))) / 100.00));
        //还款状态
        creditLoanPay.setStatus(getPayStatus(String.valueOf(repaymentMap.get("status"))));
        creditLoanPay.setUpdateDate(new Date());
        creditLoanPay = operaRealPenlty(repaymentMap, creditLoanPay);
        this.localDataDao.updateCreditLoanPay(creditLoanPay);
    }

    /**
     * 保存用户还款详情表
     */
    @Override
    public void saveCreditLoanPayDetail(HashMap<String, Object> repayment, String id, List<HashMap<String, Object>> repaymentDetalList) {
        List<String> idList = null;
        if (CollectionUtils.isNotEmpty(repaymentDetalList)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("PAY_ID", id);
            //查询目前插入的还款记录
            idList = this.localDataDao.selectCreditLoanPayDetail(map);
        }
        CreditLoanPayDetail creditLoanPayDetail;
        HashMap<String, Integer> amountMap = getAmountToMap(repayment);
        for (HashMap<String, Object> repayDetail : repaymentDetalList) {
            String detailId = String.valueOf(repayDetail.get("id"));
            //不存在则插入
            if (checkDetailId(idList, detailId)) {
                //减免罚息    如果还款类型为6，还款状态2.则更新还款表的 减免金额
                syncUtils.reductionmoney(repayDetail, id, localDataDao);

                creditLoanPayDetail = new CreditLoanPayDetail();
                creditLoanPayDetail.setId(detailId);
                creditLoanPayDetail.setPayId(id);
                creditLoanPayDetail.setCreateDate(DateUtil.getDateTimeFormat(String.valueOf(repayDetail.get("created_at")), "yyyy-MM-dd HH:mm:ss"));
                creditLoanPayDetail.setUpdateDate(new Date());
                creditLoanPayDetail.setReturnType(String.valueOf(repayDetail.get("repayment_type")));
                creditLoanPayDetail.setRemark(String.valueOf(repayDetail.get("remark")));
                creditLoanPayDetail = operaRealPenltyDetail(amountMap, repayDetail, id, creditLoanPayDetail);
                this.localDataDao.saveCreditLoanPayDetail(creditLoanPayDetail);
            }
        }
    }

    /**
     * 验证id是否存在list
     */
    @Override
    public boolean checkDetailId(List<String> idList, String id) {
        boolean notExist = true;
        if (CollectionUtils.isNotEmpty(idList)) {
            for (String anIdList : idList) {
                if (id.equals(anIdList)) {
                    notExist = false;
                }
            }
        }
        return notExist;
    }

    /**
     * 保存用户通讯录
     */
    @Override
    public void saveMmanUserRela(HashMap<String, Object> userInfo, List<HashMap<String, Object>> userContactsList) {
        String userId = String.valueOf(userInfo.get("id"));
        MmanUserRela mmanUserRela;
        //保存user表中的联系人1
        mmanUserRela = new MmanUserRela();
        String phoneNumO = String.valueOf(userInfo.get("first_contact_phone"));
        mmanUserRela.setId(IdGen.uuid());
        mmanUserRela.setUserId(userId);
        mmanUserRela.setContactsKey("1");
        mmanUserRela.setInfoName(String.valueOf(userInfo.get("first_contact_name")));
        mmanUserRela.setInfoValue(phoneNumO);
        //联系人关系
        mmanUserRela.setRelaKey(String.valueOf(userInfo.get("frist_contact_relation")));
        //删除标记（''1''-已删除，''0''正常）
        mmanUserRela.setDelFlag("0");
        this.localDataDao.saveMmanUserRela(mmanUserRela);

        //保存user表中的联系人2
        mmanUserRela = new MmanUserRela();
        String phoneNumT = String.valueOf(userInfo.get("second_contact_phone"));
        mmanUserRela.setId(IdGen.uuid());
        mmanUserRela.setUserId(userId);
        mmanUserRela.setContactsKey("2");
        mmanUserRela.setInfoName(String.valueOf(userInfo.get("second_contact_name")));
        mmanUserRela.setInfoValue(phoneNumT);
        //联系人关系
        mmanUserRela.setRelaKey(String.valueOf(userInfo.get("second_contact_relation")));
        //删除标记（''1''-已删除，''0''正常）
        mmanUserRela.setDelFlag("0");
        this.localDataDao.saveMmanUserRela(mmanUserRela);

        for (HashMap<String, Object> anUserContactsList : userContactsList) {
            String phone = String.valueOf(anUserContactsList.get("contact_phone"));
            mmanUserRela = new MmanUserRela();
            mmanUserRela.setId(IdGen.uuid());
            mmanUserRela.setUserId(userId);
            mmanUserRela.setInfoName(String.valueOf(anUserContactsList.get("contact_name")));
            mmanUserRela.setInfoValue(phone);
            //删除标记（''1''-已删除，''0''正常）
            mmanUserRela.setDelFlag("0");
            this.localDataDao.saveMmanUserRela(mmanUserRela);
        }
    }

    /**
     * 保存用户银行卡
     */
    @Override
    public void saveSysUserBankCard(HashMap<String, Object> cardInfo) {
        SysUserBankCard bankCard = new SysUserBankCard();
        bankCard.setId(IdGen.uuid());
        bankCard.setUserId(String.valueOf(cardInfo.get("user_id")));
        bankCard.setBankCard(String.valueOf(cardInfo.get("card_no")));
        bankCard.setDepositBank(String.valueOf(cardInfo.get("bank_name")));
        bankCard.setBankInstitutionNo(String.valueOf(cardInfo.get("bank_id")));
        bankCard.setName(String.valueOf(cardInfo.get("open_name")));
        bankCard.setMobile(String.valueOf(cardInfo.get("phone")));
        bankCard.setCityName(String.valueOf(cardInfo.get("bank_address")));
        this.localDataDao.saveSysUserBankCard(bankCard);
    }

    /**
     * 更新银行卡
     */
    @Override
    public void updateSysUserBankCard(HashMap<String, Object> cardInfo) {
        SysUserBankCard bankCard = new SysUserBankCard();
        bankCard.setUserId(String.valueOf(cardInfo.get("user_id")));
        bankCard.setBankCard(String.valueOf(cardInfo.get("card_no")));
        bankCard.setDepositBank(String.valueOf(cardInfo.get("bank_name")));
        bankCard.setBankInstitutionNo(String.valueOf(cardInfo.get("bank_id")));
        bankCard.setName(String.valueOf(cardInfo.get("open_name")));
        bankCard.setMobile(String.valueOf(cardInfo.get("phone")));
        bankCard.setCityName(String.valueOf(cardInfo.get("bank_address")));
        this.localDataDao.updateSysUserBankCard(bankCard);
    }

    /**
     * 验证用户是否存在
     */
    private boolean checkUserInfo(String id) {
        if (StringUtils.isNotBlank(id)) {
            HashMap<String, String> map = new HashMap<>(1);
            map.put("ID", id);
            int count = this.localDataDao.checkUserInfo(map);
            return count == 0;
        }
        return false;
    }

    /**
     * 验证用户联系人是否存在
     */
    private boolean checkUserReal(String id) {
        if (StringUtils.isNotBlank(id)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ID", id);
            int count = this.localDataDao.checkUserRela(map);
            return count == 0;
        }
        return false;
    }

    /**
     * 验证是否重复入库or是否存在
     */
    @Override
    public boolean checkLoan(String id) {
        if (StringUtils.isNotBlank(id)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ID", id);
            int count = this.localDataDao.checkLoan(map);
            return count == 0;
        }
        return false;
    }

    /**
     * 获取状态
     * 还款状态（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+）
     * public static final int CREDITLOANPAY_OVERDUEA = 3;// 逾期1到10天S1
     * public static final int CREDITLOANPAY_OVERDUEB = 4;// 逾期11到30天S2
     * public static final int CREDITLOANPAY_OVERDUEC = 5;// 逾期31到60天M1-M2
     * public static final int CREDITLOANPAY_OVERDUED = 6;// 逾期61到90天M2-M3
     * public static final int CREDITLOANPAY_OVERDUEE = 7;// 逾期大于90天 M3+
     * public static final int CREDITLOANPAY_OVERDUE_UNCOMPLETE = 8;// 续期（该状态催收员不能操作）
     * public static final int CREDITLOANPAY_COMPLETE = 2;// 已还款
     */
    @Override
    public int getPayStatus(String status) {
        if (Constant.STATUS_HKZ.equals(status)) {
            return Constant.CREDITLOANPAY_OVERDUE_UNCOMPLETE;
        } else if (Constant.STATUS_YYQ.equals(status) || Constant.STATUS_YHZ.equals(status)) {
            return Constant.CREDITLOANPAY_OVERDUEA;
        } else {
            return Constant.CREDITLOANPAY_COMPLETE;
        }
    }

    /**
     * 获取相应金额
     */
    @Override
    public HashMap<String, Integer> getAmountToMap(HashMap<String, Object> repaymentMap) {
        HashMap<String, Integer> map = new HashMap<>(5);
        //总还款金额
        int repaymentAmount = Integer.parseInt(String.valueOf(repaymentMap.get("repayment_amount")));
        map.put("repaymentAmount", repaymentAmount);
        //滞纳金
        int planLateFee = Integer.parseInt(String.valueOf(repaymentMap.get("plan_late_fee")));
        map.put("planLateFee", planLateFee);
        //已还款金额
        int repaymentedAmount = Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount")));
        map.put("repaymentedAmount", repaymentedAmount);
        // 应还本金
        int receivablePrinciple = repaymentAmount - planLateFee;
        map.put("receivablePrinciple", receivablePrinciple);
        // 实收的利息 = 已还金额 - 应还本金
        int realPenlty = repaymentedAmount - receivablePrinciple;
        map.put("realPenlty", realPenlty);
        return map;
    }

    /**
     * 计算还款
     */
    @Override
    public CreditLoanPay operaRealPenlty(HashMap<String, Object> repaymentMap, CreditLoanPay creditLoanPay) {
        //总还款金额
        int repaymentAmount = Integer.parseInt(String.valueOf(repaymentMap.get("repayment_amount")));
        //滞纳金
        int planLateFee = Integer.parseInt(String.valueOf(repaymentMap.get("plan_late_fee")));
        //已还款金额
        int repaymentedAmount = Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount")));
        // 应还本金
        int receivablePrinciple = repaymentAmount - planLateFee;

        // 实收的利息 = 已还金额 - 应还本金
        int realPenlty = repaymentedAmount - receivablePrinciple;

        if (realPenlty <= 0) {
            //剩余应还本金
            creditLoanPay.setReceivablePrinciple(new BigDecimal((receivablePrinciple - repaymentedAmount) / 100.00));
            //剩余应还罚息
            creditLoanPay.setReceivableInterest(new BigDecimal(planLateFee / 100.00));
            //实收本金
            creditLoanPay.setRealgetPrinciple(new BigDecimal(repaymentedAmount / 100.00));
            //实收罚息
            creditLoanPay.setRealgetInterest(new BigDecimal(0));
        } else {
            creditLoanPay.setReceivablePrinciple(new BigDecimal(0));
            creditLoanPay.setReceivableInterest(new BigDecimal((repaymentAmount - repaymentedAmount) / 100.00));
            creditLoanPay.setRealgetPrinciple(new BigDecimal((repaymentedAmount - realPenlty) / 100.00));
            creditLoanPay.setRealgetInterest(new BigDecimal(realPenlty / 100.00));
        }
        return creditLoanPay;
    }

    /**
     * 计算还款详情
     */
    @Override
    public CreditLoanPayDetail operaRealPenltyDetail(HashMap<String, Integer> amountMap, HashMap<String, Object> repayDetail, String payId, CreditLoanPayDetail creditLoanPayDetail) {
        //总还款金额
        int repaymentAmount = amountMap.get("repaymentAmount");
        //滞纳金
        int planLateFee = amountMap.get("planLateFee");
        // 应还本金
        int receivablePrinciple = repaymentAmount - planLateFee;
        //已还款金额，从库中传过来
        int trueRepaymentMoney = Integer.parseInt(String.valueOf(repayDetail.get("true_repayment_money")));
        //还款详情表中payId还款条数
        int detailCount = localDataDao.getDetailCount(payId);

        if (detailCount == 0) {
            if (trueRepaymentMoney >= receivablePrinciple) {
                //实收本金
                creditLoanPayDetail.setRealMoney(new BigDecimal(receivablePrinciple / 100.00));
                //实收罚息
                int realPenlty = trueRepaymentMoney - receivablePrinciple;
                creditLoanPayDetail.setRealPenlty(new BigDecimal(realPenlty / 100.00));
                creditLoanPayDetail.setRealPrinciple(new BigDecimal(0));
                int realInterest = planLateFee - realPenlty;
                creditLoanPayDetail.setRealInterest(new BigDecimal(realInterest / 100.00));
            } else {
                creditLoanPayDetail.setRealMoney(new BigDecimal(trueRepaymentMoney / 100.00));
                creditLoanPayDetail.setRealPenlty(new BigDecimal(0));
                creditLoanPayDetail.setRealPrinciple(new BigDecimal((receivablePrinciple - trueRepaymentMoney) / 100.00));
                creditLoanPayDetail.setRealInterest(new BigDecimal(planLateFee / 100.00));
            }

        } else {
            CreditLoanPaySum creditLoanPaySum = localDataDao.sumRealMoneyAndPenlty(payId);
            //总实收本金
            int sumRealMoney = creditLoanPaySum.getSumRealMoney().intValue() * 100;
            //总实收罚息
            int sumRealPenalty = creditLoanPaySum.getSumRealPenlty().intValue() * 100;
            if (sumRealMoney >= receivablePrinciple) {
                creditLoanPayDetail.setRealMoney(new BigDecimal(0));
                creditLoanPayDetail.setRealPenlty(new BigDecimal(trueRepaymentMoney / 100.00));
                creditLoanPayDetail.setRealPrinciple(new BigDecimal(0));
                creditLoanPayDetail.setRealInterest(new BigDecimal((planLateFee - sumRealPenalty - trueRepaymentMoney) / 100.00));
            } else {

                if ((sumRealMoney + trueRepaymentMoney) < receivablePrinciple) {
                    creditLoanPayDetail.setRealMoney(new BigDecimal(trueRepaymentMoney / 100));
                    creditLoanPayDetail.setRealPenlty(new BigDecimal(0));
                    //剩余应还本金
                    int realPrinciple = receivablePrinciple - (sumRealMoney + trueRepaymentMoney);
                    creditLoanPayDetail.setRealPrinciple(new BigDecimal(realPrinciple / 100.00));
                    creditLoanPayDetail.setRealInterest(new BigDecimal(planLateFee / 100.00));
                } else {
                    //实收罚息
                    int realPenlty = sumRealMoney + trueRepaymentMoney - receivablePrinciple;
                    creditLoanPayDetail.setRealMoney(new BigDecimal((trueRepaymentMoney - realPenlty) / 100.00));
                    creditLoanPayDetail.setRealPenlty(new BigDecimal(realPenlty / 100.00));
                    creditLoanPayDetail.setRealPrinciple(new BigDecimal(0));
                    creditLoanPayDetail.setRealInterest(new BigDecimal((planLateFee - realPenlty) / 100.00));
                }
            }

        }
        return creditLoanPayDetail;
    }


    @Override
    public void dealOverdue(Map<String, String> callBackParams, String repaymentId) {
        HashMap<String, String> paramMap = new HashMap<>(1);
        try {
            paramMap.put("ID", repaymentId);
            HashMap<String, Object> repayment = this.dataDao.getAssetRepayment(paramMap);
            if (repayment == null) {
                throw new BusinessException("999", "dealOverdue error. repayment is null repaymentId: " + repaymentId);
            }
            String reId = String.valueOf(repayment.get("asset_order_id"));
            paramMap.put("ORDER_ID", reId);
            HashMap<String, Object> borrowOrder = this.dataDao.getAssetBorrowOrder(paramMap);
            if (borrowOrder == null) {
                throw new BusinessException("999", "dealOverdue error. borrowOrder is null repaymentId: " + repaymentId);
            }
            List<HashMap<String, Object>> repaymentDetalList = this.dataDao.getAssetRepaymentDetail(paramMap);

            if (checkLoan(reId)) {
                paramMap.clear();
                paramMap.put("USER_ID", String.valueOf(repayment.get("user_id")));
                HashMap<String, Object> userInfo = this.dataDao.getUserInfo(paramMap);
                if (userInfo == null) {
                    throw new BusinessException("999", "dealOverdue error. userInfo is null repaymentId: " + repaymentId);
                }
                HashMap<String, Object> cardInfo = this.dataDao.getUserCardInfo(paramMap);
                if (cardInfo == null) {
                    throw new BusinessException("999", "dealOverdue error. cardInfo is null repaymentId: " + repaymentId);
                }
                List<HashMap<String, Object>> userContactsList = this.dataDao.getUserContacts(paramMap);

                saveDateToLocal(repayment, userInfo, borrowOrder, cardInfo, userContactsList, repaymentDetalList);
                log.info("taskJobMiddleService dispatch time:" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                this.taskJobMiddleService.dispatchforLoanId(reId);
                //验证是否减免
                syncUtils.checkReduction(repayment, localDataDao);

            } else {
                MmanUserLoan loan = new MmanUserLoan();
                loan.setId(reId);
                MmanUserLoan mmanUserLoan = this.taskJobMiddleService.getRecord(loan);
                Date endTime = mmanUserLoan.getLoanEndTime();
                Date now = new Date();
                if (DateUtil.daysBetween(endTime, now) > 0 && Constant.STATUS_OVERDUE_THREE.equals(mmanUserLoan.getLoanStatus())) {
                    mmanUserLoan.setLoanStatus("4");
                    mmanUserLoan.setLoanSysRemark("续期后逾期");
                    this.localDataDao.updateMmanUserLoan(mmanUserLoan);
                }

                if (null != repaymentDetalList) {
                    updateDateToLocalForOverdue(repayment, repaymentDetalList, borrowOrder);
                }
                //验证是否减免
                syncUtils.checkReduction(repayment, localDataDao);
            }
            callBackParams.put("status", "true");
        } catch (BusinessException be) {
            callBackParams.put("status", "false");
            log.error(be.getMessage());
        } catch (Exception e) {
            callBackParams.put("status", "false");
            log.error("dealOverdue repayId: {} error: {}", repaymentId, e);
            BussinessLogUtil.error("OperaOverdueDataThread dispatch error", "dispatchOrder", e);
        }
        if (callBackParams.size() > 1) {
            try {
                HttpUtil.doPost(PayContents.MQ_CALLBACK_URL, callBackParams);
            } catch (Exception e) {
                log.error("BusinessListener dopostMap error. {}", e);
            }
        }

    }

    @Override
    public void saveSucRecord(MmanLoanCollectionOrder order, HashMap<String, Object> repayment, HashMap<String, Object> borrowOrder
            , String status) {
        CollectionSucRecord record = new CollectionSucRecord();
        BackUser csUser = backUserDao.getBackUserByUuid(order.getCurrentCollectionUserId());
        record.setCollectName(csUser == null ? "" : csUser.getUserName());
        record.setCreateDate(new Date());
        record.setDispatchName(order.getDispatchName());
        record.setLateDay(order.getOverdueDays());
        record.setLateFee(new BigDecimal(Integer.parseInt(String.valueOf(repayment.get("plan_late_fee"))) / 100));
        record.setLoanId(Integer.valueOf(order.getLoanId()));
        record.setLoanMoney(new BigDecimal(Integer.parseInt(String.valueOf(borrowOrder.get("money_amount"))) / 100));
        record.setLoanName(order.getLoanUserName());
        record.setLoanTel(order.getLoanUserPhone());
        record.setLateDate(order.getCreateDate());
        if (BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(status)) {
            Object repayTimeObj = repayment.get("repayment_real_time");
            Object repayedMoney = repayment.get("repaymented_amount");
            if(repayTimeObj != null) {
                record.setRechargeDate(DateUtil.getDateTimeFormat(repayTimeObj.toString(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                record.setRechargeDate(new Date());
            }
            if(repayedMoney != null) {
                record.setRepayedAmount(new BigDecimal(repayedMoney.toString()));
            }
        } else {
            BigDecimal totalRenewalFee = BigDecimal.ZERO;
            Object loanInterest = repayment.get("repaymentInterest");
            Object renewalFee = repayment.get("renewalFee");
            Object lateFee = repayment.get("lateFee");
            if(loanInterest != null) {
                totalRenewalFee = totalRenewalFee.add(new BigDecimal(loanInterest.toString()));
            }
            if(renewalFee != null) {
                totalRenewalFee = totalRenewalFee.add(new BigDecimal(renewalFee.toString()));
            }
            if(lateFee != null) {
                totalRenewalFee = totalRenewalFee.add(new BigDecimal(lateFee.toString()));
            }
            record.setRepayedAmount(totalRenewalFee.setScale(2, BigDecimal.ROUND_HALF_UP));
            record.setRechargeDate(DateUtil.addDay(DateUtil.getDateTimeFormat(repayment.get("repayment_time").toString(), DateUtil.YMDHMS), -7));
        }
        record.setRechargeStatus(Integer.valueOf(status));
        record.setReducMoney(new BigDecimal(order.getReductionMoney()));
        sucRecordDao.insertSelective(record);
    }

    @Override
    public int getOrderCount(Map<String, Object> map) {
        return mmanLoanCollectionOrderDao.getOrderCount(map);
    }

    @Override
    public List<Integer> getLoanIds(Map<String, Object> map) {
        return mmanLoanCollectionOrderDao.getLoanIds(map);
    }


    @Override
    public void addOrderRenewCount(Map<String, Integer> dataMap) {
        mmanLoanCollectionOrderDao.updateOrderRenewCount(dataMap);
    }

    @Override
    public OrderLifeCycle findByLoanIdAndGroup(HashMap<String, Object> lifeMap) {
        return orderLifeCycleDao.findByLoanIdAndGroup(lifeMap);
    }

    @Override
    public void updateOrderLifeById(OrderLifeCycle orderLifeCycle) {
        orderLifeCycleDao.updateByPrimaryKeySelective(orderLifeCycle);
    }

    @Override
    public void dealRenewal(Map<String, String> callBackParams, String repaymentId) {
        HashMap<String, String> map = new HashMap<>(1);
        try {
            map.put("ID", repaymentId);
            HashMap<String, Object> repayment = this.dataDao.getAssetRepayment(map);
            log.info("repayment=" + repayment);
            if (repayment == null) {
                throw new BusinessException("999", "dealRenewal error. repayment is null repaymentId: " + repaymentId);
            }
            //还款id
            String reId = String.valueOf(repayment.get("asset_order_id"));
            if (!checkLoan(reId)) {
                map.clear();
                map.put("ORDER_ID", String.valueOf(repayment.get("asset_order_id")));
                HashMap<String, Object> borrowOrder = this.dataDao.getAssetBorrowOrder(map);
                log.info("borrowOrder=" + borrowOrder);
                map.clear();
                map.put("ID", repaymentId);
                List<HashMap<String, Object>> repaymentDetalList = this.dataDao.getAssetRepaymentDetail(map);
                log.info("repaymentDetalList=" + repaymentDetalList);
                if (null != borrowOrder) {
                    updateDateToLocalForRenewal(repayment, repaymentDetalList, borrowOrder);
                }
            }
            callBackParams.put("status", "true");
        } catch (BusinessException be) {
            callBackParams.put("status", "false");
            log.error(be.getMessage());
        } catch (Exception e) {
            callBackParams.put("status", "false");
            log.error("dealRenewal error: {}", e);
        }
        if (callBackParams.size() > 1) {
            try {
                HttpUtil.doPost(PayContents.MQ_CALLBACK_URL, callBackParams);
            } catch (Exception e) {
                log.error("RepayListener dopostMap error. repayId: {} ,{}", repaymentId, e);
            }
        }
    }


    /**
     * 更新本地数据-续期
     *
     * @param repaymentMap       还款map
     * @param repaymentDetalList 还款详情
     * @param borrowOrder        借款订单
     */
    private void updateDateToLocalForRenewal(HashMap<String, Object> repaymentMap, List<HashMap<String, Object>> repaymentDetalList, HashMap<String, Object> borrowOrder) {
        //还款id
        String id = String.valueOf(repaymentMap.get("id"));
        //orderid借款id
        String assetOrderId = String.valueOf(repaymentMap.get("asset_order_id"));
        //更新用户借款表
        updateMmanUserLoan(borrowOrder, repaymentMap, Constant.STATUS_OVERDUE_THREE);
        //更新用户还款表
        updateCreditLoanPay(repaymentMap);
        //保存用户还款详情表
        saveCreditLoanPayDetail(repaymentMap, id, repaymentDetalList);
        HashMap<String, Object> searchMap = new HashMap<>();
        searchMap.put("ORDER_ID", assetOrderId);
        searchMap.put("USER_ID", repaymentMap.get("user_id"));
        MmanLoanCollectionOrder order = this.localDataDao.selectLoanOrder(searchMap);

        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> lifeMap = new HashMap<>(2);
        BigDecimal realMoney = new BigDecimal(Integer.parseInt(String.valueOf(repaymentMap.get("repaymented_amount"))) / 100.00);
        map.put("STATUS", Constant.STATUS_OVERDUE_FIVE);
        map.put("ORDER_ID", assetOrderId);
        map.put("USER_ID", repaymentMap.get("user_id"));
        map.put("REAL_MONEY", realMoney);
        HashMap<String, Object> lateFeeMap = dataDao.selectRenewalRecord(Integer.valueOf(id));
        if (lateFeeMap.get("lateFee") != null && Integer.valueOf(lateFeeMap.get("lateFee").toString()) > 0) {
            map.put("repayRenewalMark", 1);
            repaymentMap.putAll(lateFeeMap);
            saveSucRecord(order, repaymentMap, borrowOrder, BackConstant.XJX_COLLECTION_ORDER_STATE_PAYING);
            //修改订单生命周期
            lifeMap.put("orderId", assetOrderId);
            lifeMap.put("group", order.getCurrentOverdueLevel());
            OrderLifeCycle lifeCycle = findByLoanIdAndGroup(lifeMap);
            if (lifeCycle != null) {
                lifeCycle.setCurrentLevel(new Byte(order.getCurrentOverdueLevel()));
                lifeCycle.setCurrentStatus(new Byte(Constant.STATUS_OVERDUE_FIVE));
                updateOrderLifeById(lifeCycle);
            }
        }
        this.localDataDao.updateOrderStatus(map);
    }


    @Override
    public void dealRepay(Map<String, String> callBackParams, String repayId) {
        HashMap<String, String> paramMap = new HashMap<>(1);
        try {
            paramMap.put("ID", repayId);
            HashMap<String, Object> repayment = this.dataDao.getAssetRepayment(paramMap);
            log.info("repayment=" + repayment);
            if (repayment == null) {
                throw new BusinessException("999", "dealRenewal error. repayment is null repaymentId: " + repayId);
            }
            String reId = String.valueOf(repayment.get("asset_order_id"));
            if (!checkLoan(reId)) {
                List<HashMap<String, Object>> repaymentDetails = this.dataDao.getAssetRepaymentDetail(paramMap);
                log.info("repaymentDetalList=" + repaymentDetails);

                paramMap.clear();
                paramMap.put("ORDER_ID", reId);
                HashMap<String, Object> borrowOrder = this.dataDao.getAssetBorrowOrder(paramMap);
                log.info("borrowOrder=" + borrowOrder);

                if (null != borrowOrder && null != repaymentDetails) {
                    updateDateToLocalForRepay(repayment, repaymentDetails, borrowOrder);
                }
            }
            callBackParams.put("status", "true");
        } catch (BusinessException be) {
            callBackParams.put("status", "false");
            log.error(be.getMessage());
        } catch (Exception e) {
            callBackParams.put("status", "false");
            log.error("dealRepay error: {}", e);
        }
        if (callBackParams.size() > 1) {
            try {
                HttpUtil.doPost(PayContents.MQ_CALLBACK_URL, callBackParams);
            } catch (Exception e) {
                log.error("RepayListener dopostMap error. repayId: {} ,{}", repayId, e);
            }
        }
    }

    /**
     * 更新本地数据-还款
     */
    private void updateDateToLocalForRepay(HashMap<String, Object> repayment, List<HashMap<String, Object>> repaymentDetalList,
                                           HashMap<String, Object> borrowOrder) {
        if (null != repayment) {
            String id = String.valueOf(repayment.get("id"));
            String assetOrderId = String.valueOf(repayment.get("asset_order_id"));
            HashMap<String, Object> forUpdateMap = new HashMap<>(5);
            forUpdateMap.put("ORDER_ID", assetOrderId);
            forUpdateMap.put("USER_ID", repayment.get("user_id"));
            MmanLoanCollectionOrder order = this.localDataDao.selectLoanOrder(forUpdateMap);
            if (order == null) {
                log.error("updateDateToLocalForRepay error. order is null assetOrderId: {}", assetOrderId);
                return;
            }
            //更新用户借款表
            updateMmanUserLoan(borrowOrder, repayment, Constant.STATUS_OVERDUE_FIVE);
            //更新用户还款表
            updateCreditLoanPay(repayment);
            //保存用户还款详情表
            saveCreditLoanPayDetail(repayment, id, repaymentDetalList);

            HashMap<String, Object> lifeMap = new HashMap<>(2);
            BigDecimal realMoney = new BigDecimal(Integer.parseInt(String.valueOf(repayment.get("repaymented_amount"))) / 100.00);
            forUpdateMap.put("REAL_MONEY", realMoney);
            forUpdateMap.put("STATUS", Constant.STATUS_OVERDUE_FOUR);
            if (repayment.get("late_day") != null && Integer.valueOf(repayment.get("late_day").toString()) > 0) {
                forUpdateMap.put("repayRenewalMark", 1);
                saveSucRecord(order, repayment, borrowOrder, BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS);
                //修改订单生命周期
                lifeMap.put("orderId", assetOrderId);
                lifeMap.put("group", order.getCurrentOverdueLevel());
                OrderLifeCycle lifeCycle = findByLoanIdAndGroup(lifeMap);
                if (lifeCycle != null) {
                    lifeCycle.setCurrentLevel(new Byte(order.getCurrentOverdueLevel()));
                    lifeCycle.setCurrentStatus(new Byte(Constant.STATUS_OVERDUE_FOUR));
                    updateOrderLifeById(lifeCycle);
                }
            }
            this.localDataDao.updateOrderStatus(forUpdateMap);
            String backUserId = order.getCurrentCollectionUserId();
            lifeMap.clear();
            lifeMap.put("ID", backUserId);
            BackUser backUser = this.localDataDao.selectBackUser(lifeMap);
            if (null != backUser) {
                log.info("保存流转日志=借款loanID" + assetOrderId + "执行start" + DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
                MmanLoanCollectionStatusChangeLog loanChangeLog = new MmanLoanCollectionStatusChangeLog();
                loanChangeLog.setId(IdGen.uuid());
                loanChangeLog.setLoanCollectionOrderId(order.getOrderId());
                loanChangeLog.setBeforeStatus(order.getStatus());
                loanChangeLog.setAfterStatus(Constant.STATUS_OVERDUE_FOUR);
                //催收完成
                loanChangeLog.setType(Constant.STATUS_OVERDUE_FIVE);
                loanChangeLog.setOperatorName(Constant.OPERATOR_NAME);
                loanChangeLog.setRemark(Constant.PAY_MENT_SUCCESS + backUser.getUserName());
                loanChangeLog.setCompanyId(backUser.getCompanyId());
                loanChangeLog.setCurrentCollectionUserId(backUser.getUuid());
                loanChangeLog.setCurrentCollectionUserLevel(backUser.getGroupLevel());
                if (StringUtils.isNotBlank(order.getS1Flag())) {
                    loanChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_S1);
                } else {
                    loanChangeLog.setCurrentCollectionOrderLevel(order.getCurrentOverdueLevel());
                }
                loanChangeLog.setCreateDate(new Date());
                this.localDataDao.saveLoanChangeLog(loanChangeLog);
            }
        }
    }

    @Override
    public boolean getRepayedMoneyEqualTrueRepayed(String repayId) throws BusinessException {
        HashMap<String, String> paramMap = new HashMap<>(1);
        paramMap.put("ID", repayId);
        HashMap<String, Object> repayment = dataDao.getAssetRepayment(paramMap);
        int repaytedAmount = Integer.valueOf(repayment.get("repaymented_amount").toString());
        String loanId = repayment.get("asset_order_id").toString();
        CreditLoanPay loanPay = creditLoanPayDao.findByLoanId(loanId);
        if(loanPay == null || loanPay.getRealMoney().intValue() == repaytedAmount) {
            throw new BusinessException("999", "consume part repay error service system repay not over.");
        }
        //查询减免是否完成
        HashMap<String, Object> params = new HashMap<>(6);
        params.put("payId", repayId);
        params.put("type", 3);
        params.put("status", 2);
        Integer auditCount = auditCenterDao.findAllCount(params);
        if(auditCount > 0) {
            params.clear();
            params.put("ORDER_ID", loanId);
            params.put("USER_ID", repayment.get("user_id"));
            MmanLoanCollectionOrder order = this.localDataDao.selectLoanOrder(params);
            if(order.getReductionMoney() == 0) {
                throw new BusinessException("999", "reduction not over");
            }
        }

        return true;
    }

    @Override
    public boolean renewalOver(String repayId) throws BusinessException {
        HashMap<String, String> paramMap = new HashMap<>(1);
        paramMap.put("ID", repayId);
        HashMap<String, Object> repayment = dataDao.getAssetRepayment(paramMap);
        String repayTime = DateUtil.getDateFormat(DateUtil.getDateTimeFormat(repayment.get("repayment_time").toString(), "yyyy-MM-dd"), "yyyy-MM-dd");
        String loanId = repayment.get("asset_order_id").toString();
        CreditLoanPay loanPay = creditLoanPayDao.findByLoanId(loanId);
        if(DateUtil.getDateFormat(loanPay.getReceivableDate(), "yyyy-MM-dd").equals(repayTime)) {
           throw new BusinessException("999", "consume part repay error service system renewal not over.");
        }
        return true;
    }
}