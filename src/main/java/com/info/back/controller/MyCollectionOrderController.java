package com.info.back.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.info.back.annotation.ExcludeUrl;
import com.info.back.annotation.OperaLogAnno;
import com.info.back.dao.ISmsUserDao;
import com.info.back.dao.ITemplateSmsDao;
import com.info.back.exception.BusinessException;
import com.info.back.result.JsonResult;
import com.info.back.service.*;
import com.info.back.utils.*;
import com.info.back.vo.LoanRecord;
import com.info.back.vo.RenewOrPayRecord;
import com.info.back.vo.TaobaoAddr;
import com.info.back.vo.TaobaoOrder;
import com.info.back.vo.jxl.Remark;
import com.info.config.PayContents;
import com.info.constant.Constant;
import com.info.web.copys.pojo.User;
import com.info.web.pojo.cspojo.*;
import com.info.web.synchronization.dao.IDataDao;
import com.info.web.synchronization.service.IDataService;
import com.info.web.util.DateUtil;
import com.info.web.util.JsonUtil;
import com.info.web.util.PageConfig;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 我的催收订单控制层
 *
 * @author Administrator
 */
@Slf4j
@Controller
@RequestMapping("collectionOrder/")
public class MyCollectionOrderController extends BaseController {

    @Resource
    private IMmanUserLoanService manUserLoanService;
    @Resource
    private IDataDao dataDao;
    @Resource
    private IMmanLoanCollectionCompanyService mmanLoanCollectionCompanyService;
    @Resource
    private IMmanLoanCollectionOrderService mmanLoanCollectionOrderService;
    @Resource
    private IMmanUserInfoService mmanUserInfoService;
    @Resource
    private IMmanLoanCollectionStatusChangeLogService mmanLoanCollectionStatusChangeLogService;
    @Resource
    private IMmanLoanCollectionRecordService mmanLoanCollectionRecordService;
    @Resource
    private IBackUserService backUserService;
    @Resource
    private ITemplateSmsDao templateSmsDao;
    @Resource
    private ICreditLoanPayDetailService creditLoanPayDetailService;
    @Resource
    private IMmanUserLoanService mmanUserLoanService;
    @Resource
    private IAuditCenterService auditCenterService;
    @Resource
    private ISysDictService sysDictService;
    @Resource
    private ICreditLoanPayService creditLoanPayService;
    @Resource
    private ISysUserBankCardService sysUserBankCardService;
    @Resource
    private ISmsUserService smsUserService;
    @Resource
    private IMmanUserRelaService mmanUserRelaService;
    @Resource
    private ISmsUserDao iSmsUserDao;
    @Resource
    private IMmanLoanCollectionOrderdeductionService collectionOrderdeductionService;

    @Resource
    private ICountCollectionAssessmentService countCollectionAssessmentService;

    @Resource
    private ICountCollectionManageService countCollectionManageService;
    @Resource
    private IFengKongService fengKongService;
    @Resource
    private ICreditLoanPayService loanPayService;
    @Resource
    private IDataDao iDataDao;
    @Resource
    private IMmanLoanCollectionOrderService manLoanCollectionOrderService;
    @Resource
    private IAlertMsgService sysAlertMsgService;
    @Resource
    private IRemarkService remarkService;
    @Resource
    private IBackDictionaryService backDictionaryService;
    @Resource
    private IBackRoleService backRoleService;
    @Resource
    private IDataService dataService;

    /**
     * 我的订单初始化加载查询
     */
    @RequestMapping("getListCollectionOrder")
    public String getListCollectionOrder(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        BackUser backUser = getSessionUser(request);
        List<BackUserCompanyPermissions> companyPermissions = backUserService
                .findCompanyPermissions(backUser.getId());
        if (companyPermissions != null && companyPermissions.size() > 0) {
            params.put("CompanyPermissionsList", companyPermissions);
        }
        BackRole topCsRole = backRoleService.getTopCsRole();
        List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
        // 如果是催收员只能看自己的订单
        if (backUser.getRoleId() != null && roleChildIds.contains(Integer.valueOf(backUser.getRoleId()))) {
            params.put("roleUserId", backUser.getUuid());
            // 催收员查属于自己的组
            params.put("collectionGroup", backUser.getGroupLevel());
            if (BackConstant.XJX_OVERDUE_LEVEL_S3.equals(backUser.getGroupLevel())) {
                /*s3可留单*/
                model.addAttribute("showSave", "true");
            }
        } else {
            // 若组没有 ，则默认查询S1 组
            String groupStr = params.get("collectionGroup") == null ? "" : String.valueOf(params.get("collectionGroup"));
            if (StringUtils.isEmpty(groupStr)) {
                params.put("collectionGroup", "3");
                model.addAttribute("showSave", "false");
            } else if (BackConstant.XJX_OVERDUE_LEVEL_S3.equals(groupStr)) {
                /*s3可留单*/
                model.addAttribute("showSave", "true");
            }

        }
        String groupStr = params.get("collectionGroup").toString();
        switch (groupStr) {
            case "4":
                model.addAttribute("term", "15");
                break;
            case "8":
                model.addAttribute("term", "30");
                break;
            default:
                model.addAttribute("term", "3");
                break;
        }

        // 查询公司列表
        MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
        List<MmanLoanCollectionCompany> collectionCompanies = mmanLoanCollectionCompanyService
                .getList(mmanLoanCollectionCompany);
        // 分页的订单信息
        String beforeStatus = params.get("status") == null ? null : params.get("status").toString();
        // 搜索框留单状态值
        String saveParam = "9";
        if (saveParam.equals(beforeStatus)) {
            params.remove("status");
            params.put("csStatus", BackConstant.SAVE);
        }
        PageConfig<OrderBaseResult> page;
        String sortBydateA = "update_date ASC";
        String sortBydateD = "update_date DESC";
        String sortStr = "sortBy";
        if (sortBydateA.equals(params.get(sortStr))) {
            params.put("hk_sort", sortBydateA);
        } else if (sortBydateD.equals(params.get(sortStr))) {
            params.put("hk_sort", sortBydateD);
        }
        params.putIfAbsent(sortStr, "overdueDays ASC");
        page = mmanLoanCollectionOrderService.getPage(params);
        params.put("status", beforeStatus);
        model.addAttribute("ListMmanLoanCollectionCompany",
                collectionCompanies);
        model.addAttribute("page", page);
        model.addAttribute("params", params);
        model.addAttribute("paramsJson", JSON.toJSON(params));
        model.addAttribute("userGropLeval", backUser.getRoleId());
        model.addAttribute("dictMap", BackConstant.GROUP_NAME_MAP);
        // 跟进等级
        List<SysDict> levellist = sysDictService.getStatus("xjx_stress_level");
        HashMap<String, String> levelMap = BackConstant.orderState(levellist);
        model.addAttribute("levellist", levellist);
        model.addAttribute("levelMap", levelMap);
        return "mycollectionorder/collectionOrder";
    }

