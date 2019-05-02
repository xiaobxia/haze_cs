package com.info.back.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.info.back.result.MonthDetailResult;
import com.info.back.result.MonthUseDetail;
import com.info.back.service.IMmanLoanCollectionOrderService;
import com.info.back.service.ISysDictService;
import com.info.risk.pojo.RiskOrders;
import com.info.web.copys.pojo.ShuJuMoHeVo;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/mmanUserInfo")
public class MmanUserInfoController extends BaseController {

    //订单
    @Resource
    private IMmanLoanCollectionOrderService mmanLoanCollectionOrderService;
    @Resource
    private ISysDictService sysDictService;

    public MmanUserInfoController() {
    }

    @RequestMapping(value = "/tdReport")
    public String getTdPage(HttpServletRequest request, Model model) {
        String url = "mycollectionorder/td_report";

        HashMap<String, Object> params = getParametersO(request);
        if (StringUtils.isNotBlank(params.get("id") + "")) {
            MmanLoanCollectionOrder mmanLoanCollectionOrderOri = mmanLoanCollectionOrderService.getOrderById(params.get("id").toString());
            int overdueDaysLimit = Integer.parseInt(sysDictService.findDictByType("see_tdData_limit_overduedays").get(0).getValue());
            if (mmanLoanCollectionOrderOri.getOverdueDays() < overdueDaysLimit) {
                request.setAttribute("message", "逾期天数未超过" + overdueDaysLimit + "天");
                model.addAttribute("navTab", "true");
                return "mycollectionorder/collectionOrder";
            }
//            Map<String, String> concatsRemarkMap = autoRiskService.getConcatsRemarkMap(mmanLoanCollectionOrderOri.getUserId());
//            Integer paramId = Integer.valueOf(mmanLoanCollectionOrderOri.getLoanId());
//            DataSourceContextHolder.setDbType("dataSourcexjx");
//            RiskOrders order = riskOrdersService.selectCreditReportByBorrowId(paramId);
//            DataSourceContextHolder.setDbType("dataSourcecs");
//            ShuJuMoHeVo shuJuMoHeVoOld = parseSjmhVo(order);
//            if (shuJuMoHeVoOld != null) {
//                appendCallName(concatsRemarkMap, shuJuMoHeVoOld.getNumberCallInfoMap());
//                Map<String, NumberCallInfo> callInfoMap = shuJuMoHeVoOld.getNumberCallInfoMap();
//                if (MapUtils.isNotEmpty(callInfoMap)) {
//                    Map<String, NumberCallInfo> finalMap = new LinkedHashMap<>(callInfoMap.size());
//                    callInfoMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue(Comparator.comparing(e -> e.getDuration())))).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
//                    shuJuMoHeVoOld.setNumberCallInfoMap(finalMap);
//                }
//                model.addAttribute("SJMHModel", shuJuMoHeVoOld);
//            } else {
//                com.info.risk.pojo.newrisk.ShuJuMoHeVo shuJuMoHeVoNew = autoRiskService.getShuJuMoHe(order);
//                appendCallName(concatsRemarkMap, shuJuMoHeVoNew.getNumberCallInfoMap());
//                Map<String, NumberCallInfo> callInfoMap = shuJuMoHeVoNew.getNumberCallInfoMap();
//                if (MapUtils.isNotEmpty(callInfoMap)) {
//                    Map<String, NumberCallInfo> finalMap = new LinkedHashMap<>(callInfoMap.size());
//                    callInfoMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue(Comparator.comparing(e -> e.getDuration())))).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
//                    shuJuMoHeVoNew.setNumberCallInfoMap(finalMap);
//                }
//                model.addAttribute("SJMHModel", shuJuMoHeVoNew);
//            }
        }
        return url;
    }

//    private void appendCallName(Map<String, String> concatsRemarkMap, Map<String, NumberCallInfo> numberCallInfoMap) {
//        if (MapUtils.isEmpty(concatsRemarkMap) || MapUtils.isEmpty(numberCallInfoMap)) {
//            return;
//        }
//        if (MapUtils.isNotEmpty(numberCallInfoMap)) {
//            numberCallInfoMap.forEach((k, callInfo) -> {
//                if (callInfo != null && StringUtils.isNotEmpty(callInfo.getCallNumber())) {
//                    callInfo.setCallName(concatsRemarkMap.get(callInfo.getCallNumber()) == null ? "" : concatsRemarkMap.get(callInfo.getCallNumber()));
//                }
//            });
//        }
//    }

//    private void handleJx(HttpServletRequest request, MmanLoanCollectionOrder mmanLoanCollectionOrderOri) {
//        MmanUserInfo userInfo = mmanUserInfoService.getUserInfoById(mmanLoanCollectionOrderOri.getUserId());
//        User user = dataDao.selectByUserId(Integer.parseInt(mmanLoanCollectionOrderOri.getUserId()));
//        Map<String, Object> result = mmanUserInfoService.getTdYysInfo(user.getTdRawData(), userInfo);
//        List<MonthUseDetail> monthUseDetails = (ArrayList) result.get("month");
//        String jsonStr = JSON.toJSONString(result);
//        List<MonthDetailResult> dataList = getResult(monthUseDetails);
//        /*top10*/
//        HashMap<String, Object> contactMap = new HashMap<>();
//        contactMap.put("userId", user.getId());
//        List<UserContacts> userContactsList = dataDao.selectUserContacts(contactMap);
//        //根据订单查询 risk_orders信息
//        HashMap<String, Object> mapParams = new HashMap<>();
//        mapParams.put("assetBorrowId", mmanLoanCollectionOrderOri.getLoanId());
//        RiskOrders riskOrders = dataDao.selectCreditReportByBorrowId(mapParams);
//        //兼容新老用户
//        ShuJuMoHeVo shuJuMoHeVo;
//        if (riskOrders != null && riskOrders.getAutoSjmh() != null) {
//            shuJuMoHeVo = parseSjmhVo(riskOrders);
//        } else {
//            Map<String, String> userContactMap = new HashMap<>();
//            if (userContactsList != null) {
//                for (UserContacts userContacts : userContactsList) {
//                    userContactMap.put(userContacts.getContactPhone(), userContacts.getContactName());
//                }
//            }
//            ParseSjmhUntil parseSjmhUntil = new ParseSjmhUntil();
//            String autoRiskTdRawData = user.getTdRawData();
//            String firstCall = user.getFirstContactPhone();
//            String secondCall = user.getSecondContactPhone();
//            shuJuMoHeVo = parseSjmhUntil.getShuJuMoHeVo(autoRiskTdRawData, userContactMap, firstCall, secondCall);
//        }
//
//        request.setAttribute("resultTdYunYingShang", jsonStr);
//        request.setAttribute("monthDetails", dataList);
//        request.setAttribute("tdTopTenList", shuJuMoHeVo.getTdTopTenList());
//    }

