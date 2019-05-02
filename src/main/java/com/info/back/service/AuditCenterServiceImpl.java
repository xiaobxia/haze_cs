package com.info.back.service;

import com.info.back.dao.IAuditCenterDao;
import com.info.back.dao.ICreditLoanPayDao;
import com.info.back.dao.IMmanLoanCollectionOrderDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.result.JsonResult;
import com.info.back.utils.IdGen;
import com.info.config.PayContents;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.AuditCenter;
import com.info.web.pojo.cspojo.CreditLoanPay;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import com.info.web.util.HttpUtil;
import com.info.web.util.PageConfig;
import com.info.web.util.encrypt.AesUtil;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuditCenterServiceImpl implements IAuditCenterService {

    @Resource
    private IAuditCenterDao auditCenterDao;
    @Resource
    private IPaginationDao paginationDao;
    //订单
    @Resource
    private IMmanLoanCollectionOrderDao manLoanCollectionOrderDao;
    @Resource
    private ICreditLoanPayDao iCreditLoanPayDao;

    @Override
    public JsonResult svueAuditCenter(Map<String, String> params) {
        JsonResult result = new JsonResult("-1", "操作失败");
        MmanLoanCollectionOrder mmanLoanCollectionOrderOri = manLoanCollectionOrderDao.getOrderById(params.get("id"));
        AuditCenter auditCenter = new AuditCenter();
        if (StringUtils.isNotBlank(params.get("id"))) {
            auditCenter.setOrderid(mmanLoanCollectionOrderOri.getId());
            auditCenter.setLoanUserId(mmanLoanCollectionOrderOri.getUserId());

        }
        auditCenter.setId(IdGen.uuid());
        auditCenter.setCreatetime(new Date());
        auditCenter.setNote(params.get("note"));
        auditCenter.setType(params.get("type"));
        auditCenter.setOperationUserId(params.get("operationUserId"));
        auditCenter.setRemark(params.get("remark"));
        auditCenter.setStatus("0");
        int cont = auditCenterDao.saveUpdate(auditCenter);
        if (cont > 0) {
//			HashMap<String, Object> xjxparams =new HashMap<>();
//			params.put("id",auditCenter.getLoanUserId());
//			params.put("collection_advise", auditCenter.getNote());
//			String collectionAdviseUpdateResult = HttpUtil.dopostMap(PayContents.COLLECTION_ADVISE_UPDATE_URL, params);
            result.setMsg("操作成功");
            result.setCode("0");
        }
        return result;
    }

    @Override
    public PageConfig<AuditCenter> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "AuditCenter");
        PageConfig<AuditCenter> pageConfig;
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        return pageConfig;
    }

    @Override
    public JsonResult updateAuditCenter(Map<String, String> params) {
        JsonResult result = new JsonResult("-1", "审核失败");

        String ids = params.get("id");
        String[] orderIds = ids.split(",");

        if (orderIds.length > 20) {
            result.setMsg("请选择信息不要超过20条");
        } else {
            int jxlCount = 0;
            int csjyCount = 0;
            int jmCount = 0;
            // 申请中状态
            boolean markStatus = true;
            for (String orderId : orderIds) {
                AuditCenter auditCenter = auditCenterDao.findAuditId(orderId);
                if (auditCenter != null) {
                    //聚信立
                    if ("1".equals(auditCenter.getType())) {
                        jxlCount++;
                        //催收建议
                    } else if ("2".equals(auditCenter.getType())) {
                        csjyCount++;
                        //订单减免
                    } else if ("3".equals(auditCenter.getType())) {
                        jmCount++;
                    }

                    if (!"0".equals(auditCenter.getStatus()) && markStatus) {
                        // 非申请中状态
                        markStatus = false;
                    }
                }
            }
            log.info("updateAuditCenter jxlCount=" + jxlCount + " orderIds=" + orderIds.length + " csjyCount=" + csjyCount + " markStatus=" + markStatus + " id=" + params.get("id"));
            if (jxlCount == orderIds.length || csjyCount == orderIds.length || jmCount == orderIds.length) {
                if (markStatus) {
                    if (StringUtils.isNotBlank(params.get("id"))) {
                        int successCount = 0;
                        for (String orderId : orderIds) {
                            successCount = updateAuditStatus(params, result, successCount, orderId);
                        }
                        if (successCount == orderIds.length) {
                            result.setMsg("审核成功");
                            result.setCode("0");
                        } else {
                            result.setMsg("总单:" + orderIds.length + "，审核成功：" + successCount);
                        }
                        params.put("id", ids);
                    } else {
                        result.setMsg("请选择需要审核的信息");
                    }
                } else {
                    result.setMsg("申请中的状态才能去审核");
                }
            } else {
                result.setMsg("请选择审核类型相同的信息");
            }
        }
        return result;
    }

    private int updateAuditStatus(Map<String, String> params, JsonResult result, int successCount, String orderId) {
        log.info("updateAuditCenter id=" + params.get("id") + " status=" + params.get("status") + " orderId=" + orderId);
        if (StringUtils.isNotBlank(params.get("id")) && StringUtils.isNotBlank(params.get("status"))) {
            AuditCenter auditCenter = auditCenterDao.findAuditId(orderId);
            if (auditCenter != null) {
                auditCenter.setStatus(params.get("status"));
                params.put("id", orderId);
                auditCenterDao.updateStatus(params);
                HashMap<String, String> auditMap = new HashMap<>();
                auditMap.put("id", auditCenter.getOrderid());
                if ("2".equals(auditCenter.getType())) {
                    auditMap.put("csstatus", params.get("status"));
                    manLoanCollectionOrderDao.updateAuditStatus(auditMap);
                    if ("2".equals(auditCenter.getStatus())) {
                        params.put("id", auditCenter.getLoanUserId());
                        params.put("collection_advise", auditCenter.getNote());
                        String resultT = HttpUtil.doPost(PayContents.COLLECTION_ADVISE_UPDATE_URL, params);
                        log.info("updateAuditStatus COLLECTION_ADVISE_UPDATE_URL=" + PayContents.COLLECTION_ADVISE_UPDATE_URL + " params=" + params.toString() + " resultT=" + resultT);
                    }

                } else if ("1".equals(auditCenter.getType())) {
                    auditMap.put("jxlStatus", params.get("status"));
                    manLoanCollectionOrderDao.updateAuditStatus(auditMap);
                    //特殊原因
                } else if ("3".equals(auditCenter.getType()) && ("2".equals(auditCenter.getStatus()) || "5".equals(auditCenter.getStatus()))) {
                    auditMap.put("status", params.get("status"));
                    if ("2".equals(auditMap.get("status"))) {
                        auditMap.put("status", "7");
                        manLoanCollectionOrderDao.sveUpdateJmStatus(auditMap);
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("orderId", auditCenter.getOrderid());
//                        map.put("reductionMoney", auditCenter.getReductionMoney());
//                        manLoanCollectionOrderDao.updateReductionMoney(map);
                    }
                    if ("5".equals(auditCenter.getStatus())) {
                        auditMap.put("status", "7");
                        manLoanCollectionOrderDao.sveUpdateJmStatus(auditMap);
                        HashMap<String, Object> aumap = new HashMap<>();
                        aumap.put("orderId", auditCenter.getOrderid());
                        aumap.put("reductionMoney", 0);
                        manLoanCollectionOrderDao.updateReductionMoney(aumap);
                    }
                    CreditLoanPay loanPay = iCreditLoanPayDao.get(auditCenter.getPayId());
                    //减免滞纳金
                    int reductionMoney = Integer.parseInt(String.valueOf(auditCenter.getReductionMoney()));
                    //应还金额
                    int receivablemoney = loanPay.getReceivableMoney().intValue();
                    //实收金额
                    int realMoney = loanPay.getRealMoney().intValue();
                    if (realMoney >= receivablemoney - reductionMoney && realMoney < receivablemoney) {
                        reductionMoney = receivablemoney - realMoney;
                        log.info("reductionMoney =" + reductionMoney);
                        String sign = Md5coding.md5(AesUtil.encrypt(auditCenter.getLoanUserId() + auditCenter.getPayId() + reductionMoney * 100 + auditCenter.getId(), PayContents.XJX_WITHHOLDING_NOTIFY_KEY));
                        log.info("reductionMoney =" + reductionMoney * 100);
                        //2、发起请求
                        String withholdPostUrl = PayContents.XJX_JIANMIAN_URL + "/" + auditCenter.getLoanUserId() + "/" + auditCenter.getPayId() + "/" + reductionMoney * 100 + "/" + auditCenter.getId() + "/" + sign;
                        log.info("减免请求地址：" + withholdPostUrl);
                        String xjxWithholdingStr = HttpUtil.getHttpMess(withholdPostUrl, "", "POST", "UTF-8");
                        if (StringUtils.isNotBlank(xjxWithholdingStr)) {
                            JSONObject jos = JSONObject.fromObject(xjxWithholdingStr);
                            log.info("返回还款结果信息jos转换" + jos);
                            if (!"-100".equals(jos.get("code"))) {
                                log.error("-100", "系统错误");
                            } else if ("0".equals(jos.get("code"))) {
                                log.error("0", "减免成功！");
                            } else if ("-101".equals(jos.get("code"))) {
                                log.error("-101", "参数错误 减免失败！！！");
                            }
                        }
                    }
                    //用户发送短信
                    /*SMSUtils sus=new SMSUtils();
                    String msg= auditCenter.getLoanUserName()+"，您的减免已生效，请您在今日23点之前还款"+bigDecimal+"元，逾期未还将不予减免。请您联系催收员进行代扣，或通过支付宝还款并备注您的姓名和电话";
                    System.out.println("msg "+msg);
                    if (auditCenter.getLoanUserPhone()!=null){
                        sus.sendSms(auditCenter.getLoanUserPhone(), msg);
                    }*/
                } else if ("3".equals(auditCenter.getType()) && "3".equals(auditCenter.getStatus())) {
                    auditMap.put("status", params.get("status"));
                    if ("3".equals(auditMap.get("status"))) {
                        auditMap.put("status", "8");
                    }
                    manLoanCollectionOrderDao.sveUpdateJmStatus(auditMap);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderId", auditCenter.getOrderid());
                    map.put("reductionMoney", 0);
                    manLoanCollectionOrderDao.updateReductionMoney(map);
                }
                successCount++;
            } else {
                result.setMsg("改信息不存在");
            }
        } else {
            result.setMsg("审核参数错误");
        }
        return successCount;
    }

    /**
     * 减免审核
     */
    @Override
    public JsonResult saveorderdeduction(Map<String, Object> params) {
        JsonResult result = new JsonResult("-1", "申请减免失败！");
        MmanLoanCollectionOrder collectionOrder = manLoanCollectionOrderDao.getOrderById(params.get("id").toString());
        String deductionMoney = (String) params.get("deductionmoney");
        int money = Integer.valueOf(deductionMoney);
        // 金额校验
        if (money != 0) {
            if (collectionOrder != null) {
                //更新订单状态
                collectionOrder.setStatus((String) params.get("note"));
                manLoanCollectionOrderDao.updateJmStatus(collectionOrder);
                AuditCenter auditCenter = new AuditCenter();
                //添加减免审核
                if (StringUtils.isNotBlank((String) params.get("id"))) {
                    auditCenter.setOrderid(collectionOrder.getId());
                    auditCenter.setLoanUserId(collectionOrder.getUserId());
                }
                auditCenter.setId(IdGen.uuid());
                auditCenter.setCreatetime(new Date());
                auditCenter.setNote((String) params.get("note"));
                auditCenter.setType((String) params.get("type"));
                auditCenter.setOperationUserId((String) params.get("operationUserId"));
                auditCenter.setRemark((String) params.get("deductionremark"));
                auditCenter.setReductionMoney(new Long(deductionMoney));
                auditCenter.setLoanPenalty(new BigDecimal(String.valueOf(params.get("loanPenlty"))));
                auditCenter.setLoanId((String) params.get("loanId"));
                auditCenter.setPayId((String) params.get("payId"));
                auditCenter.setLoanUserName((String) params.get("name"));
                auditCenter.setLoanUserPhone((String) params.get("phone"));
                auditCenter.setOrderStatus((String) params.get("orderStatus"));
                int cont = auditCenterDao.saveNotNull(auditCenter);
                if (cont > 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderId", auditCenter.getOrderid());
                    map.put("reductionMoney", auditCenter.getReductionMoney());
                    manLoanCollectionOrderDao.updateReductionMoney(map);
                    result.setMsg("申请成功！等待审核");
                    result.setCode("0");
                }
            }
        } else {
            result.setMsg("减免金额不能小于等于0");
        }
        return result;

    }

    @Override
    public PageConfig<AuditCenter> findAllPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "AuditCenter");
        PageConfig<AuditCenter> pageConfig;
        pageConfig = paginationDao.findPage("find", "findCount", params, null);
        return pageConfig;
    }

    @Override
    public void updateSysStatus(Map<String, String> params) {
        auditCenterDao.updateSysStatus(params);
    }
//   /**
//     * 减免推真实测试地址
//     */
//   public  static void   HttpPost(){
//        String user_id="438780";
//        String pay_id="63861";
//        int money= 35000;
//        String  uuid ="734aa7a95b39403ca0242adcf56e4d82";
//        String sign = Md5coding.md5(AesUtil.encrypt(user_id + pay_id + money + uuid, PayContents.XJX_WITHHOLDING_NOTIFY_KEY));
//        //2、发起请求
//        String withholdPostUrl=PayContents.XJX_JIANMIAN_URL+"/"+ user_id +"/"+ pay_id +"/"+ money +"/"+ uuid +"/"+sign;
//        log.error("减免请求地址："+withholdPostUrl);
//        String xjxWithholdingStr = HttpUtil.getHttpMess(withholdPostUrl, "", "POST", "UTF-8");
//        JSONObject jos = JSONObject.fromObject(xjxWithholdingStr);
//        System.out.println(jos);
//    }

}