    /**
     * 查看淘宝信息
     */
    @RequestMapping("toTaoBao")
    @SuppressWarnings("unchecked")
    public String toTaoBao(HttpServletRequest request, Model model) {
        String url = "mycollectionorder/taobaoData";
        List<TaobaoAddr> addrs = new ArrayList<>();
        List<TaobaoOrder> orders = new ArrayList<>();
        HashMap<String, Object> params = getParametersO(request);
        MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(params.get("id").toString());
        int overdueDaysLimit = Integer.parseInt(sysDictService.findDictByType("see_taobao_limit_overduedays").get(0).getValue());
        if (mmanLoanCollectionOrderOri.getOverdueDays() < overdueDaysLimit) {
            request.setAttribute("message", "逾期天数未超过" + overdueDaysLimit + "天");
            return "mycollectionorder/collectionOrder";
        }
        User user = dataDao.selectByUserId(Integer.parseInt(mmanLoanCollectionOrderOri.getUserId()));
        String zmToken = user.getZmToken();
        if (zmToken != null) {
            JSONObject zmObj;
            try {
                zmObj = JSONObject.parseObject(zmToken);
                String tokenName = "token";
                if (null != zmObj && zmObj.containsKey(tokenName)) {
                    zmToken = zmObj.getString("token");
                    Map<String, Object> resultMap = getGongXinBaoData(zmToken);
                    if (resultMap.size() > 0) {
                        addrs = (List<TaobaoAddr>) resultMap.get("addrs");
                        orders = (List<TaobaoOrder>) resultMap.get("orders");
                    }
                    model.addAttribute("addrs", addrs);
                    model.addAttribute("orders", orders);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return url;
    }

    /**
     * 获取公信宝json数据
     */
    private Map<String, Object> getGongXinBaoData(String zmToken) {
        Map<String, Object> resultMap = new HashMap<>();
        List<TaobaoOrder> orders = new ArrayList<>();
        List<TaobaoAddr> addrs = new ArrayList<>();
        final String appId = "gxbc5b8419fada3f5ff";
        final String appSecurity = "66fa775cbaeb48058341640683559ed7";
        long timestamp = System.currentTimeMillis();
        String sign = DigestUtils.md5Hex(String.format("%s%s%s", appId, appSecurity, timestamp));
        String url = "https://prod.gxb.io/crawler/data/query_by_token?token=" + zmToken +
                "&appId=" + appId + "&timestamp=" + timestamp + "&sign=" + sign;
        String data = GetRequest.getData(url);
        if (StringUtils.isNotEmpty(data)) {
            JSONObject dataObj = JSON.parseObject(data).getJSONObject("data");
            if (dataObj != null) {
                JSONObject authObj = dataObj.getJSONObject("authResult");
                if (authObj != null) {
                    com.alibaba.fastjson.JSONArray taobaoArr = authObj.getJSONArray("ecommerceConsigneeAddresses");
                    if (taobaoArr != null && !taobaoArr.isEmpty()) {
                        addrs = JSON.parseObject(taobaoArr.toJSONString(), new TypeReference<ArrayList<TaobaoAddr>>() {
                        });
                    }

                    com.alibaba.fastjson.JSONArray shopArr = authObj.getJSONArray("taobaoOrders");
                    if (shopArr != null && !shopArr.isEmpty()) {
                        JSONObject oneObj;
                        JSONObject sellerObj;
                        com.alibaba.fastjson.JSONArray subArr;
                        for (int i = 0; i < shopArr.size(); i++) {
                            oneObj = shopArr.getJSONObject(i);
                            if (oneObj == null) {
                                continue;
                            }
                            TaobaoOrder order = new TaobaoOrder();
                            order.setActual(oneObj.getString("actualFee"));
                            order.setEndTime(oneObj.getString("endTime"));
                            sellerObj = oneObj.getJSONObject("seller");
                            subArr = oneObj.getJSONArray("subOrders");
                            if (subArr != null && !subArr.isEmpty()) {
                                JSONObject subObj = subArr.getJSONObject(0);
                                order.setItemTitle(subObj == null ? "未知" : subObj.getString("itemTitle"));
                            }
                            order.setTotalQuantity(oneObj.getString("totalQuantity"));
                            order.setShopName(sellerObj == null ? "未知" : sellerObj.getString("shopName"));
                            order.setTradeStatusName(oneObj.getString("tradeStatusName"));
                            order.setVirtualSign(oneObj.getString("virtualSign"));
                            orders.add(order);
                        }
                    }
                }

            }

        }
        resultMap.put("addrs", addrs);
        resultMap.put("orders", orders);
        return resultMap;
    }


    /**
     * 运营系统查询催收记录
     */
    @ExcludeUrl
    @RequestMapping("ossOverdueRecord")
    public String seeRecordList(String orderId, String md5Code, Model model, HttpServletRequest request) {
        Map<String, String> params = this.getParameters(request);
        List<MmanLoanCollectionRecord> list = new ArrayList<>();
        String code = Md5coding.md5(orderId + Constant.SALT);
        if (!code.equals(md5Code)) {
            return null;
        }
        MmanLoanCollectionOrder param = new MmanLoanCollectionOrder();
        param.setLoanId(orderId);
        List<MmanLoanCollectionOrder> orders = mmanLoanCollectionOrderService.findList(param);
        MmanLoanCollectionOrder order;
        CreditLoanPay loanPay = null;
        HashMap<String, Object> auditParam = new HashMap(2);
        StringBuilder reductionSb = new StringBuilder();
        auditParam.put("status", 2);
        auditParam.put("loanId", orderId);
        List<AuditCenter> audits = auditCenterService.findAllPage(auditParam).getItems();
        if (CollectionUtils.isNotEmpty(audits)) {
            audits.forEach(one -> reductionSb.append(DateUtil.getDateFormat(one.getCreatetime(), "yyyy-MM-dd HH:mm:ss"))
                    .append("&nbsp;:&nbsp;").append(one.getReductionMoney()).append("<br/>"));
        }
        if (CollectionUtils.isNotEmpty(orders)) {
            order = orders.get(0);
            if (Constant.STATUS_OVERDUE_FOUR.equals(order.getStatus())) {
                loanPay = creditLoanPayService.findByLoanId(orderId);
            }
            list = mmanLoanCollectionRecordService.findListRecord(order.getId());
            if (list.size() > 0) {
                for (MmanLoanCollectionRecord one : list) {
                    one.setOverdueDays(order.getOverdueDays().toString());
                    one.setRepaymentDescript(loanPay == null ? "" : DateUtil.getDateFormat(loanPay.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
                    one.setReductionDescript(reductionSb.toString());
                }
            }
        }
        model.addAttribute("listRecord", list);
        // 跟进等级
        List<SysDict> levellist = sysDictService
                .getStatus("xjx_stress_level ");
        HashMap<String, String> levelMap = BackConstant
                .orderState(levellist);
        model.addAttribute("levelMap", levelMap);
        model.addAttribute("params", params);
        return "mycollectionorder/listRecord2";
    }

    /**
     * 添加催记录和催收建议页面
     */
    @RequestMapping("toCollectionRecordAndAdvice")
    public String toAddCollectionRecord(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String orderId;
        if ("other".equals(params.get("type"))) {
            log.info("CollectionRecordAndAdvice=" + params.get("id").toString());
            String ids = params.get("id").toString();
            String[] list = ids.split(",");
            String userRelaId = list[0];
            orderId = list[1];
            log.info("CollectionRecordAndAdvice-userRelaId=" + list[0]);
            log.info("CollectionRecordAndAdvice-orderId=" + list[1]);
            //根据userRelaId查询联系人
            MmanUserRela userRela = mmanUserRelaService.getUserRealByUserId(userRelaId);
            params.put("infoName", userRela.getInfoName());
            params.put("infoVlue", userRela.getInfoValue());
        } else {
            //根据orderId查询订单
            orderId = params.get("id").toString();
            MmanLoanCollectionOrder order = mmanLoanCollectionOrderService.getOrderById(orderId);
            params.put("infoName", order.getLoanUserName());
            params.put("infoVlue", order.getLoanUserPhone());
        }
        OrderBaseResult baseOrder = mmanLoanCollectionOrderService.getBaseOrderById(orderId);
        params.put("loanId", baseOrder.getLoanId());
        params.put("loanUserName", baseOrder.getRealName());
        params.put("loanMoney", baseOrder.getLoanMoney());
        params.put("loanPenlty", baseOrder.getLoanPenlty());

        params.put("id", orderId);
        List<SysDict> dictlist = sysDictService.getStatus("xjx_stress_level ");
        model.addAttribute("dictlist", dictlist);
        List<SysDict> statulist = sysDictService.getStatus("xjx_collection_advise");
        List<FengKong> fengKongList = fengKongService.getFengKongList();
        model.addAttribute("statulist", statulist);
        model.addAttribute("fengKongList", fengKongList);
        model.addAttribute("params", params);
        return "mycollectionorder/collectionRecordAndAdvice";
    }

    /**
     * 添加催收记录和催收建议
     */
    @ExcludeUrl
    @RequestMapping("addRecordAndAdvice")
    public String addcuishou(HttpServletRequest request,
                             HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult("-1", "添加催收记录和催收建议失败");
        Map<String, String> params = this.getParameters(request);
        BackUser backUser = this.loginAdminUser(request);
        try {
            result = mmanLoanCollectionRecordService.saveCollection(params,
                    backUser);
            if (result.isSuccessed()
                    && StringUtils.isNotBlank(params.get("stressLevel"))) {
                HashMap<String, Object> topMap = new HashMap<>();
                topMap.put("id", params.get("id") + "");
                topMap.put("topLevel", params.get("stressLevel"));
                mmanLoanCollectionOrderService.saveTopOrder(topMap);
            }
            params.put("backUserId", backUser.getId().toString());
            params.put("userName", backUser.getUserName());
            result = fengKongService.saveCollectionAdvice(params);
            model.addAttribute("params", params);
        } catch (BusinessException be) {
            result.setMsg(be.getMessage());
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()),
                result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT,
                "");
        return null;
    }

    /**
     * 催收流转日志
     */
    @RequestMapping("getlistlog")
    public String getCollectionStatusChangeLog(HttpServletRequest request, Model model) {
        Map<String, String> params = this.getParameters(request);
        List<MmanLoanCollectionStatusChangeLog> list = null;
        String idName = "id";
        if (StringUtils.isNotBlank(params.get(idName) + "")) {
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService
                    .getOrderById(params.get("id"));
            list = mmanLoanCollectionStatusChangeLogService
                    .findListLog(mmanLoanCollectionOrderOri.getOrderId());
            List<SysDict> statulist = sysDictService
                    .getStatus("xjx_collection_order_state");
            // 查询所有的催收状态
            HashMap<String, String> statuMap = BackConstant
                    .orderState(statulist);
            model.addAttribute("statuMap", statuMap);
            model.addAttribute("group", BackConstant.GROUP_NAME_MAP);
        }
        model.addAttribute("params", params);
        model.addAttribute("listlog", list);
        return "mycollectionorder/listlog";
    }

    /**
     * 跳转到订单详情页
     */

    @RequestMapping("toxianqin")
    public String toOrderDetail(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String idStr = "id";
        String nextStr = "next";
        String realOrderId = params.get(idStr) + "";
        if (StringUtils.isNotBlank(realOrderId)) {
            BackUser backUser = getSessionUser(request);

            List<BackUserCompanyPermissions> companyPermissions = backUserService.findCompanyPermissions(backUser.getId());
            // 指定公司的订单
            if (companyPermissions != null && companyPermissions.size() > 0) {
                params.put("CompanyPermissionsList", companyPermissions);
            }
            BackRole topCsRole = backRoleService.getTopCsRole();
            List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
            String groupStr = "collectionGroup";
            // 如果是催收员只能看自己的订单
            if (backUser.getRoleId() != null && roleChildIds.contains(Integer.valueOf(backUser.getRoleId()))) {
                params.put("roleUserId", backUser.getUuid());
                // 催收员查属于自己的组
                params.put(groupStr, backUser.getGroupLevel());
            } else {
                // 若组没有 ，则默认查询S1 组
                if (null == params.get(groupStr) || StringUtils.isBlank(String.valueOf(params.get(groupStr)))) {
                    params.put(groupStr, "3");
                }
            }
            PageConfig<OrderBaseResult> page = mmanLoanCollectionOrderService.getPage(params);
            if (params.get(nextStr) != null) {
                List<OrderBaseResult> orderBaseResults = page.getItems();
                for (int i = 0; i < orderBaseResults.size(); i++) {
                    if (orderBaseResults.get(i).getId().equals(params.get("id") + "") && i < orderBaseResults.size() - 1) {
                        realOrderId = orderBaseResults.get(i + 1).getId();
                    }
                }
            }
            model.addAttribute("this_id", realOrderId);


            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService
                    .getOrderById(realOrderId);
            MmanUserLoan userLoan = mmanUserLoanService
                    .get(mmanLoanCollectionOrderOri.getLoanId());
            model.addAttribute("userLoan", userLoan);
            model.addAttribute("collectionOrder", mmanLoanCollectionOrderOri);
            MmanUserInfo userInfo = mmanUserInfoService
                    .getUserInfoById(mmanLoanCollectionOrderOri.getUserId());
            model.addAttribute("userInfo", userInfo);
            if (userInfo != null) {
                List<Remark> remarks = remarkService.queryBorrowRemark(userInfo.getId());
                model.addAttribute("remarks", remarks);
                Map<String, Object> remarkMap1 = backDictionaryService.findDictionary(Constant.USER_REMARK);
                Map<String, Object> remarkMap2 = backDictionaryService.findDictionary(Constant.USER_REMARK_ONLINE);
                Map<String, Object> remarkMap = new HashMap<>();
                remarkMap.putAll(remarkMap1);
                remarkMap.putAll(remarkMap2);
                model.addAttribute("remarkMap", remarkMap);
            }

            List<CreditLoanPayDetail> detailList = creditLoanPayDetailService.findPayDetail(mmanLoanCollectionOrderOri.getPayId());
            BigDecimal payMonery = new BigDecimal(0);
            HashMap<String, String> userParam = new HashMap<>();
            HashMap<String, Object> userMap;
            userParam.put("USER_ID", userInfo.getId());
            userMap = iDataDao.getUserInfo(userParam);
            model.addAttribute("userMap", userMap);
            if (detailList != null) {
                for (CreditLoanPayDetail pay : detailList) {
                    payMonery = payMonery.add(pay.getRealMoney()).add(
                            pay.getRealPenlty());
                }
            }
            SysUserBankCard userCar = sysUserBankCardService
                    .findUserId(mmanLoanCollectionOrderOri.getUserId());
            // 已还金额
            model.addAttribute("userCar", userCar);
            // 已还金额
            model.addAttribute("payMonery", payMonery);
            model.addAttribute("detailList", detailList);
            // 代扣记录
            List<CollectionWithholdingRecord> withholdList = mmanLoanCollectionRecordService
                    .findWithholdRecord(mmanLoanCollectionOrderOri.getId());
            model.addAttribute("withholdList", withholdList);
            model.addAttribute("domaiName", PayContents.XJX_DOMAINNAME_URL);
            HashMap<String, Object> userLogin = dataDao.selectUserLastLoginLog(userInfo.getId());
            if (MapUtils.isNotEmpty(userLogin)) {
                model.addAttribute("lastLoginTime", userLogin.get("lastLoginTime") == null ? "未知" : userLogin.get("lastLoginTime"));
            }

            //查询借款记录
            List<LoanRecord> loanRecords = dataService.getUserLoanRecord(Integer.valueOf(mmanLoanCollectionOrderOri.getUserId()));
            if (CollectionUtils.isNotEmpty(loanRecords)) {
                model.addAttribute("loanRecords", loanRecords);
            }

        }
        model.addAttribute("params", params);
        model.addAttribute("paramsJson", JSON.toJSON(params));
        model.addAttribute("idVal", realOrderId);
        model.addAttribute("needSeparator", "");

        return "mycollectionorder/myorderDetails";
    }

    /**
     * 获取续还记录
     */
    @RequestMapping("getRenewOrPayRecord")
    public String getRenewOrPayRecord(HttpServletRequest request, Model model) {
        Map<String, String> params = this.getParameters(request);
        String assetId = params.get("assetOrderId");
        if (StringUtils.isNotEmpty(assetId)) {
            List<RenewOrPayRecord> records = dataService.getUserRenewOrPayRecord(Integer.valueOf(assetId));
            if (CollectionUtils.isNotEmpty(records)) {
                model.addAttribute("renewOrPayRecords", records);
            }
        }
        return "mycollectionorder/renewOrPayRecord";
    }

    /**
     * 催收记录表
     */
    @RequestMapping("collectionRecordList")
    public String getloanCollectionRecordList(HttpServletRequest request, Model model) {
        List<MmanLoanCollectionRecord> list;
        Map<String, String> params = this.getParameters(request);
        list = mmanLoanCollectionRecordService.findListRecord(params
                .get("id"));
        model.addAttribute("listRecord", list);
        // 跟进等级
        List<SysDict> levellist = sysDictService
                .getStatus("xjx_stress_level ");
        HashMap<String, String> levelMap = BackConstant
                .orderState(levellist);
        model.addAttribute("levelMap", levelMap);
        model.addAttribute("params", params);
        return "mycollectionorder/listRecord";
    }


    /**
     * 跳转扣款页面
     */

    @RequestMapping("tokokuan")
    @OperaLogAnno(operationType = "我的订单", operationName = "代扣")
    public String tokokuan(HttpServletRequest request, Model model) {
        Map<String, String> params = this.getParameters(request);
        String idStr = "id";
        if (StringUtils.isNotBlank(params.get(idStr))) {
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService
                    .getOrderById(params.get("id"));
            CreditLoanPay creditLoanPay = creditLoanPayService
                    .get(mmanLoanCollectionOrderOri.getPayId());
            model.addAttribute("totalPayMonery", creditLoanPay.getReceivablePrinciple().add(creditLoanPay.getReceivableInterest()));
        }
        model.addAttribute("params", params);
        return "mycollectionorder/tokokuan";
    }

    /**
     * 发起扣款
     */
    @ExcludeUrl
    @RequestMapping("kokuan")
    public void kokuan(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult("-1", "代扣失败");
        Map<String, String> params = this.getParameters(request);
        BackUser backUser = getSessionUser(request);
        try {
            params.put("roleId", backUser.getRoleId());
            params.put("operationUserId", backUser.getId().toString());
            result = mmanLoanCollectionRecordService.xjxWithholding(params);
        } catch (Exception e) {
            log.error("saveCompany error", e);
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()),
                result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT,
                params.get("parentId"));
    }

    /**
     * 跳转转派页面
     */
    @OperaLogAnno(operationType = "我的订单", operationName = "转派")
    @RequestMapping(value = "toOrderDistibute")
    public String toOrderDistibute(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        try {
            if (params.get("groupStatus") != null) {
                if (!"0".equals(params.get("groupStatus"))) {
                    throw new BusinessException("0", "只能相同组之间转派。请选择同组的数据");
                }
            } else {
                throw new BusinessException("0", "请选择要转派的订单");
            }
            MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
            mmanLoanCollectionCompany.setStatus(BackConstant.ON);
            List<MmanLoanCollectionCompany> companyList = mmanLoanCollectionCompanyService.getList(mmanLoanCollectionCompany);
            // 催收组
            List<SysDict> dict = sysDictService.findDictByType("xjx_overdue_level");
            // 启用的公司列表
            model.addAttribute("companyList", companyList);
            model.addAttribute("group", dict);
            model.addAttribute("params", params);
        } catch (BusinessException be) {
            model.addAttribute("message", be.getMessage());
        }
        model.addAttribute("params", params);
        return "mycollectionorder/orderDispatch";
    }


    @RequestMapping("getOrderIds")
    @ResponseBody
    public void getOrderIds(HttpServletRequest request, @RequestParam(value = "ids") String ids) {
        BackUser backUser = getSessionUser(request);
        String key = backUser == null ? "" : backUser.getUserAccount();
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(ids)) {
            jedisCluster.setex(key + Constant.ORDER_DISPATCH_IDS, 600, ids);
        }

    }

    /**
     * 转派-根据公司查询分组or催收员
     */
    @RequestMapping("findPerson")
    @ResponseBody
    public void orderDisBackUser(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> params = getParametersO(request);
        BackUser backUser = new BackUser();
        // 选择的催收公司
        backUser.setCompanyId(String.valueOf(params.get("companyId")));
        // 催收组
        backUser.setGroupLevel(String.valueOf(params.get("groupId")));
        // 剔除自己
        backUser.setNotMineId(String.valueOf(params.get("currUserId")));
        // 启用的催收员
        backUser.setUserStatus(BackUser.STATUS_USE);
        BackRole topCsRole = backRoleService.getTopCsRole();
        List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
        backUser.setRealIds(roleChildIds);
        List<BackUser> backUserList = backUserService
                .findUserByDispatch(backUser);
        if (CollectionUtils.isNotEmpty(backUserList)) {
            SpringUtils.renderJson(response, JSONArray.fromObject(backUserList).toString(), new String[0]);
        }
    }

    /**
     * 转派订单
     */
    @ExcludeUrl
    @RequestMapping(value = "doDispatch")
    public void doDispatch(MmanLoanCollectionOrder order, HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult("-1", "转派失败");
        HashMap<String, Object> params = this.getParametersO(request);
        String key = null;
        try {
            BackUser backUser = getSessionUser(request);
            key = backUser == null ? "" : backUser.getUserAccount();
            String ids = jedisCluster.get(key + Constant.ORDER_DISPATCH_IDS);
            if (StringUtils.isEmpty(ids)) {
                throw new BusinessException("0", "请选择要转派的订单");
            }
            order.setId(ids);
            Calendar calendar = Calendar.getInstance();
            int now = calendar.get(Calendar.HOUR_OF_DAY);
            if (now < 6) {
                throw new BusinessException("0", "0点到6点之间不允许进行转派操作");
            }
            if (params.get("groupStatus") != null && !"0".equals(params.get("groupStatus"))) {
                throw new BusinessException("0", "只能相同组之间转派。请选择同组的数据");
            } else {
                json = mmanLoanCollectionRecordService.batchDispatch(
                        backUser, order);
            }
        } catch (BusinessException be) {
            json.setMsg(be.getMessage());
        }
        if (StringUtils.isNotEmpty(key)) {
            jedisCluster.del(key);
        }
        SpringUtils.renderDwzResult(response, "0".equals(json.getCode()),
                json.getMsg(), DwzResult.CALLBACK_CLOSECURRENT,
                params.get("parentId").toString());
    }

    /**
     * 跳转到发送短信页面
     */
    @RequestMapping("gotoSendMsg")
    public String gotoSendMsg(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(params.get("id") + "")) {
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService
                    .getOrderById(params.get("id").toString());
            MmanUserInfo userInfo = mmanUserInfoService
                    .getUserInfoById(mmanLoanCollectionOrderOri.getUserId());
            model.addAttribute("loanOrderId",
                    mmanLoanCollectionOrderOri.getLoanId());
            model.addAttribute("userPhone", userInfo.getUserPhone());
            model.addAttribute("orderId",
                    mmanLoanCollectionOrderOri.getUserId());
            model.addAttribute("userId", userInfo.getId());
            model.addAttribute("originalNum", userInfo.getUserPhone());
            model.addAttribute("msgCount", iSmsUserDao
                    .getSendTotalMsgCount(mmanLoanCollectionOrderOri
                            .getLoanId()));
            String msgType = mmanLoanCollectionOrderOri.getCurrentOverdueLevel();
            switch (msgType) {
                case "5": {
                    List<String> list = new ArrayList<>();
                    list.add("3");
                    list.add("4");
                    list.add("5");
                    map.put("msgType", list);
                    break;
                }
                case "6":
                case "7": {
                    List<String> list = new ArrayList<>();
                    list.add("5");
                    map.put("msgType", list);
                    break;
                }
                case "3": {
                    List<String> list = new ArrayList<>();
                    list.add("3");
                    map.put("msgType", list);
                    break;
                }
                default: {
                    List<String> list = new ArrayList<>();
                    list.add("4");
                    map.put("msgType", list);
                    break;
                }
            }
            model.addAttribute("msgs", templateSmsDao.getType(map));
        }
        model.addAttribute("params", params);
        return "mycollectionorder/smsSendDetail";
    }

    /**
     * 发送催收短信
     * <p>
     * mobiles        需要发送短信的手机号
     * smsContent     短信内容
     * isSendSmsToAll 是否发送给所有人
     */
    @ExcludeUrl
    @RequestMapping("sendMsg")
    public Result sendSms(HttpServletRequest request,
                          HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult("-1", "发送短信失败");
        HashMap<String, Object> params = this.getParametersO(request);
        try {
            String mobiles = request.getParameter("phoneNumber");
            String msgId = request.getParameter("msgId");
            String smsContent;
            if (msgId == null || Objects.equals(msgId, "")) {
                result.setMsg("请选择需要发送的短信内容!");
            } else {
                smsContent = templateSmsDao.getTemplateSmsById(msgId)
                        .getContenttext();
                String orderId = request.getParameter("loanOrderId");
                String userName = mmanUserInfoService.getUserInfoById(
                        request.getParameter("orderId")).getRealname();
                String originalNum = request.getParameter("originalNum");
                String userId = request.getParameter("userId");
                smsContent = userName + smsContent;
                // 查询出该借款人所有的联系人
                List<MmanUserRela> userReal = mmanUserRelaService
                        .getContactPhones(userId);
                List<String> phones = new ArrayList<>();
                for (MmanUserRela mmanUserRela : userReal) {
                    phones.add(mmanUserRela.getInfoValue());
                }
                if (phones.contains(mobiles) || mobiles.equals(originalNum)) {
                    // 查询出对应借款订单信息
                    MmanLoanCollectionOrder order = mmanLoanCollectionOrderService
                            .getOrderWithId(orderId);
                    // 查询出该订单当天已发短信的次数
                    int count = smsUserService.getSendMsgCount(orderId);
                    int overdueDays = order.getOverdueDays();
                    // 逾期10天以内(包括10) 可发短信的数量为每天3条
                    int ten = 10;
                    int five = 5;
                    int coutLimit = 3;
                    if (overdueDays <= ten) {
                        if (count < coutLimit) {
                            sendMsg(result, mobiles, orderId, userName,
                                    smsContent, request);
                        } else {
                            result.setCode("3");
                            result.setMsg("今日该订单短信已达上限");
                        }
                    } else {
                        // 逾期10天以上(不包括10) 可发短信的数量为每天5条
                        if (count < five) {
                            sendMsg(result, mobiles, orderId, userName,
                                    smsContent, request);
                        } else {
                            result.setCode("3");
                            result.setMsg("今日该订单短信已达上限");
                        }
                    }
                } else {
                    result.setCode("5");
                    result.setMsg("请检查您输入的手机号码!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("params", params);
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()),
                result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT,
                params.get("parentId").toString());
        return null;
    }

    /**
     * 发送短信方法
     */
    private void sendMsg(JsonResult result, String mobiles, String orderId,
                         String userName, String smsContent, HttpServletRequest request)
            throws InterruptedException {
        if (StringUtils.isNotBlank(smsContent)
                && StringUtils.isNotBlank(mobiles)) {
            if (!"0".equals(new SMSUtils().sendSms(mobiles, smsContent))) {
                result.setCode("204");
                result.setMsg("发送失败");
            } else {
                result.setMsg("发送成功");
                result.setCode("0");
                // 新增一条短信发送记录
                SmsUser msg = new SmsUser();
                BackUser user = this.loginAdminUser(request);
                msg.setSendUserId(user.getUuid());
                msg.setAddTime(new Date());
                msg.setLoanOrderId(orderId);
                msg.setSmsContent(smsContent);
                msg.setUserPhone(mobiles);
                msg.setUserName(userName);
                this.smsUserService.insert(msg);
                // 每个数据包发送后等待一秒 云峰短信有频率限制
                Thread.sleep(1000);
            }
        } else {
            result.setMsg("参数不正确");
        }
    }

    /**
     * 分期还款计算
     */
    @RequestMapping("installmentPay")
    public String getInstallmentPay(HttpServletRequest request, Model model) {
        Map<String, String> params = this.getParameters(request);
        model.addAttribute("params", params);
        String installName = "installmentpay";
        if (!params.containsKey(installName) || StringUtils.isBlank(params.get(installName))) {
            params.put(installName, "2");
        }
        try {
            if (StringUtils.isNotBlank(params.get("id"))) {
                MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(params.get("id"));
                MmanUserLoan userLoan = mmanUserLoanService.get(mmanLoanCollectionOrderOri.getLoanId());
                if (userLoan != null) {
                    // 总本金
                    model.addAttribute("ownMoney", userLoan.getLoanMoney());
                    // 总滞纳金
                    model.addAttribute("overdueMoney", userLoan.getLoanPenalty());
                    int count = Integer.valueOf(params.get("installmentpay"));
                    // 获取分期明细信息
                    List<InstallmentPayInfoVo> list = getInstallmentPayInfoList(count, userLoan);

                    model.addAttribute("installmentPayInfoVoList", list);
                    model.addAttribute("installmentList", BackConstant.TYPE_MAP);
                }

                List<InstallmentPayRecord> installmentPayRecordList = mmanLoanCollectionRecordService.findInstallmentList(params.get("id"));
                model.addAttribute("installmentPayRecordList", installmentPayRecordList);
            }
        } catch (Exception e) {
            log.error("分期还款计算异常(installmentPay)：", e);
        }
        return "mycollectionorder/toInstallmentPay";
    }

    private List<InstallmentPayInfoVo> getInstallmentPayInfoList(int count, MmanUserLoan userLoan) {
        // 分期最多为4期
        int maxPeriods = 4;
        if (count > maxPeriods) {
            count = 2;
        }
        List<InstallmentPayInfoVo> list = new ArrayList<>();
        InstallmentPayInfoVo installmentPayInfoVo;

        for (int i = 0; i < count; i++) {
            installmentPayInfoVo = new InstallmentPayInfoVo();
            installmentPayInfoVo.setInstallmentType(BackConstant.TYPE_NAME_MAP.get(String.valueOf(i + 1)));
            installmentPayInfoVo.setRepayTime(getNextWeek(i));
            BigDecimal stagesOwnMoney = userLoan.getLoanMoney().divide(new BigDecimal(count), 2, BigDecimal.ROUND_UP);
            // 分期本金
            installmentPayInfoVo.setStagesOwnMoney(stagesOwnMoney);
            BigDecimal stagesOverdueMoney = userLoan.getLoanPenalty().divide(new BigDecimal(count), 2, BigDecimal.ROUND_UP);
            // 分期滞纳金
            installmentPayInfoVo.setStagesOverdueMoney(stagesOverdueMoney);
            // 分期还款总计
            installmentPayInfoVo.setTotalRepay(stagesOwnMoney.add(stagesOverdueMoney));
            if (i == 0) {
                BigDecimal serviceCharge = new BigDecimal(10).multiply(new BigDecimal(count));
                installmentPayInfoVo.setServiceCharge(serviceCharge);
                installmentPayInfoVo.setTotalRepay(stagesOwnMoney.add(stagesOverdueMoney).add(serviceCharge)); // 分期还款总计
            }
            list.add(installmentPayInfoVo);
        }
        return list;
    }

    private Date getNextWeek(int i) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, i);
        return calendar.getTime();
    }

    /**
     * 分期账单创建
     */
    @RequestMapping("insertInstallmentPayRecord")
    public void insertInstallmentPayRecord(HttpServletRequest request, HttpServletResponse response) {
        List<InstallmentPayRecord> installmentPayRecordList;
        Map<String, Object> map = new HashMap<>();
        JsonResult result = new JsonResult("-1", "创建分期失败");
        Map<String, String> params = this.getParameters(request);
        BackUser backUser = getSessionUser(request);

        try {
            List<InstallmentPayRecord> countList = mmanLoanCollectionRecordService.findInstallmentList(params.get("id"));
            if (CollectionUtils.isEmpty(countList)) {
                MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(params.get("id"));
                MmanUserLoan userLoan = mmanUserLoanService.get(mmanLoanCollectionOrderOri.getLoanId());
                if (userLoan != null) {
                    map.put("loanUserName", mmanLoanCollectionOrderOri.getLoanUserName());
                    map.put("loanUserPhone", mmanLoanCollectionOrderOri.getLoanUserPhone());
                    int count = Integer.valueOf(params.get("installmentpay"));
                    // 获取分期明细信息
                    List<InstallmentPayInfoVo> list = getInstallmentPayInfoList(count, userLoan);
                    params.put("roleId", backUser.getRoleId());
                    params.put("operationUserId", backUser.getId().toString());
                    JsonResult withholdingResult = mmanLoanCollectionRecordService.xjxWithholding(params);
                    withholdingResult.setCode("0");
                    // 代扣成功
                    if ("0".equals(withholdingResult.getCode())) {
                        result = mmanLoanCollectionRecordService.insertInstallmentPayRecord(list, mmanLoanCollectionOrderOri);
                        installmentPayRecordList = (List<InstallmentPayRecord>) result.getData();
                        map.put("installmentPayRecordList", installmentPayRecordList);
                    }
                }
            } else {
                result.setMsg("该订单已创建过了");
            }
            String json = JsonUtil.beanToJson(map);
            response.getWriter().write(json);
            response.getWriter().flush();

        } catch (Exception e) {
            log.error("分期账单创建失败！", e);
        }
    }

    @RequestMapping("getContactInfo")
    public String gotoGetContactInfoPage(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String phoneNum = request.getParameter("phoneNum");
        if (phoneNum != null && !"".equals(phoneNum)) {
            model.addAttribute("contactInfo",
                    mmanUserInfoService.getContactInfo(phoneNum));
        }
        model.addAttribute("params", params);
        return "mycollectionorder/getContactInfo";
    }

    /**
     * 减免页面
     */
    @OperaLogAnno(operationType = "我的订单", operationName = "减免")
    @RequestMapping("jianmian")
    public String jianmian(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        if (StringUtils.isNotBlank(params.get("id") + "")) {
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService
                    .getOrderById(params.get("id").toString());
            MmanUserLoan userLoan = mmanUserLoanService
                    .get(mmanLoanCollectionOrderOri.getLoanId());
            model.addAttribute("userLoan", userLoan);
            model.addAttribute("collectionOrder", mmanLoanCollectionOrderOri);
            MmanUserInfo userInfo = mmanUserInfoService
                    .getUserInfoById(mmanLoanCollectionOrderOri.getUserId());
            model.addAttribute("userInfo", userInfo);
            List<CreditLoanPayDetail> detailList = creditLoanPayDetailService
                    .findPayDetail(mmanLoanCollectionOrderOri.getPayId());
            BigDecimal payMonery = new BigDecimal(0);
            if (detailList != null) {
                for (CreditLoanPayDetail pay : detailList) {
                    payMonery = payMonery.add(pay.getRealMoney()).add(
                            pay.getRealPenlty());
                }
            }

            SysUserBankCard userCar = sysUserBankCardService
                    .findUserId(mmanLoanCollectionOrderOri.getUserId());
            // 已还金额
            model.addAttribute("userCar", userCar);
            // 本金
            model.addAttribute("payMonery", payMonery);
            model.addAttribute("detailList", detailList);
            //减免类型
            List<SysDict> statulist = sysDictService.getStatus("xjx_collection_derate");
            model.addAttribute("statulist", statulist);
        }
        model.addAttribute("params", params);
        return "mycollectionorder/jmlist";
    }

    /**
     * 减免确认
     */
    @ExcludeUrl
    @RequestMapping("addloanCollPaymoney")
    public void addloanCollPaymoney(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult("-1", "申请减免失败！！");
        HashMap<String, Object> params = this.getParametersO(request);
        BackUser backUser = getSessionUser(request);

        try {
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService
                    .getOrderById(params.get("id").toString());
            MmanUserLoan userLoan = mmanUserLoanService
                    .get(mmanLoanCollectionOrderOri.getLoanId());
            BigDecimal remoney = new BigDecimal(params.get("deductionmoney").toString());
            CreditLoanPay loanPay = loanPayService.get(mmanLoanCollectionOrderOri.getPayId());
            BigDecimal receivableInterest = loanPay.getReceivableInterest();
            if (remoney.compareTo(userLoan.getLoanPenalty()) <= 0) {
                if ((receivableInterest.compareTo(remoney) >= 0)) {
                    if (!"6".equalsIgnoreCase(mmanLoanCollectionOrderOri.getStatus())) {
                        if (!"4".equals(mmanLoanCollectionOrderOri.getStatus())) {
                            if (!"7".equals(mmanLoanCollectionOrderOri.getStatus())) {
                                //减免类型
                                List<SysDict> statulist = sysDictService
                                        .getStatus("xjx_collection_derate");
                                //减免审核状态
                                List<SysDict> jmstatulist = sysDictService
                                        .getStatus("xjx_collection_order_state");
                                model.addAttribute("statulist", statulist);
                                params.put("orderId", mmanLoanCollectionOrderOri.getId());
                                params.put("userId", mmanLoanCollectionOrderOri.getUserId());
                                params.put("deductionmoney", params.get("deductionmoney"));
                                params.put("deductionremark", params.get("deductionremark"));
                                params.put("loanPenlty", userLoan.getLoanPenalty());
                                params.put("loanId", mmanLoanCollectionOrderOri.getLoanId());
                                params.put("operationUserId", String.valueOf(backUser.getId()));
                                params.put("type", statulist.get(0).getValue());
                                params.put("note", jmstatulist.get(2).getValue());
                                params.put("payId", mmanLoanCollectionOrderOri.getPayId());
                                params.put("name", mmanLoanCollectionOrderOri.getLoanUserName());
                                params.put("phone", mmanLoanCollectionOrderOri.getLoanUserPhone());
                                params.put("orderStatus", mmanLoanCollectionOrderOri.getStatus());
                                result = auditCenterService.saveorderdeduction(params);
                            } else {
                                result.setMsg("减免审核成功订单，不能再次申请减免");
                            }
                        } else {
                            result.setMsg("催收成功订单，不能申请减免");
                        }
                    } else {
                        result.setMsg("申请减免中的订单， 不能重复申请减免");
                    }
                } else {
                    result.setMsg("减免失败 减免金额不能大于剩余应还金额");
                }
            } else {
                result.setMsg("减免金额 不能大于滞纳金");
            }
        } catch (Exception e) {
            result.setMsg("申请减免异常");
            log.error("申请减免异常 addloanCollPaymoney  ", e);
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()),
                result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT,
                params.get("parentId").toString());
    }