    private ShuJuMoHeVo parseSjmhVo(RiskOrders riskOrders) {
        ShuJuMoHeVo shuJuMoHeVo = null;
        if (riskOrders != null && riskOrders.getAutoSjmh() != null) {
            String jsonData = riskOrders.getAutoSjmh();
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(jsonData).getAsJsonObject();

            String sjmhData = root.has("shuJuMoHeVoJson") ? root.get("shuJuMoHeVoJson").getAsString() : null;
            Gson gson = new Gson();
            if (sjmhData != null) {
                shuJuMoHeVo = gson.fromJson(sjmhData, ShuJuMoHeVo.class);
            }
        }
        return shuJuMoHeVo;
    }

    private List<MonthDetailResult> getResult(List<MonthUseDetail> datalist) {
        List<MonthDetailResult> results = new ArrayList<>();
        String[] types = {"类型", "与第一紧急联系人最后一次通话时间", "与第二紧急联系人最后一次通话时间", "与第一紧急联系人通话次数", "与第二紧急联系人通话次数", "与第一联系人通话次数排名",
                "与第二联系人通话次数排名", "手机关机静默天数", "手机连续关机最大天数", "通话号码数量", "手机通话晚间活跃度", "每月话费", "通话总时长"};
        MonthDetailResult result;
        String month;
        String value;
        for (String type : types) {
            result = new MonthDetailResult();
            result.setType(type);
            for (MonthUseDetail data : datalist) {
                month = data.getMonth();
                value = getValue(data, type);
                switch (month) {
                    case "1":
                        result.setMonth1(value);
                        break;
                    case "2":
                        result.setMonth2(value);
                        break;
                    case "3":
                        result.setMonth3(value);
                        break;
                    case "4":
                        result.setMonth4(value);
                        break;
                    case "5":
                        result.setMonth5(value);
                        break;
                    case "6":
                        result.setMonth6(value);
                        break;
                    case "7":
                        result.setMonth7(value);
                        break;
                    case "8":
                        result.setMonth8(value);
                        break;
                    case "9":
                        result.setMonth9(value);
                        break;
                    case "10":
                        result.setMonth10(value);
                        break;
                    case "11":
                        result.setMonth11(value);
                        break;
                    case "12":
                        result.setMonth12(value);
                        break;
                    case "总计":
                        result.setAllCount(value);
                        break;
                    default:
                }
            }
            results.add(result);
        }
        return results;
    }

    private String getValue(MonthUseDetail data, String type) {
        switch (type) {
            case "类型":
                return data.getMonth();
            case "与第一紧急联系人最后一次通话时间":
                return data.getFirstConcatLastCallTime();
            case "与第二紧急联系人最后一次通话时间":
                return data.getSecondConcatLastCallTime();
            case "与第一紧急联系人通话次数":
                return data.getFirstConcatCallCount();
            case "与第二紧急联系人通话次数":
                return data.getSecondConcatCallCount();
            case "与第一联系人通话次数排名":
                return data.getFirstConcatCallRanking();
            case "与第二联系人通话次数排名":
                return data.getSecondConcatCallRanking();
            case "手机关机静默天数":
                return data.getTelCloseDays();
            case "手机连续关机最大天数":
                return data.getSeveralCloseMaxDays();
            case "通话号码数量":
                return data.getCallNumCount();
            case "手机通话晚间活跃度":
                return data.getCallNightActiveness();
            case "每月话费":
                return data.getTotalFee();
            case "通话总时长":
                return data.getTotalTime();
            default:
        }
        return null;
    }

}
