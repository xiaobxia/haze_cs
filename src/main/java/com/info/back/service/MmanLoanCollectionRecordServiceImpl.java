package com.info.back.service;

import com.alibaba.fastjson.JSON;
import com.info.back.dao.*;
import com.info.back.exception.BusinessException;
import com.info.back.result.JsonResult;
import com.info.back.utils.BackConstant;
import com.info.back.utils.DecimalFormatUtil;
import com.info.back.utils.IdGen;
import com.info.back.vo.OdvRate;
import com.info.config.PayContents;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.*;
import com.info.web.util.HttpUtil;
import com.info.web.util.JsonUtil;
import com.info.web.util.PageConfig;
import com.info.web.util.encrypt.AesUtil;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class MmanLoanCollectionRecordServiceImpl implements IMmanLoanCollectionRecordService {

    @Resource
    private IMmanLoanCollectionRecordDao mmanLoanCollectionRecordDao;

    @Resource
    private IPaginationDao paginationDao;

    @Resource
    private IMmanLoanCollectionRuleDao mmanLoanCollectionRuleDao;

    @Resource
    private IAlertMsgService sysAlertMsgService;

    @Resource
    private ICreditLoanPayService creditLoanPayService;


    @Resource
    private IMmanLoanCollectionOrderService mmanLoanCollectionOrderService;

    @Resource
    private IBackUserDao backUserDao;

    @Resource
    private IMmanLoanCollectionStatusChangeLogDao mmanLoanCollectionStatusChangeLogDao;
    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;

    @Resource
    private IMmanUserInfoDao mmanUserInfoDao;
    @Resource
    private ICollectionWithholdingRecordDao collectionWithholdingRecordDao;
    @Resource
    private IInstallmentPayRecordDao iInstallmentPayRecordDao;
    @Resource
    private IOdvRateDao odvRateDao;
    @Resource
    private IBackUserService backUserService;


    @Override
    public PageConfig<MmanLoanCollectionRecord> findPage(
            HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "MmanLoanCollectionRecord");
        PageConfig<MmanLoanCollectionRecord> pageConfig;
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        return pageConfig;
    }


    private void addOrUpdateOrderAndAddStatusChangeLogAndUpdatePayStatus(MmanLoanCollectionPerson person,
                                                                         MmanLoanCollectionOrder mmanLoanCollectionOrder, Date date) {

        //添加催收流转日志
        MmanLoanCollectionStatusChangeLog mmanLoanCollectionStatusChangeLog = new MmanLoanCollectionStatusChangeLog();
        mmanLoanCollectionStatusChangeLog.setId(IdGen.uuid());
        mmanLoanCollectionStatusChangeLog.setLoanCollectionOrderId(mmanLoanCollectionOrder.getOrderId());
        mmanLoanCollectionStatusChangeLog.setOperatorName("系统");
        mmanLoanCollectionStatusChangeLog.setCreateDate(date);


        mmanLoanCollectionOrder.setCurrentCollectionUserId(person.getUserId());
        log.info("personLevel： " + JsonUtil.beanToJson(person));
        mmanLoanCollectionOrder.setCurrentOverdueLevel(person.getGroupLevel());

        if ("S1".equals(mmanLoanCollectionOrder.getS1Flag())) {//说明是S1,S2平分过来的单子
            if ("4".equals(person.getGroupLevel())) {
                mmanLoanCollectionOrder.setS1Flag("S1");//说明S1组的订单流转到了S2人员手上
            } else {
                mmanLoanCollectionOrder.setS1Flag(null);
            }
        }

        if (StringUtils.isBlank(mmanLoanCollectionOrder.getId())) {
            mmanLoanCollectionStatusChangeLog.setType(BackConstant.XJX_COLLECTION_STATUS_MOVE_TYPE_IN);//入催
            mmanLoanCollectionStatusChangeLog.setRemark("系统派单，催收人：" + person.getUsername() + "，手机：" + person.getPhone());
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserId(person.getUserId());
        } else {
            mmanLoanCollectionStatusChangeLog.setBeforeStatus(mmanLoanCollectionOrder.getStatus());
            mmanLoanCollectionStatusChangeLog.setType(BackConstant.XJX_COLLECTION_STATUS_MOVE_TYPE_CONVERT);//逾期等级转换
            mmanLoanCollectionStatusChangeLog.setRemark("逾期升级，系统重新派单,当前催收人：" + person.getUsername() + "，手机：" + person.getPhone());
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserId(person.getUserId());
        }

        //添加或更新催收订单
        //催收公司和状态这里统一设置或统一重置（升级的单子无论原来什么状态，这里都会重置！），根据当前分配到的催收员所在公司而定，状态为本公司待催收，委外公司委外中
        mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserLevel(person.getGroupLevel());
        mmanLoanCollectionOrder.setOutsideCompanyId(person.getCompanyId());
//        if ("1".equals(mmanLoanCollectionOrder.getOutsideCompanyId())) {
//            mmanLoanCollectionOrder.setStatus(BackConstant.XJX_COLLECTION_ORDER_STATE_WAIT);
//        } else {
//            mmanLoanCollectionOrder.setStatus(BackConstant.XJX_COLLECTION_ORDER_STATE_OUTSIDE);
//        }


        if (BackConstant.XJX_OVERDUE_LEVEL_S1.equals(person.getGroupLevel())) {
            mmanLoanCollectionOrder.setM1ApproveId(person.getUserId());
            mmanLoanCollectionOrder.setM1OperateStatus(BackConstant.OFF);
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_S1);
        } else if (BackConstant.XJX_OVERDUE_LEVEL_S2.equals(person.getGroupLevel())) {
            mmanLoanCollectionOrder.setM2ApproveId(person.getUserId());
            mmanLoanCollectionOrder.setM2OperateStatus(BackConstant.OFF);
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_S2);
            if ("S1".equals(mmanLoanCollectionOrder.getS1Flag()) && mmanLoanCollectionOrder.getOverdueDays() <= 10) {
                mmanLoanCollectionOrder.setM2ApproveId(null);
                mmanLoanCollectionOrder.setM1ApproveId(person.getUserId());
                mmanLoanCollectionOrder.setM1OperateStatus(BackConstant.OFF);
                mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_S1);
            }

        } else if (BackConstant.XJX_OVERDUE_LEVEL_M1_M2.equals(person.getGroupLevel())) {
            mmanLoanCollectionOrder.setM3ApproveId(person.getUserId());
            mmanLoanCollectionOrder.setM3OperateStatus(BackConstant.OFF);

            mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_M1_M2);

        } else if (BackConstant.XJX_OVERDUE_LEVEL_M2_M3.equals(person.getGroupLevel())) {
            mmanLoanCollectionOrder.setM4ApproveId(person.getUserId());
            mmanLoanCollectionOrder.setM4OperateStatus(BackConstant.OFF);

            mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_M2_M3);
        } else {
            mmanLoanCollectionOrder.setM5ApproveId(person.getUserId());
            mmanLoanCollectionOrder.setM5OperateStatus(BackConstant.OFF);

            mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(BackConstant.XJX_OVERDUE_LEVEL_M3P);
        }

        mmanLoanCollectionStatusChangeLog.setRemark(mmanLoanCollectionStatusChangeLog.getRemark() + ",催收组：" + BackConstant.GROUP_NAME_MAP.get(mmanLoanCollectionStatusChangeLog.getCurrentCollectionOrderLevel()));
        mmanLoanCollectionStatusChangeLog.setAfterStatus(mmanLoanCollectionOrder.getStatus());
        mmanLoanCollectionStatusChangeLog.setCompanyId(mmanLoanCollectionOrder.getOutsideCompanyId());

        mmanLoanCollectionStatusChangeLogDao.insert(mmanLoanCollectionStatusChangeLog);

        //更新还款状态
        CreditLoanPay creditLoanPay = creditLoanPayService.findByLoanId(mmanLoanCollectionOrder.getLoanId());
        creditLoanPay.setStatus(Integer.parseInt(person.getGroupLevel()));
        creditLoanPayService.save(creditLoanPay);
        MmanLoanCollectionOrder param = new MmanLoanCollectionOrder();
        param.setLoanId(mmanLoanCollectionOrder.getLoanId());
        List<MmanLoanCollectionOrder> order = mmanLoanCollectionOrderService.getOrderList(param);
        if (order != null && !CollectionUtils.isEmpty(order)) {
            mmanLoanCollectionOrder.setUpdateDate(new Date());
            mmanLoanCollectionOrder.setId(order.get(0).getId());
            mmanLoanCollectionOrderService.updateRecord(mmanLoanCollectionOrder);
        } else {
            mmanLoanCollectionOrderService.saveMmanLoanCollectionOrder(mmanLoanCollectionOrder);
        }
        log.info("mmanLoanCollectionOrder:" + mmanLoanCollectionOrder);
    }

    public List<MmanLoanCollectionRecord> findAll(HashMap<String, Object> params) {
        return mmanLoanCollectionRecordDao.findAll(params);
    }

    @Override
    public void insert(MmanLoanCollectionRecord record) {
        mmanLoanCollectionRecordDao.insert(record);
    }

    @Override
    public void update(MmanLoanCollectionRecord record) {
        mmanLoanCollectionRecordDao.update(record);

    }

    @Override
    public MmanLoanCollectionRecord getOne(HashMap<String, Object> params) {
        List<MmanLoanCollectionRecord> list = this.findAll(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<MmanLoanCollectionRecord> findListRecord(String orderid) {
        return mmanLoanCollectionRecordDao.findListRecord(orderid);
    }

    @Override
    public JsonResult saveCollection(Map<String, String> params, BackUser user) throws BusinessException {
//        String fenkongStr = "fengkongIds";
//        if (StringUtils.isEmpty(params.get(fenkongStr))) {
//            throw new BusinessException("-1", "请选择风控标签!");
//        }
        JsonResult result = new JsonResult("0", "添加成功");
        if (user != null) {
            //更新我的催收订单
            Date now = new Date();
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(params.get("id"));

            MmanLoanCollectionOrder mmanLoanCollectionOrder = new MmanLoanCollectionOrder();
            if (mmanLoanCollectionOrderOri != null && !BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(mmanLoanCollectionOrderOri.getStatus())) {
                //不填承诺还款时间为催收中
                String repayStr = "repaymentTime";
                if (params.get(repayStr) == null || Objects.equals(params.get(repayStr), "")) {
                    mmanLoanCollectionOrder.setStatus(BackConstant.XJX_COLLECTION_ORDER_STATE_ING);
                    mmanLoanCollectionOrder.setPromiseRepaymentTime(null);
                } else {
                    mmanLoanCollectionOrder.setStatus(BackConstant.XJX_COLLECTION_ORDER_STATE_PROMISE);
                    mmanLoanCollectionOrder.setPromiseRepaymentTime(com.info.web.util.DateUtil.formatDate(params.get(repayStr), "yyyy-MM-dd"));
                }
            }
            mmanLoanCollectionOrder.setLastCollectionTime(now);
            mmanLoanCollectionOrder.setOperatorName(StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : "");
            //根据等级设置当前催收员某等级操作状态，1代表操作过催收单
            switch (user.getGroupLevel()) {
                case BackConstant.XJX_OVERDUE_LEVEL_S1:
                    mmanLoanCollectionOrder.setM1OperateStatus(BackConstant.ON);
                    break;
                case BackConstant.XJX_OVERDUE_LEVEL_S2:
                    if (Constant.S_FLAG.equals(mmanLoanCollectionOrderOri.getS1Flag()) && mmanLoanCollectionOrderOri.getOverdueDays() <= 10) {
                        mmanLoanCollectionOrder.setM1OperateStatus(BackConstant.ON);
                    } else {
                        mmanLoanCollectionOrder.setM2OperateStatus(BackConstant.ON);
                    }
                    break;
                case BackConstant.XJX_OVERDUE_LEVEL_M1_M2:
                    mmanLoanCollectionOrder.setM3OperateStatus(BackConstant.ON);
                    break;
                case BackConstant.XJX_OVERDUE_LEVEL_M2_M3:
                    mmanLoanCollectionOrder.setM4OperateStatus(BackConstant.ON);
                    break;
                default:
                    mmanLoanCollectionOrder.setM5OperateStatus(BackConstant.ON);
                    break;
            }
            mmanLoanCollectionOrder.setUpdateDate(now);
            mmanLoanCollectionOrder.setId(mmanLoanCollectionOrderOri.getId());
            /*已续期则保持状态不变*/
            mmanLoanCollectionOrder.setCareStatus(BackConstant.XJX_COLLECTION_ORDER_STATE_PAYING);
            mmanLoanCollectionOrderService.updateRecord(mmanLoanCollectionOrder);
            MmanLoanCollectionRecord mmanLoanCollectionRecord = new MmanLoanCollectionRecord();
            //添加催收记录
            mmanLoanCollectionRecord.setStressLevel(params.get("stressLevel"));
            mmanLoanCollectionRecord.setCollectionType(params.get("collectionType"));
            mmanLoanCollectionRecord.setContent(params.get("content"));
            mmanLoanCollectionRecord.setRemark(params.get("remark"));
            mmanLoanCollectionRecord.setContactType(params.get("contactType") == null ? "" : params.get("contactType"));
            mmanLoanCollectionRecord.setContactName(params.get("contactName"));
            mmanLoanCollectionRecord.setRelation(params.get("relation"));
            mmanLoanCollectionRecord.setContactPhone(params.get("contactPhone"));
            mmanLoanCollectionRecord.setCollectionDate(now);
            mmanLoanCollectionRecord.setOrderId(mmanLoanCollectionOrder.getId());
            mmanLoanCollectionRecord.setCollectionId(user.getUuid());
            mmanLoanCollectionRecord.setUserId(mmanLoanCollectionOrderOri.getUserId());

            mmanLoanCollectionRecord.setOrderState(mmanLoanCollectionOrderOri.getStatus());

            mmanLoanCollectionRecord.setId(IdGen.uuid());
            mmanLoanCollectionRecord.setCreateDate(now);
            mmanLoanCollectionRecord.setUpdateDate(now);
            mmanLoanCollectionRecordDao.insert(mmanLoanCollectionRecord);
        } else {
            result.setCode("-1");
            result.setMsg("登录失效,请重新登录");
        }
        return result;
    }

    /**
     * 转派
     */
    @Override
    public JsonResult batchDispatch(BackUser user, MmanLoanCollectionOrder mmanLoanCollectionOrder) {
        JsonResult result = new JsonResult("-1", "转派失败，未知异常");
        HashMap<String, String> resutMap = new HashMap<>();
        //更新我的催收订单
        String currentCollectionUserId = mmanLoanCollectionOrder.getCurrentCollectionUserId();
        String ids = mmanLoanCollectionOrder.getId();
        String[] orderIds = ids.split(",");
        if (orderIds != null && orderIds.length > 0) {
            int successCount = 0;

            for (String orderId : orderIds) {
                //原始催收订单
                MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(orderId);
                try {
                    if (!"4".equals(mmanLoanCollectionOrderOri.getStatus())) {
                        if (!currentCollectionUserId.equals(mmanLoanCollectionOrderOri.getCurrentCollectionUserId())) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("currentCollectionUserId", currentCollectionUserId);
                            params.put("orderId", orderId);
                            //判断催收员是否转派过改单
                            int countSinge = mmanLoanCollectionStatusChangeLogDao.findOrderSingle(params);
                            if (countSinge <= 0) {
                                //当前催收人
                                BackUser buc = backUserDao.getBackUserByUuid(currentCollectionUserId);
                                params.put("companyId", buc.getCompanyId());
                                params.put("grouplevel", buc.getGroupLevel());
                                //查询该公催收每日订单上线
                                Integer limitCount = mmanLoanCollectionRuleDao.findCompanyGoupOnline(params);
                                MmanLoanCollectionPerson person = new MmanLoanCollectionPerson();
                                person.setId(buc.getId() + "");
                                //查询当前催收员今日派到手里的订单数(包括已完成的)
                                Integer todayOrder = backUserDao.findTodayAssignedCount(person);//查询当前催收员今日派到手里的订单数(包括已完成的)
                                if (limitCount > todayOrder) {
                                    //原始催收人
                                    BackUser bu = backUserDao.getBackUserByUuid(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());
                                    if (bu.getGroupLevel().equals(buc.getGroupLevel())) {
                                        MmanLoanCollectionStatusChangeLog mmanLoanCollectionStatusChangeLog = new MmanLoanCollectionStatusChangeLog();
                                        //催收订单状态
                                        String beforeStatus = mmanLoanCollectionOrderOri.getStatus();
                                        //公司内部转派待催收
                                        mmanLoanCollectionOrderOri.setStatus(beforeStatus);
                                        //转单
                                        mmanLoanCollectionStatusChangeLog.setType(BackConstant.XJX_COLLECTION_STATUS_MOVE_TYPE_OTHER);
                                        String currentOverdueLevel = buc.getGroupLevel();
                                        //转派后要将当前级催收状态初始化
                                        if (BackConstant.XJX_OVERDUE_LEVEL_S1.equals(currentOverdueLevel)) {
                                            mmanLoanCollectionOrderOri.setM1ApproveId(currentCollectionUserId);
                                            mmanLoanCollectionOrderOri.setM1OperateStatus(BackConstant.OFF);
                                        } else if (BackConstant.XJX_OVERDUE_LEVEL_S2.equals(currentOverdueLevel)) {
                                            mmanLoanCollectionOrderOri.setM2ApproveId(currentCollectionUserId);
                                            mmanLoanCollectionOrderOri.setM2OperateStatus(BackConstant.OFF);
                                        } else if (BackConstant.XJX_OVERDUE_LEVEL_M1_M2.equals(currentOverdueLevel)) {
                                            mmanLoanCollectionOrderOri.setM3ApproveId(currentCollectionUserId);
                                            mmanLoanCollectionOrderOri.setM3OperateStatus(BackConstant.OFF);
                                        } else if (BackConstant.XJX_OVERDUE_LEVEL_M2_M3.equals(currentOverdueLevel)) {
                                            mmanLoanCollectionOrderOri.setM4ApproveId(currentCollectionUserId);
                                            mmanLoanCollectionOrderOri.setM4OperateStatus(BackConstant.OFF);
                                        } else {
                                            mmanLoanCollectionOrderOri.setM5ApproveId(currentCollectionUserId);
                                            mmanLoanCollectionOrderOri.setM5OperateStatus(BackConstant.OFF);
                                        }
                                        mmanLoanCollectionOrderOri.setCurrentOverdueLevel(currentOverdueLevel);
                                        //上一催收员
                                        mmanLoanCollectionOrderOri.setLastCollectionUserId(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());
                                        mmanLoanCollectionOrderOri.setCurrentCollectionUserId(currentCollectionUserId);
                                        mmanLoanCollectionOrderOri.setOutsideCompanyId(mmanLoanCollectionOrder.getOutsideCompanyId());
                                        mmanLoanCollectionOrderOri.setOperatorName(StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : "");
                                        mmanLoanCollectionOrderOri.setDispatchName(StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : "");
                                        mmanLoanCollectionOrderOri.setDispatchTime(new Date());
                                        mmanLoanCollectionOrderOri.setRemark("[" + bu.getUserName() + "]转派给[" + buc.getUserName() + "]");

                                        //更新聚信立报告申请审核状态为初始状态，下一催收员要看需要重新申请
                                        mmanLoanCollectionOrderOri.setJxlStatus(BackConstant.XJX_JXL_STATUS_REFUSE);
                                        Date now = new Date();
                                        mmanLoanCollectionOrderOri.setUpdateDate(now);
                                        mmanLoanCollectionOrderDao.updateCollectionOrder(mmanLoanCollectionOrderOri);
                                        //添加转派记录
                                        mmanLoanCollectionStatusChangeLog.setLoanCollectionOrderId(mmanLoanCollectionOrderOri.getOrderId());
                                        mmanLoanCollectionStatusChangeLog.setCompanyId(mmanLoanCollectionOrderOri.getOutsideCompanyId());
                                        mmanLoanCollectionStatusChangeLog.setBeforeStatus(beforeStatus);
                                        mmanLoanCollectionStatusChangeLog.setAfterStatus(mmanLoanCollectionOrderOri.getStatus());
                                        mmanLoanCollectionStatusChangeLog.setOperatorName(StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : "");
                                        mmanLoanCollectionStatusChangeLog.setRemark("转单，催收人：" + buc.getUserName() + "，手机：" + buc.getUserMobile());
                                        mmanLoanCollectionStatusChangeLog.setId(IdGen.uuid());
                                        mmanLoanCollectionStatusChangeLog.setCreateDate(now);
                                        //订单转派后的催收人
                                        mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserId(buc.getUuid());
                                        mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(mmanLoanCollectionOrderOri.getCurrentOverdueLevel());
                                        mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserLevel(buc.getGroupLevel());
                                        mmanLoanCollectionStatusChangeLogDao.insert(mmanLoanCollectionStatusChangeLog);
                                        successCount++;
                                    } else {
                                        resutMap.put("sameGroup", "只能同组之间转派");
                                    }
                                } else {
                                    resutMap.put("todayOrder", "已超过催收员每日上线,");
                                    break;
                                }
                            } else {
                                resutMap.put("countSinge", "订单在当前催收员手上有转派过,");
                            }
                        } else {
                            resutMap.put("backUser", "自己不能转给自己");
                        }
                    } else {
                        resutMap.put("orderStatus", "催收成功的订单不能转派");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (successCount == orderIds.length) {
                result.setMsg("转派成功");
                result.setCode("0");
            } else {
                StringBuilder resultStr = new StringBuilder();
                if (resutMap.get("currDateMesg") != null) {
                    resultStr.append(resutMap.get("currDateMesg"));
                }
                if (resutMap.get("orderStatus") != null) {
                    resultStr.append(resutMap.get("orderStatus"));
                }
                if (resutMap.get("countSinge") != null) {
                    resultStr.append(resutMap.get("countSinge"));
                }
                if (resutMap.get("backUser") != null) {
                    resultStr.append(resutMap.get("backUser"));
                }
                if (resutMap.get("todayOrder") != null) {
                    resultStr.append(resutMap.get("todayOrder"));
                }
                if (resutMap.get("sameGroup") != null) {
                    resultStr.append(resutMap.get("sameGroup"));
                }
                result.setMsg("总单:" + orderIds.length + "，转派成功：" + successCount + "  失败原因：" + resultStr.toString());
            }
        } else {
            result.setMsg("请选择需要转派的订单");
        }
        return result;
    }

    @Override
    public JsonResult xjxWithholding(Map<String, String> params) {
        //原催收订单
        MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(params.get("id"));
        JsonResult reslut = new JsonResult("-1", "申请代扣款失败");
        try {
            if (mmanLoanCollectionOrderOri != null) {
                List<CollectionWithholdingRecord> recordList = collectionWithholdingRecordDao.findOrderList(params.get("id"));
                if (!"5".equals(mmanLoanCollectionOrderOri.getStatus())) {
                    if (CollectionUtils.isEmpty(recordList) || System.currentTimeMillis() > getCreateTimePlus(recordList)) {
                        CreditLoanPay creditLoanPay = creditLoanPayService.get(mmanLoanCollectionOrderOri.getPayId());
                        //扣款金额
                        String payMonery = params.get("payMoney");
                        BigDecimal koPayMonery = new BigDecimal(0);
                        BigDecimal maxpayMonery = creditLoanPay.getReceivablePrinciple().add(creditLoanPay.getReceivableInterest());
                        if (payMonery == null || "".equals(payMonery) || !com.info.web.util.CompareUtils.greaterThanZero(new BigDecimal(payMonery))) {
                            koPayMonery = creditLoanPay.getReceivablePrinciple().add(creditLoanPay.getReceivableInterest());
                        } else {
                            koPayMonery = new BigDecimal(params.get("payMoney"));
                        }
                        if (com.info.web.util.CompareUtils.greaterEquals(maxpayMonery, koPayMonery)) {
                            HashMap<String, String> dayMap = new HashMap<>();
                            dayMap.put("orderId", params.get("id").toString());
                            dayMap.put("currDate", com.info.web.util.DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
                            //查询当天定单代扣次数
                            int count = collectionWithholdingRecordDao.findCurrDayWithhold(dayMap);
                            log.error("当前roleId: " + String.valueOf(params.get("roleId")));
                            //超级管理员，催收经理 不受权限控制
                            if (Constant.ROLE_ID.equals(String.valueOf(params.get("roleId"))) || "10001".equals(String.valueOf(params.get("roleId")))) {
                                count = 0;
                            }
                            log.error("当前次数count:" + count);
                            if (count < 5) {
                                long actualPayMonery = koPayMonery.multiply(new BigDecimal(100)).longValue();
                                String uuid = IdGen.uuid();
                                String sign = Md5coding.md5(AesUtil.encrypt(mmanLoanCollectionOrderOri.getUserId() + mmanLoanCollectionOrderOri.getPayId() + actualPayMonery + uuid, PayContents.XJX_WITHHOLDING_NOTIFY_KEY));
                                //2、发送请求
                                String withholdPostUrl = PayContents.XJX_WITHHOLDING_NOTIFY_URL + "/" + mmanLoanCollectionOrderOri.getUserId() + "/" + mmanLoanCollectionOrderOri.getPayId() + "/" + actualPayMonery + "/" + uuid + "/" + sign;
                                log.error("小鱼儿代扣请求地址：" + withholdPostUrl);
                                String xjxWithholdingStr = HttpUtil.getHttpMess(withholdPostUrl, "", "POST", "UTF-8");
                                //3、解析响应结果封装到Java Bean
                                if (xjxWithholdingStr != null && !"".equals(xjxWithholdingStr)) {
                                    JSONObject jos = JSONObject.fromObject(xjxWithholdingStr);
                                    if (!"-100".equals(jos.get("code"))) {
                                        CollectionWithholdingRecord withholdingrecord = new CollectionWithholdingRecord();
                                        MmanUserInfo userInfo = mmanUserInfoDao.get(mmanLoanCollectionOrderOri.getUserId());
                                        withholdingrecord.setLoanUserId(userInfo.getId());
                                        withholdingrecord.setId(uuid);
                                        withholdingrecord.setLoanUserName(userInfo.getRealname());
                                        withholdingrecord.setLoanUserPhone(userInfo.getUserPhone());
                                        withholdingrecord.setOrderId(mmanLoanCollectionOrderOri.getId());
                                        withholdingrecord.setCreateDate(new Date());

                                        withholdingrecord.setArrearsMoney(DecimalFormatUtil.df2Points.format(creditLoanPay.getReceivableMoney()));
                                        withholdingrecord.setHasalsoMoney(creditLoanPay.getRealMoney().toString());
                                        withholdingrecord.setOperationUserId(params.get("operationUserId"));
                                        withholdingrecord.setDeductionsMoney(payMonery);
                                        withholdingrecord.setOrderStatus(mmanLoanCollectionOrderOri.getStatus());
                                        if ("0".equals(jos.get("code")) || "100".equals(jos.get("code"))) {
                                            //扣款成功要更新操作人，由于代扣成功时会有接口更新订单、借款、还款、详情等数据，所以这里千万不能更新mmanLoanCollectionOrderOri，因为这里的订单状态还是原始状态！！！
                                            MmanLoanCollectionOrder mmanLoanCollectionOrderNow = new MmanLoanCollectionOrder();
                                            mmanLoanCollectionOrderNow.setId(mmanLoanCollectionOrderOri.getId());
                                            mmanLoanCollectionOrderNow.setOperatorName(params.get("userName"));
                                            if (BackConstant.XJX_COLLECTION_ORDER_STATE_WAIT.equals(mmanLoanCollectionOrderOri.getStatus())) {
                                                mmanLoanCollectionOrderNow.setStatus(BackConstant.XJX_COLLECTION_ORDER_STATE_ING);
                                            }
                                            mmanLoanCollectionOrderService.updateRecord(mmanLoanCollectionOrderNow);
                                            if ("0".equals(jos.get("code"))) {
                                                withholdingrecord.setStatus(1);
                                            } else {
                                                withholdingrecord.setStatus(0);
                                            }
                                            reslut.setMsg("申请代扣成功");
                                            reslut.setCode("0");
                                        } else {
                                            reslut.setMsg(jos.getString("msg"));
                                            withholdingrecord.setStatus(2);
                                        }

                                        //添加一条扣款记录
                                        collectionWithholdingRecordDao.insert(withholdingrecord);
                                    } else {
                                        reslut.setMsg("申请代扣失败,失败编码-100");
                                    }
                                    log.error("小鱼儿代扣返回：" + xjxWithholdingStr);
                                }
                            } else {
                                reslut.setMsg("每笔订单每天代扣次数不能超过三次");
                            }
                        } else {
                            reslut.setMsg("代扣金额不能大于" + creditLoanPay.getReceivablePrinciple().add(creditLoanPay.getReceivableInterest()));
                        }
                    } else {
                        reslut.setMsg("代扣过于频繁,请稍等(间隔时间为5分钟)");
                    }
                } else {
                    reslut.setMsg("续期订单不允许代扣！！");
                }
            } else {
                reslut.setMsg("该订单不存在");
            }
        } catch (Exception e) {
            log.error("代扣异常：", e);
        }
        return reslut;
    }

    private long getCreateTimePlus(List<CollectionWithholdingRecord> recordList) {
        //最新一条代扣时间
        long createTime = recordList.get(0).getCreateDate().getTime();
        //新增5分钟
        return createTime + 5 * 60 * 1000;
    }

    @Override
    public List<CollectionWithholdingRecord> findWithholdRecord(String id) {
        return collectionWithholdingRecordDao.findOrderList(id);
    }

    @Override
    public JsonResult insertInstallmentPayRecord(List<InstallmentPayInfoVo> list, MmanLoanCollectionOrder mmanLoanCollectionOrderOri) {
        JsonResult jsonResult = new JsonResult();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<InstallmentPayRecord> recordsList = new ArrayList<>();
        for (InstallmentPayInfoVo installmentPayInfoVo : list) {
            InstallmentPayRecord installmentPayRecord = new InstallmentPayRecord();
            installmentPayRecord.setId(IdGen.uuid());
            installmentPayRecord.setRepayTime(installmentPayInfoVo.getRepayTime());
            installmentPayRecord.setDateNew(formatter.format(installmentPayInfoVo.getRepayTime()));
            installmentPayRecord.setCreateTime(new Date());
            installmentPayRecord.setRepayMoney(installmentPayInfoVo.getTotalRepay());
            installmentPayRecord.setLoanOrderId(mmanLoanCollectionOrderOri.getId());
            installmentPayRecord.setLoanUserName(mmanLoanCollectionOrderOri.getLoanUserName());
            installmentPayRecord.setLoanUserPhone(mmanLoanCollectionOrderOri.getLoanUserPhone());
            installmentPayRecord.setRepayBatches(installmentPayInfoVo.getInstallmentType() + "还款");
            if (installmentPayInfoVo.getServiceCharge() != null) {
                //还款成功
                installmentPayRecord.setRepayStatus("0");
                //无代扣
                installmentPayRecord.setOperationStatus("1");
            }
            recordsList.add(installmentPayRecord);
            iInstallmentPayRecordDao.insert(installmentPayRecord);

        }
        jsonResult.setData(recordsList);
        jsonResult.setCode("0");
        jsonResult.setMsg("分期创建成功");
        return jsonResult;
    }

    @Override
    public List<InstallmentPayRecord> findInstallmentList(String id) {
        return iInstallmentPayRecordDao.findInstallmentList(id);
    }

    @Override
    public void assignCollectionOrderToRelatedGroup(
            List<MmanLoanCollectionOrder> mmanLoanCollectionOrderList,
            List<MmanLoanCollectionPerson> mmanLoanCollectionPersonList, Date date) {

        if (null != mmanLoanCollectionOrderList && mmanLoanCollectionOrderList.size() > 0) {

            //2.1 查询当前组中所有非禁用的催收员，按照截止到当前手里未处理的订单数升序排序(前面已查)，并查出他们组每人每天单数上限(上限规则中公司+组唯一)，取出有效催收员
            List<MmanLoanCollectionRule> allRuleList = mmanLoanCollectionRuleDao.findList(new MmanLoanCollectionRule());
            HashMap<String, Integer> allRuleLimitCountMap = new HashMap<>();
            if (null != allRuleList && allRuleList.size() > 0) {
                for (MmanLoanCollectionRule ruleOri : allRuleList) {
                    allRuleLimitCountMap.put(ruleOri.getCompanyId() + "_" + ruleOri.getCollectionGroup(), ruleOri.getEveryLimit());
                }
            }
            log.info("allRuleLimitCountMap:" + allRuleLimitCountMap);

            HashMap<String, Object> personMap = new HashMap<>(2);
            MmanLoanCollectionPerson person = mmanLoanCollectionPersonList.get(0);
            log.info("assignCollectionOrderToRelatedGroup person:{}", JSON.toJSONString(person));
            /*根据grouplevel查询是否手动分配派单比例*/
            MmanLoanCollectionRule rule = mmanLoanCollectionRuleDao.selectBycollectionGroup(person.getGroupLevel(), person.getCompanyId());
            if (rule != null && OdvRate.HAND.equals(rule.getAssignType())) {
                for (MmanLoanCollectionOrder order : mmanLoanCollectionOrderList) {
                    int odvId = getSectionOdvId(person);
                    personMap.put("userId", odvId + "");
                    MmanLoanCollectionPerson effectivePerson = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap).get(0);
                    //派单方法：添加或更新催收订单、添加催收流转日志并更新还款状态
                    addOrUpdateOrderAndAddStatusChangeLogAndUpdatePayStatus(effectivePerson, order, date);
                }
            } else {
                assignAvg(allRuleLimitCountMap, mmanLoanCollectionPersonList, mmanLoanCollectionOrderList, date);
            }

        }
    }

    /**
     * 生成1-100随机数并判断区间以分单
     *
     * @param person 任意催收员
     */
    private int getSectionOdvId(MmanLoanCollectionPerson person) {
        int randomNum = (int) (Math.random() * 100) + 1;
        Map<Integer, Integer[]> ratesMap = getSectionMap(person);
        Integer[] result = {0};
        ratesMap.forEach((k, v) -> {
            if (randomNum >= v[0] && randomNum <= v[1]) {
                result[0] = k;
                return;
            }
        });
        return result[0];
    }

    private Map<Integer, Integer> getRateOrders(int total, MmanLoanCollectionPerson person) {
        /*记录小数位map*/
        Map<Integer, Integer> decimalMap = new HashMap<>();
        Map<Integer, Integer> result = new HashMap<>();
        List<OdvRate> odvRates = odvRateDao.selectByGroupLevel(Integer.valueOf(person.getGroupLevel()));
        int count = 0;
        for (OdvRate rate : odvRates) {
            if (rate.getAssignRate() > 0.0) {
                String[] nums = (rate.getAssignRate() * total + "").split(".");
                Integer odvId = rate.getOdvId();
                result.put(odvId, Integer.valueOf(nums[0]));
                decimalMap.put(odvId, Integer.valueOf(nums[1].substring(0, 1)));
                count += Integer.valueOf(nums[0]);
            }
        }
        int left = total - count;
        if (left > 0) {
            List<Map.Entry<Integer, Integer>> list = new ArrayList<>(decimalMap.entrySet());
            list.sort(Comparator.comparing(Map.Entry::getValue));
            Collections.reverse(list);
            for (Map.Entry<Integer, Integer> entry : list) {
                Integer key = entry.getKey();
                Integer value = result.get(entry.getKey()) + 1;
                result.put(key, value);
                left--;
                if (left == 0) {
                    break;
                }
            }
        }

        return result;
    }


    /**
     * 平均分单
     */
    private void assignAvg(HashMap<String, Integer> allRuleLimitCountMap, List<MmanLoanCollectionPerson> mmanLoanCollectionPersonList,
                           List<MmanLoanCollectionOrder> mmanLoanCollectionOrderList, Date date) {
        //开始分配前,先筛选出有效催收员(手里单子未超出上限的催收员),查询并设置每个催收员今日派到手里的订单数(包括已完成的)
        //当前公司_组
        String currentCompanyGroup = "";
        List<MmanLoanCollectionPerson> effectiveCollectionPersonList = new ArrayList<>();
        for (MmanLoanCollectionPerson person : mmanLoanCollectionPersonList) {
            //查询当前催收员今日派到手里的订单数(包括已完成的)
            Integer todayAssignedCount = backUserDao.findTodayAssignedCount(person);
            person.setTodayAssignedCount(todayAssignedCount);
            //当前催收组每人每天上限
            Integer limitCount = allRuleLimitCountMap.get(person.getCompanyId() + "_" + person.getGroupLevel());
            currentCompanyGroup = person.getCompanyName() + "_" + BackConstant.GROUP_NAME_MAP.get(person.getGroupLevel());
            if (limitCount == null) {
                limitCount = 0;
            }
            log.info("当前催收员手中单量:{},上限:{}",todayAssignedCount,limitCount);
            if (todayAssignedCount < limitCount) {
                effectiveCollectionPersonList.add(person);
            }
        }
        //2.2 采用多次均匀涂抹法（将待分配订单数按排好序的催收员，依次分配，最后一次内层循环会优先分配给手里待处理单子少的）派单(最多循环次数：ceilAvgCount * effectivePersonCount)
        if (effectiveCollectionPersonList.isEmpty()) {
            SysAlertMsg alertMsg = new SysAlertMsg();
            alertMsg.setTitle("分配催收任务失败");
            alertMsg.setContent("当前" + currentCompanyGroup + "组所有催收员催收规则上限不足，请抓紧调整！");
            alertMsg.setDealStatus(BackConstant.OFF);
            alertMsg.setStatus(BackConstant.OFF);
            alertMsg.setType(SysAlertMsg.TYPE_COMMON);
            sysAlertMsgService.insert(alertMsg);
            log.error("当前" + currentCompanyGroup + "组所有催收员催收规则上限不足，请抓紧调整...");
        } else {
            //待分配订单数
            int orderCount = mmanLoanCollectionOrderList.size();
            //当前可用催收员数
            int effectivePersonCount = effectiveCollectionPersonList.size();
            //平均订单数向上取整数
            int ceilAvgCount = new BigDecimal(orderCount).divide(new BigDecimal(effectivePersonCount), 0, BigDecimal.ROUND_CEILING).intValue();
            //外层循环次数（ceilAvgCount）
            int i = 0;
            //已分配的订单数（最大为orderCount）
            int j = 0;
            while (i < ceilAvgCount) {
                for (MmanLoanCollectionPerson effectivePerson : effectiveCollectionPersonList) {
                    //当前催收员
                    //这里再实时查询当前催收员今日派到手里的订单数(包括已完成的)，防止每天第一次派单会超过上限，因为这个时候effectivePerson.getTodayAssignedCount().intValue()一直是0
                    Integer todayAssignedCount = backUserDao.findTodayAssignedCount(effectivePerson);
                    //当前催收组每人每天上限
                    Integer limitCount = allRuleLimitCountMap.get(effectivePerson.getCompanyId() + "_" + effectivePerson.getGroupLevel());
                    if (limitCount == null) {
                        limitCount = 0;
                    }
                    //可以分配
                    if (todayAssignedCount < limitCount) {
                        if (j < orderCount) {
                            MmanLoanCollectionOrder order = mmanLoanCollectionOrderList.get(j);
                            try {
                                //派单方法：添加或更新催收订单、添加催收流转日志并更新还款状态
                                addOrUpdateOrderAndAddStatusChangeLogAndUpdatePayStatus(effectivePerson, order, date);
                            } catch (Exception e) {
                                log.error("分配当前催收任务出错，订单ID：" + order.getOrderId(), e);
                            }
                            j++;
                        } else {
                            //全部派单完成
                            return;
                        }
                    }
                }
                i++;
            }

            //最终订单数未分配完成，给一个通知
            if (j < orderCount) {
                SysAlertMsg alertMsg = new SysAlertMsg();
                alertMsg.setTitle("分配催收任务失败");
                alertMsg.setContent("当前" + currentCompanyGroup + "组，本次派单后出现催收规则上限不足，剩余" + (orderCount - j) + "单未派送，请及时调整。");
                alertMsg.setDealStatus(BackConstant.OFF);
                alertMsg.setStatus(BackConstant.OFF);
                alertMsg.setType(SysAlertMsg.TYPE_COMMON);
                sysAlertMsgService.insert(alertMsg);
                log.error("当前" + currentCompanyGroup + "组，本次派单后出现催收规则上限不足，剩余" + (orderCount - j) + "单未派送，请及时调整...");
            }
        }

    }

    /**
     * 获取各自比例并生成对应区间
     */
    private Map<Integer, Integer[]> getSectionMap(MmanLoanCollectionPerson person) {
        List<OdvRate> odvRates = odvRateDao.selectByGroupLevel(Integer.valueOf(person.getGroupLevel()));
        odvRates.forEach(one -> one.setAssignRate(one.getAssignRate() * 100));
        for (int i = 0; i < odvRates.size(); i++) {
            if (odvRates.get(i).getAssignRate().intValue() == 0) {
                odvRates.remove(odvRates.get(i));
            }
        }

        odvRates.stream().filter(one -> 0 != one.getAssignRate().intValue());
        odvRates.sort(Comparator.comparing(OdvRate::getAssignRate));
        Map<Integer, Integer[]> rateMap = new HashMap<>(odvRates.size());
        for (int i = 0; i < odvRates.size(); i++) {
            if (MapUtils.isEmpty(rateMap)) {
                rateMap.put(odvRates.get(i).getOdvId(), new Integer[]{1, odvRates.get(i).getAssignRate().intValue()});
            } else {
                Integer lastOneEnd = rateMap.get(odvRates.get(i - 1).getOdvId())[1];
                rateMap.put(odvRates.get(i).getOdvId(), new Integer[]{lastOneEnd + 1, odvRates.get(i).getAssignRate().intValue() + lastOneEnd - 1});
            }
        }
        return rateMap;
    }

}
