package com.info.back.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.info.back.dao.*;
import com.info.back.utils.BackConstant;
import com.info.back.vo.OrderLifeCycle;
import com.info.back.vo.PerformanceCountRecord;
import com.info.back.vo.PerformanceResult;
import com.info.config.PayContents;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.*;
import com.info.web.util.BussinessLogUtil;
import com.info.web.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskJobMiddleService {


    @Resource
    private IMmanUserLoanService manUserLoanService;

    @Resource
    private ICreditLoanPayService creditLoanPayService;

    @Resource
    private IMmanLoanCollectionOrderService manLoanCollectionOrderService;

    @Resource
    private IMmanLoanCollectionRecordService mmanLoanCollectionRecordService;

    @Resource
    private IAlertMsgService sysAlertMsgService;

    @Resource
    private IMmanUserLoanDao mmanUserLoanDao;

    @Resource
    private ILocalDataDao localDataDao;

    @Resource
    private IMmanLoanCollectionOrderService mmanLoanCollectionOrderService;

    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;

    @Resource
    private IPerformanceCountRecordDao performanceCountRecordDao;

    @Resource
    private IBackRoleService backRoleService;
    @Resource
    private IBackUserService backUserService;
    @Resource
    private IOrderLifeCycleDao orderLifeCycleDao;
    @Resource
    private ICollectionStatisticsService collectionStatisticsService;


    /**
     * 获取借款记录
     */
    public MmanUserLoan getRecord(MmanUserLoan mmanUserLoan) {
        List<MmanUserLoan> result = mmanUserLoanDao.findMmanUserLoanList(mmanUserLoan);
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }

    /**
     * 分配催收任务，更新催收相关操作(更新催收订单，添加流转日志，更新借款、还款逾期额天数状态等)
     */
    public void autoDispatch() {
        try {
            log.info("TaskJobMiddleService autoDispatch start");
            //初始化参数
            MmanUserLoan mmanUserLoan = new MmanUserLoan();
            mmanUserLoan.setLoanStatus(BackConstant.CREDITLOANAPPLY_OVERDUE);
            List<MmanUserLoan> overdueList = manUserLoanService.findMmanUserLoanList2(mmanUserLoan);

            if (CollectionUtils.isNotEmpty(overdueList)) {
                for (MmanUserLoan mmanUserLoanOri : overdueList) {
                    dispatchforLoanId(mmanUserLoanOri.getId());
                }
            }
            log.info("overdueList size:{}", overdueList.size());
            log.info("TaskJobMiddleService autoDispatch end");
        } catch (Exception e) {
            log.error("autoDispatch error", e);
            BussinessLogUtil.error("autoDispatch error", "autoDispatch", e);
        }
    }


    public void dispatchforLoanId(String loanId) {
        log.info("dispatchforLoanId start,loanId:{}", loanId);
        if (StringUtils.isBlank(loanId)) {
            log.info("dispatchforLoanId loanId is null");
            return;
        }
        MmanUserLoan mmanUserLoan = manUserLoanService.get(loanId);
        if (null == mmanUserLoan) {
            log.info("dispatchforLoanId mmanUserLoan is null,loanId:" + loanId);
            return;
        } else if (!"4".equals(mmanUserLoan.getLoanStatus())) {
            log.info("dispatchforLoanId mmanUserLoan status is not equal 4,loanId:" + loanId);
            return;
        } else {
            dispatchForLoanId(mmanUserLoan);
        }
        log.info("dispatchforLoanId end,loanId:" + loanId);

    }


    /**
     * 分配催收任务，更新催收相关操作(更新催收订单，添加流转日志，更新借款、还款逾期额天数状态等)
     */
    private void dispatchForLoanId(MmanUserLoan mmanUserLoan) {

        log.info("TaskJobMiddleService dispatchForLoanId start");
        //初始化参数
        Date now = new Date();
        String sysName = "系统";
        String sysRemark = "系统派单";
        String sysPromoteRemark = "逾期升级，系统重新派单";
        Calendar clrNow = Calendar.getInstance();
        int dayNow = clrNow.get(Calendar.DAY_OF_MONTH);

        //和处理步骤对应的订单以及催收员列表
        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderNo114List = new ArrayList<>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo114List = new ArrayList<>();
        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderNo12List = new ArrayList<>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo12List = new ArrayList<>();
        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderNo131List = new ArrayList<>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo131List = new ArrayList<>();
        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderNo132List = new ArrayList<>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo132List = new ArrayList<>();

        List<MmanUserLoan> overdueList = new ArrayList<>();
        overdueList.add(mmanUserLoan);
        log.info("overdueList size:" + overdueList.size());

        if (null != overdueList && overdueList.size() > 0) {
            for (MmanUserLoan mmanUserLoanOri : overdueList) {
                try {
                    //更新借款
                    CreditLoanPay creditLoanPay = creditLoanPayService.findByLoanId(mmanUserLoanOri.getId());
                    if (null == creditLoanPay) {
                        log.info("CreditLoanPay is null ,loanId:" + mmanUserLoanOri.getId());
                        continue;
                    }

                    if (creditLoanPay.getRealMoney() == null) {
                        creditLoanPay.setRealMoney(new BigDecimal("0"));
                    }
                    //总应还额（借款额+罚息金额）
                    BigDecimal loanMoney = mmanUserLoanOri.getLoanMoney();
                    if (BackConstant.CREDITLOANAPPLY_OVERDUE.equals(mmanUserLoanOri.getLoanStatus())) {
                        int pday;
                        pday = DateUtil.daysBetween(mmanUserLoanOri.getLoanEndTime(), new Date());
                        //罚息率
                        BigDecimal pRate = new BigDecimal((Double.parseDouble(mmanUserLoanOri.getLoanPenaltyRate()) / 10000));
                        //逾期金额（部分还款算全罚息)
                        BigDecimal pmoney = (loanMoney.multiply(pRate).multiply(new BigDecimal(pday))).setScale(2, BigDecimal.ROUND_HALF_UP);

                        //逾期滞纳金 DecimalFormatUtil.df2Points.format(pmoney.doubleValue())
                        mmanUserLoanOri.setLoanPenalty(pmoney);

                        //外层循环处理时间随着数据量增长会越来越长，防止更新滞纳金（罚息）时覆盖更新已还款的数据
                        MmanUserLoan mmanUserLoanForUpdate = new MmanUserLoan();
                        mmanUserLoanForUpdate.setId(mmanUserLoanOri.getId());
                        mmanUserLoanForUpdate.setLoanPenalty(mmanUserLoanOri.getLoanPenalty());
                        manUserLoanService.updateMmanUserLoan(mmanUserLoanForUpdate);
                        //更新还款表中滞纳金
                        BigDecimal znj = mmanUserLoanOri.getLoanPenalty().subtract(creditLoanPay.getRealgetInterest());
                        CreditLoanPay np = new CreditLoanPay();
                        np.setId(creditLoanPay.getId());
                        np.setReceivableInterest(znj);
                        np.setReceivableMoney(mmanUserLoanOri.getLoanMoney().add(mmanUserLoanOri.getLoanPenalty()));
                        creditLoanPayService.updateCreditLoanPay(np);


                        //1 查询需要处理的订单和该笔订单对应的分组
                        Calendar clrLoanEnd = Calendar.getInstance();
                        clrLoanEnd.setTime(mmanUserLoanOri.getLoanEndTime());

                        //1.3.1 所有新订单（逾期3天以内包括续期已逾期）分组为S1
                        MmanLoanCollectionOrder mmanLoanCollectionOrder = new MmanLoanCollectionOrder();
                        //读取马甲名
                        String appName = PayContents.APP_NAME;
                        mmanLoanCollectionOrder.setLoanId(mmanUserLoanOri.getId());
                        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderList = manLoanCollectionOrderService.findList(mmanLoanCollectionOrder);
                        if (null == mmanLoanCollectionOrderList || mmanLoanCollectionOrderList.isEmpty() || "续期后逾期".equals(mmanUserLoan.getLoanSysRemark())) {

                            log.error("mmanLoancollectionOrder enter 13 ,mmanUserLoan id:" + mmanUserLoanOri.getId());
                            if ("续期后逾期".equals(mmanUserLoan.getLoanSysRemark())) {
                                mmanUserLoan.setLoanSysRemark("");
                                localDataDao.updateMmanUserLoan(mmanUserLoan);
                            }
                            MmanLoanCollectionOrder mmanLoanCollectionOrderNo131 = new MmanLoanCollectionOrder();
                            mmanLoanCollectionOrderNo131.setLoanId(mmanUserLoanOri.getId());
                            mmanLoanCollectionOrderNo131.setOrderId(mmanUserLoanOri.getLoanPyId());
                            mmanLoanCollectionOrderNo131.setUserId(mmanUserLoanOri.getUserId());
                            mmanLoanCollectionOrderNo131.setOverdueDays(pday);
                            mmanLoanCollectionOrderNo131.setPayId(creditLoanPay.getId());
                            mmanLoanCollectionOrderNo131.setDispatchName(sysName);
                            mmanLoanCollectionOrderNo131.setDispatchTime(now);
                            mmanLoanCollectionOrderNo131.setStatus("1");
                            mmanLoanCollectionOrderNo131.setOperatorName(sysName);
                            mmanLoanCollectionOrderNo131.setRemark(sysRemark);
                            mmanLoanCollectionOrderNo131.setJxlStatus(BackConstant.XJX_JXL_STATUS_REFUSE);
                            mmanLoanCollectionOrderNo131.setS1Date(DateUtil.getDateFormat("yyyy-MM-dd"));
                            mmanLoanCollectionOrderNo131.setS2Date("");
                            mmanLoanCollectionOrderNo131.setS3Date("");
                            mmanLoanCollectionOrderNo131.setM1m2Date("");
                            mmanLoanCollectionOrderNo131List.add(mmanLoanCollectionOrderNo131);
                            //进入s1记录一次生命周期----------------------------------
                            OrderLifeCycle lifeCycle = new OrderLifeCycle();
                            lifeCycle.setLoanId(Integer.valueOf(mmanUserLoanOri.getId()));
                            lifeCycle.setPayId(Integer.valueOf(creditLoanPay.getId()));
                            lifeCycle.setCreateTime(new Date());
                            lifeCycle.setS1Time(lifeCycle.getCreateTime());
                            lifeCycle.setCurrentStatus(new Byte(Constant.STATUS_OVERDUE_ONE));
                            lifeCycle.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S1));
                            orderLifeCycleDao.insertSelective(lifeCycle);

                            Map<String, Object> personMap = new HashMap<>(4);
                            personMap.put("dispatchTime", DateUtil.getDateFormat("yyyy-MM-dd"));
                            personMap.put("groupLevel", BackConstant.XJX_OVERDUE_LEVEL_S1);
//								personMap.put("groupLevel",BackConstant.XJX_OVERDUE_LEVEL_S2);
                            personMap.put("userStatus", BackConstant.ON);

                            mmanLoanCollectionPersonNo131List = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap);


                            if (null == mmanLoanCollectionPersonNo131List || mmanLoanCollectionPersonNo131List.isEmpty()) {
                                log.error("mmanLoancollectionOrder enter 13 no man,mmanUserLoan id:" + mmanUserLoanOri.getId());
                                SysAlertMsg alertMsg = new SysAlertMsg();
                                alertMsg.setTitle("分配催收任务失败");
                                alertMsg.setContent("所有公司S1组查无可用催收人,请及时添加或启用该组催收员。");
//									alertMsg.setContent("所有公司S2组查无可用催收人,请及时添加或启用该组催收员。");
                                alertMsg.setDealStatus(BackConstant.OFF);
                                alertMsg.setStatus(BackConstant.OFF);
                                alertMsg.setType(SysAlertMsg.TYPE_COMMON);
                                sysAlertMsgService.insert(alertMsg);
                                log.warn("所有公司S1组查无可用催收人...");
//                                log.warn("所有公司S2组查无可用催收人...");
                            }
                        } else {

                            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderList.get(0);

                            log.error("mmanLoancollectionOrder enter 13 redispatch,mmanLoanCollectionOrder id:" + mmanLoanCollectionOrder.getId());


                            if (BackConstant.XJX_COLLECTION_ORDER_DELETED.equals(mmanLoanCollectionOrderOri.getRenewStatus())) {
                                log.error("mmanLoancollectionOrder renewStatus is deleted,do not dispatch,id:" + mmanLoanCollectionOrder.getId());
                                continue;
                            }

                            if (BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(mmanLoanCollectionOrderOri.getStatus())) {
                                log.error("mmanLoancollectionOrder mmanLoanCollectionOrderOri status is success,do not dispatch,id:" + mmanLoanCollectionOrder.getId());
                                continue;
                            }

                            mmanLoanCollectionOrderOri.setOverdueDays(pday);

                            //1.3.2 S1中逾期天数 大于3的已存在未完成订单，分组升级为S2
//								&& pday >10
                            if (BackConstant.XJX_OVERDUE_LEVEL_S1.equals(mmanLoanCollectionOrderOri.getCurrentOverdueLevel()) && pday > 3
                                    && !BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(mmanLoanCollectionOrderOri.getStatus())
                                    && !BackConstant.XJX_COLLECTION_ORDER_STATE_PAYING.equals(mmanLoanCollectionOrderOri.getStatus())) {
                                log.error("mmanLoancollectionOrder enter 13 redispatch overdue>3,mmanLoanCollectionOrder id:" + mmanLoanCollectionOrder.getId());
                                mmanLoanCollectionOrderOri.setDispatchName(sysName);
                                mmanLoanCollectionOrderOri.setDispatchTime(now);
                                mmanLoanCollectionOrderOri.setOperatorName(sysName);
                                mmanLoanCollectionOrderOri.setRemark(sysPromoteRemark);
                                //上一催收员
                                mmanLoanCollectionOrderOri.setLastCollectionUserId(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());

                                //更新聚信立报告申请审核状态为初始状态，下一催收员要看需要重新申请
                                mmanLoanCollectionOrderOri.setJxlStatus(BackConstant.XJX_JXL_STATUS_REFUSE);

                                mmanLoanCollectionOrderOri.setS2Date(DateUtil.getDateFormat("yyyy-MM-dd"));
                                mmanLoanCollectionOrderNo132List.add(mmanLoanCollectionOrderOri);

                                //进入s2----------------------------------
                                HashMap<String, Object> lifeMap = new HashMap<>(2);
                                lifeMap.put("orderId", mmanUserLoanOri.getId());
                                lifeMap.put("group", BackConstant.XJX_OVERDUE_LEVEL_S1);
                                OrderLifeCycle s1Record = orderLifeCycleDao.findByLoanIdAndGroup(lifeMap);
                                if (s1Record != null) {
                                    s1Record.setId(null);
                                    s1Record.setUpdateTime(null);
                                    s1Record.setCreateTime(new Date());
                                    s1Record.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S2));
                                    orderLifeCycleDao.insertSelective(s1Record);
                                }

                                Map<String, Object> personMap = new HashMap<>(4);
                                personMap.put("dispatchTime", DateUtil.getDateFormat("yyyy-MM-dd"));
                                personMap.put("groupLevel", BackConstant.XJX_OVERDUE_LEVEL_S2);
                                personMap.put("userStatus", BackConstant.ON);

                                mmanLoanCollectionPersonNo132List = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap);

                                if (null == mmanLoanCollectionPersonNo132List || mmanLoanCollectionPersonNo132List.isEmpty()) {
                                    log.error("mmanLoancollectionOrder enter 13 overdue>10 no man,mmanUserLoan id:" + mmanUserLoanOri.getId());
                                    SysAlertMsg alertMsg = new SysAlertMsg();
                                    alertMsg.setTitle("分配催收任务失败");
                                    alertMsg.setContent("所有公司S2组查无可用催收人,请及时添加或启用该组催收员。");
                                    alertMsg.setDealStatus(BackConstant.OFF);
                                    alertMsg.setStatus(BackConstant.OFF);
                                    alertMsg.setType(SysAlertMsg.TYPE_COMMON);
                                    sysAlertMsgService.insert(alertMsg);
                                    log.warn("所有公司S2组查无可用催收人...");
                                }
                            } else if (BackConstant.XJX_OVERDUE_LEVEL_S2.equals(mmanLoanCollectionOrderOri.getCurrentOverdueLevel()) && pday > 15
                                    && !BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(mmanLoanCollectionOrderOri.getStatus())
                                    && !BackConstant.XJX_COLLECTION_ORDER_STATE_PAYING.equals(mmanLoanCollectionOrderOri.getStatus())) {
                                log.info("大于15天派单至s3 mmanLoanCollectionOrder id:" + mmanLoanCollectionOrder.getId());
                                log.info("pday:{},mmanLoanCollectionOrderOri:{}",pday, JSON.toJSONString(mmanLoanCollectionOrderOri));
                                mmanLoanCollectionOrderOri.setDispatchName(sysName);
                                mmanLoanCollectionOrderOri.setDispatchTime(now);
                                mmanLoanCollectionOrderOri.setOperatorName(sysName);
                                mmanLoanCollectionOrderOri.setRemark(sysPromoteRemark);
                                mmanLoanCollectionOrderOri.setLastCollectionUserId(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());

                                //更新聚信立报告申请审核状态为初始状态，下一催收员要看需要重新申请
                                mmanLoanCollectionOrderOri.setJxlStatus(BackConstant.XJX_JXL_STATUS_REFUSE);

                                mmanLoanCollectionOrderOri.setS3Date(DateUtil.getDateFormat("yyyy-MM-dd"));
                                mmanLoanCollectionOrderNo12List.add(mmanLoanCollectionOrderOri);

                                //进入s3----------------------------------
                                HashMap<String, Object> lifeMap = new HashMap<>(2);
                                lifeMap.put("orderId", mmanUserLoanOri.getId());
                                lifeMap.put("group", BackConstant.XJX_OVERDUE_LEVEL_S1);
                                OrderLifeCycle s1Record = orderLifeCycleDao.findByLoanIdAndGroup(lifeMap);
                                if (s1Record != null) {
                                    s1Record.setId(null);
                                    s1Record.setUpdateTime(null);
                                    s1Record.setCreateTime(new Date());
                                    s1Record.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S3));
                                    orderLifeCycleDao.insertSelective(s1Record);
                                }

                                Map<String, Object> personMap = new HashMap<>(4);
                                personMap.put("dispatchTime", DateUtil.getDateFormat("yyyy-MM-dd"));
                                personMap.put("groupLevel", BackConstant.XJX_OVERDUE_LEVEL_S3);
                                personMap.put("userStatus", BackConstant.ON);

                                mmanLoanCollectionPersonNo12List = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap);

                                if (null == mmanLoanCollectionPersonNo12List || mmanLoanCollectionPersonNo12List.isEmpty()) {
                                    log.error("mmanLoancollectionOrder enter 13 overdue>10 no man,mmanUserLoan id:" + mmanUserLoanOri.getId());
                                    SysAlertMsg alertMsg = new SysAlertMsg();
                                    alertMsg.setTitle("分配催收任务失败");
                                    alertMsg.setContent("所有公司S3组查无可用催收人,请及时添加或启用该组催收员。");
                                    alertMsg.setDealStatus(BackConstant.OFF);
                                    alertMsg.setStatus(BackConstant.OFF);
                                    alertMsg.setType(SysAlertMsg.TYPE_COMMON);
                                    sysAlertMsgService.insert(alertMsg);
                                    log.warn("所有公司S3组查无可用催收人...");
                                }

                            } else if (appName.equals(Constant.JX_NAME) && dayNow == 1 && !BackConstant.SAVE.equals(mmanLoanCollectionOrderOri.getCsstatus()) && BackConstant.XJX_OVERDUE_LEVEL_S3.equals(mmanLoanCollectionOrderOri.getCurrentOverdueLevel()) && pday > 30
                                    && !BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(mmanLoanCollectionOrderOri.getStatus())
                                    && !BackConstant.XJX_COLLECTION_ORDER_STATE_PAYING.equals(mmanLoanCollectionOrderOri.getStatus())) {
                                log.info("每月1号逾期天数大于30且未留单派单至M1_M2 mmanLoanCollectionOrder id:" + mmanLoanCollectionOrder.getId());
                                mmanLoanCollectionOrderOri.setDispatchName(sysName);
                                mmanLoanCollectionOrderOri.setDispatchTime(now);
                                mmanLoanCollectionOrderOri.setOperatorName(sysName);
                                mmanLoanCollectionOrderOri.setRemark(sysPromoteRemark);
                                //上一催收员
                                mmanLoanCollectionOrderOri.setLastCollectionUserId(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());

                                //更新聚信立报告申请审核状态为初始状态，下一催收员要看需要重新申请
                                mmanLoanCollectionOrderOri.setJxlStatus(BackConstant.XJX_JXL_STATUS_REFUSE);

                                mmanLoanCollectionOrderOri.setM1m2Date(DateUtil.getDateFormat("yyyy-MM-dd"));
                                mmanLoanCollectionOrderNo114List.add(mmanLoanCollectionOrderOri);

                                //进入m1_m2----------------------------------
                                HashMap<String, Object> lifeMap = new HashMap<>(2);
                                lifeMap.put("orderId", mmanUserLoanOri.getId());
                                lifeMap.put("group", BackConstant.XJX_OVERDUE_LEVEL_S1);
                                OrderLifeCycle s1Record = orderLifeCycleDao.findByLoanIdAndGroup(lifeMap);
                                if (s1Record != null) {
                                    s1Record.setId(null);
                                    s1Record.setUpdateTime(null);
                                    s1Record.setCreateTime(new Date());
                                    s1Record.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_M1_M2));
                                    orderLifeCycleDao.insertSelective(s1Record);
                                }

                                HashMap<String, Object> personMap = new HashMap<>(4);
                                BackRole topCsRole = backRoleService.getTopCsRole();
                                List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
                                personMap.put("dispatchTime", DateUtil.getDateFormat("yyyy-MM-dd"));
                                personMap.put("groupLevel", BackConstant.XJX_OVERDUE_LEVEL_M1_M2);
                                personMap.put("userStatus", BackConstant.ON);
                                personMap.put("realIds", roleChildIds);

                                mmanLoanCollectionPersonNo114List = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap);

                                if (null == mmanLoanCollectionPersonNo114List || mmanLoanCollectionPersonNo114List.isEmpty()) {
                                    log.error("mmanLoancollectionOrder enter 13 overdue>10 no man,mmanUserLoan id:" + mmanUserLoanOri.getId());
                                    SysAlertMsg alertMsg = new SysAlertMsg();
                                    alertMsg.setTitle("分配催收任务失败");
                                    alertMsg.setContent("所有公司M1_M2组查无可用催收人,请及时添加或启用该组催收员。");
                                    alertMsg.setDealStatus(BackConstant.OFF);
                                    alertMsg.setStatus(BackConstant.OFF);
                                    alertMsg.setType(SysAlertMsg.TYPE_COMMON);
                                    sysAlertMsgService.insert(alertMsg);
                                    log.warn("所有公司M1_M2组查无可用催收人...");
                                }
                            } else {
                                //原订单不在条件内的只需更新逾期天数
                                manLoanCollectionOrderService.saveMmanLoanCollectionOrder(mmanLoanCollectionOrderOri);
                            }
                        }
                    }