    @RequestMapping("getorderPage")
    public String getorderPage(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            //add  减免列表默认申请中状态
            if (!params.containsKey("status") && params.containsKey("--")) {
                params.put("status", "0");
                params.put("pageNum", 1);
            }
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            PageConfig<MmanLoanCollectionOrderdeduction> pageConfig = collectionOrderdeductionService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("getCompanyPage error", e);
        }
        return "mycollectionorder/getorderPage";
    }

    @RequestMapping("testKh")
    public String testKh(HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<>(4);
            params.put("currDate", request.getParameter("currDate"));
            params.put("begDate", request.getParameter("begDate"));
            params.put("endDate", request.getParameter("endDate"));
            //获取当前日
            params.put("isFirstDay", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            // 考核统计
            countCollectionAssessmentService.countCallAssessment(params);
        } catch (Exception e) {
            log.error("testKh error", e);
        }
        return null;
    }

    @RequestMapping("testGl")
    public String testGl(HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("currDate", request.getParameter("currDate"));
            params.put("begDate", DateUtil.getDayFirst());
            params.put("endDate", DateUtil.getDayLast());
            //获取当前日
            params.put("isFirstDay", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            // 管理统计
            countCollectionManageService.countCallManage(params);
        } catch (Exception e) {
            log.error("testGl error", e);
        }
        return null;
    }

    @RequestMapping("handDispatchOrder")
    public void handDispatchOrder() {
        try {
            log.info("handDispatchOrder start!");
            MmanUserLoan mmanUserLoan = new MmanUserLoan();
            mmanUserLoan.setLoanStatus(BackConstant.CREDITLOANAPPLY_OVERDUE);
            List<MmanUserLoan> overdueList = manUserLoanService.findMmanUserLoanList2(mmanUserLoan);
            log.info("overdueList size:" + overdueList.size());
            overdueList.forEach(this::dispatchOrder);
        } catch (Exception e) {
            log.error("handDispatchOrder error", e);
        }
    }

    private void dispatchOrder(MmanUserLoan mmanUserLoanOri) {
        Date now = new Date();
        String sysName = "系统";
        String sysPromoteRemark = "逾期30天以上且未留单外派到M1-M2";
        int pday = 0;
        try {
            pday = DateUtil.daysBetween(mmanUserLoanOri.getLoanEndTime(), new Date());
        } catch (ParseException e) {
            log.error("dispatchOrder parse pday failed", e);
        }
        MmanLoanCollectionOrder mmanLoanCollectionOrder = new MmanLoanCollectionOrder();
        mmanLoanCollectionOrder.setLoanId(mmanUserLoanOri.getId());
        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderList = manLoanCollectionOrderService.findList(mmanLoanCollectionOrder);
        MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderList.get(0);
        List<MmanLoanCollectionOrder> mmanLoanCollectionOrderNo132List = new ArrayList<>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo132List;
        if (!BackConstant.SAVE.equals(mmanLoanCollectionOrderOri.getCsstatus()) && BackConstant.XJX_OVERDUE_LEVEL_S3.equals(mmanLoanCollectionOrderOri.getCurrentOverdueLevel()) && pday > 30
                && !BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(mmanLoanCollectionOrderOri.getStatus())
                && !BackConstant.XJX_COLLECTION_ORDER_STATE_PAYING.equals(mmanLoanCollectionOrderOri.getStatus())) {
            mmanLoanCollectionOrderOri.setDispatchName(sysName);
            mmanLoanCollectionOrderOri.setDispatchTime(now);
            mmanLoanCollectionOrderOri.setOperatorName(sysName);
            mmanLoanCollectionOrderOri.setRemark(sysPromoteRemark);
            //上一催收员
            mmanLoanCollectionOrderOri.setLastCollectionUserId(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());

            mmanLoanCollectionOrderNo132List.add(mmanLoanCollectionOrderOri);


            HashMap<String, Object> personMap = new HashMap<>(4);
            personMap.put("dispatchTime", DateUtil.getDateFormat("yyyy-MM-dd"));
            personMap.put("groupLevel", BackConstant.XJX_OVERDUE_LEVEL_M1_M2);
            personMap.put("userStatus", BackConstant.ON);
            mmanLoanCollectionPersonNo132List = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap);

            if (null == mmanLoanCollectionPersonNo132List || mmanLoanCollectionPersonNo132List.isEmpty()) {
                SysAlertMsg alertMsg = new SysAlertMsg();
                alertMsg.setTitle("分配催收任务失败");
                alertMsg.setContent("所有公司M1_M2组查无可用催收人,请及时添加或启用该组催收员。");
                alertMsg.setDealStatus(BackConstant.OFF);
                alertMsg.setStatus(BackConstant.OFF);
                alertMsg.setType(SysAlertMsg.TYPE_COMMON);
                sysAlertMsgService.insert(alertMsg);
                log.error("M1_M2 no effiective person!");
            }
            mmanLoanCollectionRecordService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo132List, mmanLoanCollectionPersonNo132List, now);
        }
    }

    @RequestMapping("updateSaveStatus")
    @ResponseBody
    public void updateSaveStatus(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult("-1");
        HashMap<String, Object> params = this.getParametersO(request);
        try {
            String orderId = request.getParameter("id");
            MmanLoanCollectionOrder order = mmanLoanCollectionOrderService.getOrderById(orderId);
            HashMap<String, Object> findParams = new HashMap<>(3);
            findParams.put("roleUserId", order.getCurrentCollectionUserId());
            findParams.put("status", BackConstant.XJX_COLLECTION_ORDER_STATE_ING);
            findParams.put("csStatus", BackConstant.SAVE);
            int savedOrders = mmanLoanCollectionOrderService.findAllCount(findParams);
            int saveLimit = Integer.parseInt(sysDictService.findDictByType("save_order_limit").get(0).getValue());
            String currStatus = params.get("status") == null ? "" : params.get("status").toString();
            if (savedOrders == saveLimit && BackConstant.NOT_SAVE.equals(currStatus)) {
                throw new BusinessException("-1", "留单数已达上限!");
            }
            if (BackConstant.XJX_OVERDUE_LEVEL_S3.equals(order.getCurrentOverdueLevel())) {
                order.setCsstatus(currStatus.equals(BackConstant.NOT_SAVE) ? BackConstant.SAVE : BackConstant.NOT_SAVE);
                mmanLoanCollectionOrderService.updateRecord(order);
            }
            result.setCode("0");
            result.setMsg("操作成功!");
        } catch (BusinessException be) {
            result.setMsg(be.getMessage());
        } catch (Exception e) {
            log.error("updateSaveStatus error", e);
            result.setMsg("系统异常请稍后重试");
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                DwzResult.CALLBACK_CLOSECURRENT, "");
    }

}