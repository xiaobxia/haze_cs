package com.info.web.synchronization.service;

import com.info.back.exception.BusinessException;
import com.info.back.vo.OrderLifeCycle;
import com.info.web.pojo.cspojo.CreditLoanPay;
import com.info.web.pojo.cspojo.CreditLoanPayDetail;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ILocalDataService {

    /**
     * 更新本地数据-逾期
     * @param repaymentMap
     * @param repaymentDetalList
     * @param borrowOrder
     */
    void updateDateToLocalForOverdue(HashMap<String,Object> repaymentMap, List<HashMap<String,Object>> repaymentDetalList, HashMap<String,Object> borrowOrder);

    /**
     * 保存数据到本地
     * @param repaymentMap
     * @param userInfo
     * @param borrowOrder
     * @param cardInfo
     * @param userContactsList
     */
    void saveDateToLocal(HashMap<String,Object> repaymentMap,HashMap<String,Object> userInfo,
                         HashMap<String,Object> borrowOrder,HashMap<String,Object> cardInfo,
                         List<HashMap<String,Object>> userContactsList, List<HashMap<String, Object>> repaymentDetalList);

    /**保存用户借款表*/
    void saveMmanUserLoan(HashMap<String,Object> borrowOrder,HashMap<String,Object> repaymentMap);

    /**更新用户借款表*/
    void updateMmanUserLoan(HashMap<String,Object> borrowOrder,HashMap<String,Object> repaymentMap,String status);

    /**保存用户还款表*/
    void saveCreditLoanPay(HashMap<String,Object> repaymentMap);

    /**更新用户还款表*/
    void updateCreditLoanPay(HashMap<String,Object> repaymentMap);

    /**保存用户还款详情表*/
    void saveCreditLoanPayDetail(HashMap<String, Object> repayment,String id,List<HashMap<String,Object>> repaymentDetalList);

    /**
     * 保存用户通讯录
     * @param userInfo
     * @param userContactsList
     */
    void saveMmanUserRela(HashMap<String,Object> userInfo,List<HashMap<String,Object>> userContactsList);

    /**保存用户银行卡*/
    void saveSysUserBankCard(HashMap<String,Object> cardInfo);

    /**更新银行卡*/
    void updateSysUserBankCard(HashMap<String,Object> cardInfo);

    /**
     * 验证是否重复入库or是否存在
     *
     * @return
     */
    boolean checkLoan(String id);

    /**
     * 逾期处理
     * @param repaymentId
     */
    void dealOverdue(Map<String, String> callBackParams, String repaymentId);
     /** 保存至绩效详情表
     * @param order
     * @param repayment
     * @param borrowOrder
     */
    void saveSucRecord(MmanLoanCollectionOrder order, HashMap<String, Object> repayment, HashMap<String, Object> borrowOrder, String status);

    /**
     * 根据条件查询订单数量
     * @param map
     * @return
     */
    int getOrderCount(Map<String,Object> map);

    /**
     * 根据条件查询借款id
     * @param map
     * @return
     */
    List<Integer> getLoanIds(Map<String,Object> map);

    /**
     * 添加订单续期及还款次数
     * @param dataMap
     */
    void addOrderRenewCount(Map<String, Integer> dataMap);

    OrderLifeCycle findByLoanIdAndGroup(HashMap<String, Object> lifeMap);

    void updateOrderLifeById(OrderLifeCycle orderLifeCycle);

    /**
     * 计算还款
     * @param repaymentMap 还款map
     * @param creditLoanPay 还款bean
     * @return
     */
    CreditLoanPay operaRealPenlty(HashMap<String, Object> repaymentMap, CreditLoanPay creditLoanPay);


    int getPayStatus(String status);

    CreditLoanPayDetail operaRealPenltyDetail(HashMap<String, Integer> amountMap, HashMap<String, Object> repayDetail, String payId, CreditLoanPayDetail creditLoanPayDetail);

    HashMap<String, Integer> getAmountToMap(HashMap<String, Object> repaymentMap);

    boolean checkDetailId(List<String> idList, String id);

    /**
     * 续期处理
     * @param repaymentId 还款id
     */
    void dealRenewal(Map<String, String> callBackParams, String repaymentId);

    /**
     * 还款处理
     * @param repaymentId 还款id
     */
    void dealRepay(Map<String, String> callBackParams, String repaymentId);

    /**
     * 判断运营系统事务是否提交完成(运营系统金额 != cs还款表已还金额)
     * @return
     */
    boolean getRepayedMoneyEqualTrueRepayed(String repayId) throws BusinessException;

    /**
     * 判断运营系统续期是否完成
     * @param repayId
     * @return
     */
    boolean renewalOver(String repayId) throws BusinessException;
}