//					}
                } catch (Exception e) {
                    log.error("分配当前催收任务出错，借款ID：" + mmanUserLoanOri.getId(), e);
                }
            }

            //2 将订单派到对应分组催收员
            mmanLoanCollectionRecordService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo114List, mmanLoanCollectionPersonNo114List, now);
            mmanLoanCollectionRecordService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo12List, mmanLoanCollectionPersonNo12List, now);
            mmanLoanCollectionRecordService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo131List, mmanLoanCollectionPersonNo131List, now);
            mmanLoanCollectionRecordService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo132List, mmanLoanCollectionPersonNo132List, now);

        }
        log.error("TaskJobMiddleService dispatchForLoanId end");
    }



    /**
     * 晚上 11:30 开始推送中智诚黑名单
     */
    public void pushZzcBlackList() {
        /*log.info("push zzc blackList begin!");
        List<HashMap<String, Object>> hashMapList = mmanLoanCollectionOrderService.selectZzcBlackList();
        Gson gson = new Gson();
        if (hashMapList != null && hashMapList.size() > 0) {
            log.info("black list size:" + hashMapList.size());
            for (HashMap<String, Object> parmas : hashMapList) {
                if (parmas == null) {
                    continue;
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                //姓名
                hashMap.put("name", parmas.get("realname"));
                //身份证
                hashMap.put("pid", parmas.get("id_number"));
                hashMap.put("mobile", parmas.get("user_phone"));
                //贷款申请类型
                hashMap.put("loan_type", "1");
                //贷款金额 N
                hashMap.put("loan_amount", "1000");
                //逾期天数
                Object overdusObj = parmas.get("overdueDays");
                //贷款申请日期
                hashMap.put("applied_at", zzcFormatDate(parmas.get("create_date"), true, overdusObj));
                //黑名单确认日期
                hashMap.put("confirmed_at", zzcFormatDate(parmas.get("create_date"), false, overdusObj));
                //逾期金额
                hashMap.put("over_due_amount", overdueMoney(overdusObj));
                //确认类型
                hashMap.put("confirm_type", "overdue");
                //确认细节
                hashMap.put("confirm_details", zzcOverdueStatus(overdusObj));
                String jsonStrData = gson.toJson(hashMap);
                try {
                    zzcUrlPush(jsonStrData);
                } catch (IOException e) {
                    log.info("zzc push blacklist fail id = " + parmas.get("id_number") + ",reason:" + e.getMessage());
                    //log.error("zzc push error!", e);
                }
            }
        }*/
    }

    /**
     * 远程推送
     */
    private void zzcUrlPush(String jsonStrData) throws IOException {
        /*HttpClient c = new HttpClient();
        c.getParams().setContentCharset("utf-8");
        c.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(PayContents.ZZC_USERNAME, PayContents.ZZC_PASSWORD);
        c.getState().setCredentials(AuthScope.ANY, defaultcreds);
        PostMethod postMethod = new PostMethod(PayContents.ZZC_PUSHBLACKLIST_URL);
        // 可根据需要变更查询字段值
        RequestEntity r = new StringRequestEntity(jsonStrData, "application/json", "utf-8");
        postMethod.setRequestEntity(r);
        c.executeMethod(postMethod);
        // 可在此获取返回结果json字符串进行处理
        String result = postMethod.getResponseBodyAsString();
        if (result != null) {
            log.info("zzc result:" + result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (jsonObject.containsKey("uuid")) {
                log.info("push zzc blacklist success " + jsonObject.get("uuid"));
            }
        }*/


    }

    /**
     * 判断逾期的标准
     */
    private String zzcOverdueStatus(Object data) {
        if (data == null) {
            return null;
        }
        Integer ovderDues = Integer.parseInt(data.toString());
        if (1 <= ovderDues && ovderDues <= 30) {
            return "M1";
        }
        if (ovderDues >= 31 && ovderDues <= 60) {
            return "M2";
        }
        if (ovderDues >= 61 && ovderDues <= 90) {
            return "M3";
        }
        if (ovderDues >= 91 && ovderDues <= 120) {
            return "M4";
        }
        if (ovderDues >= 121 && ovderDues <= 150) {
            return "M5";
        }
        if (ovderDues >= 151 && ovderDues <= 180) {
            return "M6";
        }
        if (ovderDues >= 181 && ovderDues <= 365) {
            return "MN";
        }
        if (ovderDues >= 366 && ovderDues <= 730) {
            return "Y1";
        }
        if (ovderDues >= 731) {
            return "Y2";
        }
        return null;
    }

    /**
     * 将日期字符串转换为一个日期对象
     */
    private String zzcFormatDate(Object data, boolean flag, Object overdueDays) {
        //借款周期
        Integer loanDay = 7;
        String timeStr = null;
        if (data == null || overdueDays == null) {
            return null;
        }
        String datestr = data.toString();
        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(datestr);
            format = "yyyy/MM/dd";
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(format);
            if (flag) {
                //借款周期+逾期天数+1（因为是一点开始派单）
                Integer alldays = loanDay + Integer.parseInt(overdueDays.toString()) + 1;
                //返回订单创建时间
                timeStr = simpleDateFormat2.format(new Date(date.getTime() - (long) alldays * 24 * 60 * 60 * 1000));
            } else {
                //返回黑名单确认时间
                timeStr = simpleDateFormat2.format(date);
            }
        } catch (ParseException e) {
            log.error("zzcFormatDate error!", e);
        }
        return timeStr;
    }

    /**
     * 计算逾期金额
     *
     */
    private float overdueMoney(Object overdueDaysObj) {
        //本金
        Integer captial = 1000;
        //每天的逾期金额
        Integer overdusMoneyOneDay = 30;
        if (overdueDaysObj == null) {
            return captial;
        }
        Integer overdueDays = Integer.parseInt(overdueDaysObj.toString());
        return overdueDays * overdusMoneyOneDay + captial;
    }

    /**
     * 续还记录统计job
     */
    public void countCollectionResult() {
        log.info("countCollectionResult start!");
        try {
            collectionStatisticsService.countCollectionResult("", "");
        } catch (Exception e) {
            log.error("countCollectionResult error :{}", e);
        }
        log.info("countCollectionResult end!");
    }


    /**
     * 绩效考核统计job
     */
    public void countPerformanceJob() {
        try {
            Date now = new Date();
            /*s1*/
            String paramDate = DateUtil.getDateFormat(DateUtil.addDay(now, -3), "yyyy-MM-dd");
            countPerformance(paramDate, BackConstant.XJX_OVERDUE_LEVEL_S1, false);
            /*s2*/
            paramDate = DateUtil.getDateFormat(DateUtil.addDay(now, -12), "yyyy-MM-dd");
            countPerformance(paramDate, BackConstant.XJX_OVERDUE_LEVEL_S2, false);
            /*s3*/
            paramDate = DateUtil.getDateFormat(DateUtil.addDay(now, -15), "yyyy-MM-dd");
            countPerformance(paramDate, BackConstant.XJX_OVERDUE_LEVEL_S3, false);
            /*m1_m2*/
            countPerformance(paramDate, BackConstant.XJX_OVERDUE_LEVEL_M1_M2, true);
//            log.info("countPerformanceJob end!");
        } catch (Exception e) {
            log.error("countPerformanceJob error!", e);
            BussinessLogUtil.error("countPerformanceJob error!", "countPerformanceJob", e);
        }
    }

    private void countPerformance(String paramDate, String groupLevel, boolean isM1M2) {
//        log.info("countPerformanceJob start! currentGroupLevel:{} ", BackConstant.GROUP_NAME_MAP.get(groupLevel));
        HashMap<String, Object> params = new HashMap<>(2);
        List<String> statusList = new ArrayList<>(2);
        if (!isM1M2) {
            params.put("paramDate", paramDate);
        }
        params.put("groupLevel", groupLevel);
        /*全部*/
        List<PerformanceResult> all = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        params.put("isSys", "y");
        /*当日系统进件*/
        List<PerformanceResult> sysTotal = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        /*当日人工进件*/
        params.remove("isSys");
        params.put("isHand", "y");
        List<PerformanceResult> handTotal = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        params.remove("isHand");
        statusList.add(Constant.STATUS_OVERDUE_FIVE);
        statusList.add(Constant.STATUS_OVERDUE_THREE);
        if (BackConstant.XJX_OVERDUE_LEVEL_S3.equals(groupLevel)) {
            updateS3(params, statusList);
        }
        params.put("loanStatus", statusList);
        /*催收成功*/
        List<PerformanceResult> suc = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        /*续期数*/
        statusList.remove(Constant.STATUS_OVERDUE_FIVE);
        params.put("loanStatus", statusList);
        List<PerformanceResult> renewal = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        Map<String, Integer> renewalMap = null;
        Map<String, Integer[]> sucMap = null;
        Map<String, PerformanceResult> handMap = null;
        Map<String, PerformanceResult> sysMap = null;

        if (CollectionUtils.isNotEmpty(suc)) {
            sucMap = suc.stream().collect(Collectors.toMap(PerformanceResult::getMapK, PerformanceResult::getMapV));
        }
        if (CollectionUtils.isNotEmpty(renewal)) {
            renewalMap = renewal.stream().collect(Collectors.toMap(PerformanceResult::getMapK, PerformanceResult::getAmount));
        }
        if (CollectionUtils.isNotEmpty(handTotal)) {
            handMap = handTotal.stream().collect(Collectors.toMap(PerformanceResult::getMapK, Function.identity()));
        }
        if (CollectionUtils.isNotEmpty(sysTotal)) {
            sysMap = sysTotal.stream().collect(Collectors.toMap(PerformanceResult::getMapK, Function.identity()));
        }

        HashMap<String, Object> existParams = new HashMap<>(3);
        List<PerformanceCountRecord> exist;
        List<PerformanceCountRecord> lastOneList;
        if (CollectionUtils.isNotEmpty(all)) {
            for (PerformanceResult one : all) {
                existParams.clear();
                existParams.put("userName", one.getCsName());
                existParams.put("countDate", DateUtil.getDateFormat(one.getCountDate(), "yyyy-MM-dd"));
                existParams.put("groupLevel", one.getGroupLevel());
                exist = performanceCountRecordDao.selectByCountDateAndName(existParams);
                existParams.put("getLast", existParams.get("countDate"));
                existParams.remove("countDate");
                lastOneList = performanceCountRecordDao.selectByCountDateAndName(existParams);
                PerformanceCountRecord lastOne = null;
                if (CollectionUtils.isNotEmpty(lastOneList)) {
                    lastOne = lastOneList.get(0);
                }
                boolean lastOneNull = lastOne == null;
                int handNum = handMap == null ? 0 : handMap.get(one.getMapK()) == null ? 0 : handMap.get(one.getMapK()).getAmount();
                int renewNum = renewalMap == null ? 0 : renewalMap.get(one.getMapK()) == null ? 0 : renewalMap.get(one.getMapK());
                PerformanceResult sysOne = sysMap == null ? null : sysMap.get(one.getMapK());
                if (CollectionUtils.isNotEmpty(exist)) {
                    PerformanceCountRecord old = exist.get(0);
                    updateExist(old, sucMap, one, lastOneNull, lastOne, renewNum, handNum, sysOne);
                } else {
                    createNew(one, lastOne, lastOneNull, sucMap, renewNum, handNum, sysOne);
                }

            }
        }

    }

    private void createNew(PerformanceResult one, PerformanceCountRecord lastOne, boolean lastOneNull, Map<String, Integer[]> sucMap,
                           int renewNum, int handNum, PerformanceResult sysOne) {
        PerformanceCountRecord newRecord = new PerformanceCountRecord();
        if (sysOne == null) {
            newRecord.setSysOrder(0);
            newRecord.setTotalIntoOrder(lastOneNull ? handNum : lastOne.getTotalIntoOrder() + handNum);
        } else {
            newRecord.setSysOrder(sysOne.getAmount());
            newRecord.setTotalIntoOrder(lastOneNull ? handNum + sysOne.getAmount() : lastOne.getTotalIntoOrder() + handNum + sysOne.getAmount());
        }
        newRecord.setTel(one.getMobile());
        newRecord.setHandOrder(handNum);
        newRecord.setCountDate(DateUtil.getDateTimeFormat(DateUtil.getDateFormat(one.getCountDate(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        newRecord.setCreateDate(new Date());
        newRecord.setUpdateDate(newRecord.getCreateDate());
        newRecord.setUserName(one.getCsName());
        newRecord.setGroupLevel(one.getGroupLevel());
        Integer[] value = sucMap == null ? null : sucMap.get(one.getMapK());
        boolean isValueNull = value == null;
        newRecord.setOutOrder(isValueNull ? 0 : value[0]);
        newRecord.setFee(isValueNull ? 0 : value[1] - one.getReductionMoney());
        newRecord.setTotalOutOrder(lastOneNull ? isValueNull ? 0 : value[0] : lastOne.getTotalOutOrder() + (isValueNull ? 0 : value[0]));
        newRecord.setReturnPrincipal(isValueNull ? 0 : value[0] * 1000);
        if (newRecord.getTotalIntoOrder() > 0) {
            newRecord.setSucRate(newRecord.getTotalOutOrder() == 0 ? 0 : new BigDecimal(newRecord.getTotalOutOrder()).divide(new BigDecimal(newRecord.getTotalIntoOrder()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue());
        } else {
            newRecord.setSucRate(0d);
        }
        newRecord.setRenewalCount(renewNum);
        newRecord.setReturnPrincipal(newRecord.getReturnPrincipal() - newRecord.getRenewalCount() * 1000);
        performanceCountRecordDao.insertSelective(newRecord);
    }

    private void updateExist(PerformanceCountRecord old, Map<String, Integer[]> sucMap, PerformanceResult one, boolean lastOneNull, PerformanceCountRecord lastOne
            , int renewNum, int handNum, PerformanceResult sysOne) {
        if (sysOne == null) {
            if (handNum > old.getHandOrder()) {
                old.setTotalIntoOrder(lastOneNull ? old.getTotalIntoOrder() + (handNum - old.getHandOrder()) : lastOne.getTotalIntoOrder() + handNum);
                old.setHandOrder(handNum);
            }
        } else {
            if (sysOne.getAmount() > old.getSysOrder()) {
                old.setTotalIntoOrder(lastOneNull ? old.getTotalIntoOrder() + (sysOne.getAmount() - old.getSysOrder()) : lastOne.getTotalIntoOrder() + sysOne.getAmount() + old.getHandOrder());
                old.setSysOrder(one.getAmount());
            } else if (handNum > old.getHandOrder()) {
                old.setTotalIntoOrder(lastOneNull ? old.getTotalIntoOrder() + (handNum - old.getHandOrder()) : lastOne.getTotalIntoOrder() + old.getSysOrder() + handNum);
                old.setHandOrder(handNum);
            } else if (!lastOneNull) {
                old.setTotalIntoOrder(lastOne.getTotalIntoOrder() + sysOne.getAmount() + handNum);
            }
        }
        if (sucMap != null) {
            Integer[] value = sucMap.get(one.getMapK());
            if (value != null && value[0] > old.getOutOrder()) {
                old.setTotalOutOrder(lastOneNull ? old.getTotalOutOrder() + (value[0] - old.getOutOrder()) : lastOne.getTotalOutOrder() + value[0]);
                old.setOutOrder(value[0]);
                old.setFee(value[1] - one.getReductionMoney());
                old.setReturnPrincipal(1000 * (value[0] - renewNum));
            } else {
                old.setTotalOutOrder(lastOneNull ? old.getTotalOutOrder() : lastOne.getTotalOutOrder() + old.getOutOrder());
            }
        } else {
            old.setTotalOutOrder(lastOneNull ? old.getTotalOutOrder() : lastOne.getTotalOutOrder() + old.getOutOrder());
        }
        old.setRenewalCount(renewNum);
        old.setUpdateDate(new Date());
        if (old.getTotalIntoOrder() > 0) {
            old.setSucRate(new BigDecimal(old.getTotalOutOrder()).divide(new BigDecimal(old.getTotalIntoOrder()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue());
        } else {
            old.setSucRate(0d);
        }
        performanceCountRecordDao.updateByPrimaryKeySelective(old);
    }

    /**
     * s3由于可留单需长期更新催回量
     *
     * @param params     查询参数
     * @param statusList 状态值集合
     */
    private void updateS3(HashMap<String, Object> params, List<String> statusList) {
        params.put("updateS3", "Y");
        /*查询减免金额*/
        List<PerformanceResult> reducationAll = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        /*催收成功*/
        List<PerformanceResult> suc = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        /*续期数*/
        statusList.remove(Constant.STATUS_OVERDUE_FIVE);
        params.put("loanStatus", statusList);
        List<PerformanceResult> renewal = mmanLoanCollectionOrderDao.getPerformanceResults(params);
        params.remove("updateS3");
        params.remove("loanStatus");
        statusList.add(Constant.STATUS_OVERDUE_FIVE);
        Map<String, Integer> renewalMap = null;
        Map<String, Integer[]> sucMap = null;
        Map<String, PerformanceResult> reducMap = null;
        if (CollectionUtils.isNotEmpty(suc)) {
            sucMap = suc.stream().collect(Collectors.toMap(PerformanceResult::getMapK, PerformanceResult::getMapV));
        }
        if (CollectionUtils.isNotEmpty(renewal)) {
            renewalMap = renewal.stream().collect(Collectors.toMap(PerformanceResult::getMapK, PerformanceResult::getAmount));
        }
        if (CollectionUtils.isNotEmpty(reducationAll)) {
            reducMap = reducationAll.stream().collect(Collectors.toMap(PerformanceResult::getMapK, Function.identity()));
        }
        if (sucMap == null && renewalMap == null) {
            return;
        }
        HashMap<String, Object> findParams = new HashMap<>(2);
        findParams.put("groupLevel", BackConstant.XJX_OVERDUE_LEVEL_S3);
        findParams.put("endDate", params.get("paramDate"));
        List<PerformanceCountRecord> s3Records = performanceCountRecordDao.findAll(findParams);
        Map<String, PerformanceCountRecord> recordsMap;
        if (CollectionUtils.isNotEmpty(s3Records)) {
            recordsMap = s3Records.stream().collect(Collectors.toMap(PerformanceCountRecord::getMapK, Function.identity()));
            for (PerformanceCountRecord one : s3Records) {
                String mapK = String.join("", DateUtil.getDateFormat(one.getCountDate(), "yyyy-MM-dd"), one.getUserName());
                String lastOneK = String.join("", DateUtil.getDateFormat(DateUtil.addDay(one.getCountDate(), -1), "yyyy-MM-dd"), one.getUserName());
                PerformanceResult reduc = reducMap == null ? null : reducMap.get(mapK);
                PerformanceCountRecord lastOne = recordsMap.get(lastOneK);
                boolean lastOneNull = lastOne == null;
                if (sucMap != null) {
                    Integer[] sucArr = sucMap.get(mapK);
                    if (sucArr != null && sucArr[0] > one.getOutOrder()) {
                        one.setTotalOutOrder(lastOneNull ? one.getTotalOutOrder() + (sucArr[0] - one.getOutOrder()) : lastOne.getTotalOutOrder() + sucArr[0]);
                        one.setOutOrder(sucArr[0]);
                        one.setFee(sucArr[1] - (reduc == null ? 0 : reduc.getReductionMoney()));
                        one.setReturnPrincipal(1000 * sucArr[0]);
                    } else {
                        one.setTotalOutOrder(lastOneNull ? one.getTotalOutOrder() : lastOne.getTotalOutOrder() + one.getOutOrder());
                    }
                } else {
                    one.setTotalOutOrder(lastOneNull ? one.getTotalOutOrder() : lastOne.getTotalOutOrder() + one.getOutOrder());
                }
                int renewalNum = renewalMap == null ? 0 : renewalMap.get(mapK) == null ? 0 : renewalMap.get(mapK);
                one.setRenewalCount(renewalNum);
                one.setUpdateDate(new Date());
                if (one.getTotalIntoOrder() > 0) {
                    one.setSucRate(new BigDecimal(one.getTotalOutOrder()).divide(new BigDecimal(one.getTotalIntoOrder()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue());
                } else {
                    one.setSucRate(0d);
                }
                one.setReturnPrincipal(one.getReturnPrincipal() - one.getRenewalCount() * 1000);
                performanceCountRecordDao.updateByPrimaryKeySelective(one);
            }
        }
    }

}
