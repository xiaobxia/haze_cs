package com.info.back.controller;

import com.info.back.dao.IMmanLoanCollectionStatusChangeLogDao;
import com.info.back.dao.IOrderLifeCycleDao;
import com.info.back.service.ICollectionService;
import com.info.back.service.ICollectionStatisticsService;
import com.info.back.service.IMmanUserInfoService;
import com.info.back.service.TaskJobMiddleService;
import com.info.back.utils.BackConstant;
import com.info.back.utils.ExcelUtil;
import com.info.back.vo.*;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanLoanCollectionStatusChangeLog;
import com.info.web.synchronization.dao.IDataDao;
import com.info.web.synchronization.service.IDataService;
import com.info.web.synchronization.service.ILocalDataService;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Created by cqry_2016 on 2018/4/10
 * 绩效考核
 */
@Slf4j
@Controller
@RequestMapping("performance/")
public class PerformanceAppraisalController extends BaseController {


    @Resource
    private TaskJobMiddleService taskJobMiddleService;

    @Resource
    private ICollectionService collectionService;

    @Resource
    private IMmanUserInfoService mmanUserInfoService;

    @Resource
    private IDataService dataService;

    @Resource
    private ILocalDataService localDataService;

    @Resource
    private IMmanLoanCollectionStatusChangeLogDao mmanLoanCollectionStatusChangeLogDao;

    @Resource
    private IDataDao dataDao;

    @Resource
    private IOrderLifeCycleDao orderLifeCycleDao;

    @Resource
    private ICollectionStatisticsService collectionStatisticsService;

    @RequestMapping("toPerformancePage")
    public String toS1(HttpServletRequest request, Model model) {
        String url = "statistics/performanceCount_record_s1";
        HashMap<String, Object> params = getParametersO(request);
        switch (params.get("groupLevel").toString()) {
            case BackConstant.XJX_OVERDUE_LEVEL_S2:
                url = "statistics/performanceCount_record_s2";
                break;
            case BackConstant.XJX_OVERDUE_LEVEL_S3:
                url = "statistics/performanceCount_record_s3";
                break;
            case BackConstant.XJX_OVERDUE_LEVEL_M1_M2:
                url = "statistics/performanceCount_record_m1_m2";
                break;
            default:
        }
        boolean isbegDateNotEmpty = params.get("begDate") != null && StringUtils.isNotEmpty(params.get("begDate").toString());
        boolean isendDateNotEmpty = params.get("endDate") != null && StringUtils.isNotEmpty(params.get("endDate").toString());
        boolean isTelNotEmpty = params.get("tel") != null && StringUtils.isNotEmpty(params.get("tel").toString());
        if (!isbegDateNotEmpty && !isendDateNotEmpty) {
            params.put("begDate", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
        }
        PageConfig<PerformanceCountRecord> pm = collectionService.findPerformancePage(params);
        model.addAttribute("pm", pm);
        model.addAttribute("params", params);
        if (!isTelNotEmpty || isbegDateNotEmpty || isendDateNotEmpty) {
            PerformanceTotalResult totalResult = collectionService.getPerformanceTotalResult(params);
            model.addAttribute("total", totalResult);
        }
        return url;
    }

    /**
     * 统计数据导出
     */
    @RequestMapping("/performanceExcelReport")
    public void performanceExcelReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            boolean isbegDateNotEmpty = params.get("begDate") != null && StringUtils.isNotEmpty(params.get("begDate").toString());
            boolean isendDateNotEmpty = params.get("endDate") != null && StringUtils.isNotEmpty(params.get("endDate").toString());
            if (!isbegDateNotEmpty && !isendDateNotEmpty) {
                params.put("begDate", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
            }
            int size = 50000;
            int total = 0;
            params.put(Constant.PAGE_SIZE, size);
            int totalPageNum = collectionService.findAllCount(params);
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            OutputStream os = response.getOutputStream();
            response.reset();// 清空输出流
            ExcelUtil.setFileDownloadHeader(request, response, "绩效统计.xlsx");
            response.setContentType("application/msexcel");
            SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
            String[] titles = {"序号", "姓名", "手机号", "系统进件", "人工转派", "成功出件", "追回本金", "续期次数",
                    "违约金", "日期", "历史累计进件", "历史累计成功件", "历史成功率"};
            for (int i = 1; i <= total; i++) {
                params.put(Constant.CURRENT_PAGE, i);
                PageConfig<PerformanceCountRecord> pm = collectionService.findPerformancePage(params);
                List<PerformanceCountRecord> list = pm.getItems();
                List<Object[]> contents = new ArrayList<>();
                int index = 1;
                for (PerformanceCountRecord r : list) {
                    String[] conList = new String[titles.length];
                    conList[0] = index + "";
                    conList[1] = r.getUserName();
                    conList[2] = r.getTel();
                    conList[3] = r.getSysOrder().toString();
                    conList[4] = r.getHandOrder().toString();
                    conList[5] = r.getOutOrder().toString();
                    conList[6] = r.getReturnPrincipal().toString();
                    conList[7] = r.getRenewalCount().toString();
                    conList[8] = r.getFee().toString();
                    conList[9] = DateUtil.getDateFormat(r.getCountDate(), "yyyy-MM-dd");
                    conList[10] = r.getTotalIntoOrder().toString();
                    conList[11] = r.getTotalOutOrder().toString();
                    conList[12] = r.getSucRate().toString() + "%";
                    contents.add(conList);
                    index++;
                }
                ExcelUtil.buildExcel(workbook, "绩效考核统计", titles, contents, i, total, os);
            }
        } catch (Exception e) {
            log.error("performanceExcelReport error", e);
        }
    }

    @RequestMapping("testJob")
    public void testJob() {
        taskJobMiddleService.countPerformanceJob();
    }

    @RequestMapping("handDispatch")
    public void handDispatch() {
        taskJobMiddleService.autoDispatch();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @RequestMapping("repairUserFrom")
    public void repairUserFrom() {
        HashMap<String, Object> params = new HashMap<>(3);
        HashMap<Integer, String> datas = new HashMap<>(100);
        List<Integer> ids;
        List<HashMap<Integer, Object>> userFromData;
        try {
            params.put("countBlank", "Y");
            int count = mmanUserInfoService.getUserCount(params);
            int pageSize = 100;
            params.put("pageSize", pageSize);
            int pageNum = count % 100 == 0 ? count / 100 : count / 100 + 1;
            int offSet;
            for (int i = 0; i < pageNum + 1; i++) {
                offSet = i * pageSize;
                params.put("offset", offSet);
                ids = mmanUserInfoService.getUserIdList(params);
                userFromData = dataService.getUserFromData(ids);
                for (HashMap<Integer, Object> one : userFromData) {
                    if (NumberUtils.isNumber(one.get("user_from").toString())) {
                        datas.put((int) one.get("id"), one.get("user_from").toString());
                    }
                }
                mmanUserInfoService.addUserFrom(datas);
                datas.clear();
            }
        } catch (Exception e) {
            log.error("repairUserFrom error", e);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @RequestMapping("addRepayRecord")
    public void addRepayRecord() {
        HashMap<String, Object> params = new HashMap<>(3);
        HashMap<String, Integer> data = new HashMap<>(100);
        List<Integer> loanIds;
        List<HashMap<Integer, Long>> orderRenewalData;
        try {
            int count = localDataService.getOrderCount(params);
            int pageSize = 100;
            params.put("pageSize", pageSize);
            int pageNum = count % 100 == 0 ? count / 100 : count / 100 + 1;
            int offSet;
            for (int i = 0; i < pageNum + 1; i++) {
                offSet = i * pageSize;
                params.put("offset", offSet);
                loanIds = localDataService.getLoanIds(params);
                orderRenewalData = dataService.getOrderRenewalCount(loanIds);
                for (HashMap<Integer, Long> one : orderRenewalData) {
                    if (one.get("loanId") != null && StringUtils.isNotEmpty(one.get("loanId").toString())) {
                        data.put(one.get("loanId").toString(), one.get("num").intValue());
                    }
                }
                if (data.size() > 0) {
                    localDataService.addOrderRenewCount(data);
                    data.clear();
                }
            }
        } catch (Exception e) {
            log.error("addRepayRecord error", e);
        }
    }

    @RequestMapping("repayRenewRecordPage")
    public String repayRenewRecordPage(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        boolean isStartEmpty = params.get("startDate") == null || StringUtils.isEmpty(params.get("startDate").toString());
        boolean isEndEmpty = params.get("endDate") == null || StringUtils.isEmpty(params.get("endDate").toString());
        if (isStartEmpty && isEndEmpty) {
            params.put("startDate", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
        }
        PageConfig<CollectionSucRecord> pm = collectionService.findRepayRenewRecordPage(params);
        model.addAttribute("pm", pm);
        model.addAttribute("params", params);
        return "statistics/repay_renewal_record";
    }

    /**
     * 统计数据导出
     */
    @RequestMapping("/repayRenewRecordExcelReport")
    public void repayRenewRecordExcelReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            boolean isStartEmpty = params.get("startDate") == null || StringUtils.isEmpty(params.get("startDate").toString());
            boolean isEndEmpty = params.get("endDate") == null || StringUtils.isEmpty(params.get("endDate").toString());
            if (isStartEmpty && isEndEmpty) {
                params.put("startDate", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
            }
            int size = 50000;
            int total = 0;
            params.put(Constant.PAGE_SIZE, size);
            int totalPageNum = collectionService.findAllCount(params);
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            OutputStream os = response.getOutputStream();
            response.reset();// 清空输出流
            ExcelUtil.setFileDownloadHeader(request, response, "续还记录.xlsx");
            response.setContentType("application/msexcel");// 定义输出类型
            SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
            String[] titles = {"序号", "催回日期", "借款编号", "借款人姓名", "借款人手机号", "催收状态", "借款金额", "滞纳金",
                    "减免滞纳金", "逾期天数", "催收员", "派单人"};
            for (int i = 1; i <= total; i++) {
                params.put(Constant.CURRENT_PAGE, i);
                PageConfig<CollectionSucRecord> pm = collectionService.findRepayRenewRecordPage(params);
                List<CollectionSucRecord> list = pm.getItems();
                List<Object[]> contents = new ArrayList<>();
                int index = 1;
                for (CollectionSucRecord r : list) {
                    String[] conList = new String[titles.length];
                    conList[0] = index + "";
                    conList[1] = DateUtil.getDateFormat(r.getRechargeDate(), "yyyy-MM-dd");
                    conList[2] = r.getLoanId().toString();
                    conList[3] = r.getLoanName();
                    conList[4] = r.getLoanTel();
                    conList[5] = r.getRechargeStatus().toString();
                    conList[6] = r.getLoanMoney().toString();
                    conList[7] = r.getLateFee().toString();
                    conList[8] = r.getReducMoney().toString();
                    conList[9] = r.getLateDay().toString();
                    conList[10] = r.getCollectName();
                    conList[11] = r.getDispatchName();
                    contents.add(conList);
                    index++;
                }
                ExcelUtil.buildExcel(workbook, "续还记录", titles, contents, i, total, os);
            }
        } catch (Exception e) {
            log.error("repayRenewRecordExcelReport error", e);
        }
    }

    @RequestMapping("testOrderCycle")
    public void addBeforeOrderCycle() {
        HashMap<String, Object> params = new HashMap<>(2);
        List<String> ids;
        Map<String, List<MmanLoanCollectionStatusChangeLog>> groupData;
        List<MmanLoanCollectionStatusChangeLog> logData;
        try {
            /*查出派单时间2018-01-01 ~ 08-01前订单流转日志*/
            Integer count = mmanLoanCollectionStatusChangeLogDao.getOrderIdNum();
            int pageSize = 100;
            params.put("pageSize", pageSize);
            int pageNum = count % 100 == 0 ? count / 100 : count / 100 + 1;
            int offSet;
            for (int i = 0; i < pageNum; i++) {
                offSet = i * pageSize;
                params.put("offSet", offSet);
                ids = mmanLoanCollectionStatusChangeLogDao.getOrderIds(params);
                logData = mmanLoanCollectionStatusChangeLogDao.getLogByOrderIds(ids);
                if (CollectionUtils.isNotEmpty(logData)) {
                    groupData = logData.stream().collect(Collectors.groupingBy(MmanLoanCollectionStatusChangeLog::getLoanId));
                    groupData.forEach((k, v) -> {
                        //查询逾期续期及逾期还款时间
                        List<Date> renewTime = dataDao.getOrderRenewTime(v.get(0).getRepayId());
                        Date repayTime = dataDao.getOrderRepayTime(v.get(0).getRepayId());
                        dealLog(v, renewTime, repayTime);
                    });
                }
            }


        } catch (Exception e) {
            log.error("addBeforeOrderCycle error!", e);
        }

    }

    @SuppressWarnings("unchecked")
    private void dealLog(List<MmanLoanCollectionStatusChangeLog> logs, List<Date> renewTime, Date repayTime) {
        Map<String, List<MmanLoanCollectionStatusChangeLog>> groupData;
        if (logs.size() >= 2) {
            groupData = logs.stream().collect(Collectors.groupingBy(MmanLoanCollectionStatusChangeLog::getCurrentCollectionUserLevel));
            List<Date> s1TimeList = new ArrayList<>();
            List<Date> s2TimeList = new ArrayList<>();
            List<OrderLifeCycle> s2List = new ArrayList<>();
            List<Date> s3TimeList = new ArrayList<>();
            List<OrderLifeCycle> s3List = new ArrayList<>();
            Set<String> keySet = groupData.keySet();
            final CopyOnWriteArraySet<String> cowSet = new CopyOnWriteArraySet(keySet);
            groupData.forEach((k, v) -> {
                v.sort(Comparator.comparing(MmanLoanCollectionStatusChangeLog::getCreateDate));
                //过滤短间隔时间重复派单数据
                boolean isRemove = false;
                for (int i = 0; i < v.size(); i++) {
                    if (i <= v.size() - 2) {
                        switch (k) {
                            case "3":
                                try {
                                    isRemove = DateUtil.daysBetween(v.get(i).getCreateDate(), v.get(i + 1).getCreateDate()) < 8;
                                } catch (ParseException e) {
                                    log.error("dealLog parseDate error!", e);
                                }
                                break;
                            case "4":
                                try {
                                    isRemove = DateUtil.daysBetween(v.get(i).getCreateDate(), v.get(i + 1).getCreateDate()) < 10;
                                } catch (ParseException e) {
                                    log.error("dealLog parseDate error!", e);
                                }
                                break;
                            case "8":
                                try {
                                    isRemove = DateUtil.daysBetween(v.get(i).getCreateDate(), v.get(i + 1).getCreateDate()) < 22;
                                } catch (ParseException e) {
                                    log.error("dealLog parseDate error!", e);
                                }
                                break;
                            case "5":
                                v.removeAll(v.subList(1, v.size()));
                                break;
                            default:
                        }
                        if (isRemove) {
                            v.remove(i + 1);
                            i--;
                        }
                    }
                }
            });

            while (cowSet.size() > 0) {
                for (String k : cowSet) {
                    List<MmanLoanCollectionStatusChangeLog> v = groupData.get(k);
                    switch (k) {
                        case "3":
                            v.forEach(one -> {
                                Integer higherCount = orderLifeCycleDao.findHigherCount(one.getLoanId(), one.getCreateDate(), BackConstant.XJX_OVERDUE_LEVEL_S1);
                                if (higherCount > 0) {
                                    return;
                                }
                                OrderLifeCycle cycle = buildCycle(one);
                                cycle.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S1));
                                cycle.setS1Time(one.getCreateDate());
                                s1TimeList.add(one.getCreateDate());
                                setCurrentStatus(repayTime, cycle, renewTime, one);
                                orderLifeCycleDao.insertSelective(cycle);
                            });
                            cowSet.remove(k);
                            break;
                        case "4":
                            if (CollectionUtils.isNotEmpty(s1TimeList)) {
                                v.forEach(one -> {
                                    s1TimeList.add(one.getCreateDate());
                                    s1TimeList.sort(Comparator.naturalOrder());
                                    int place = s1TimeList.indexOf(one.getCreateDate());
                                    if (place == 0) {
                                        s1TimeList.remove(one.getCreateDate());
                                        return;
                                    }
                                    Integer existNum = orderLifeCycleDao.findHigherCount(one.getLoanId(), s1TimeList.get(place - 1), BackConstant.XJX_OVERDUE_LEVEL_S2);
                                    if (existNum > 0) {
                                        s1TimeList.remove(one.getCreateDate());
                                        return;
                                    }
                                    OrderLifeCycle cycle = buildCycle(one);
                                    cycle.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S2));
                                    cycle.setS1Time(s1TimeList.get(place - 1));
                                    setCurrentStatus(repayTime, cycle, renewTime, one);
                                    orderLifeCycleDao.insertSelective(cycle);
                                    s1TimeList.remove(one.getCreateDate());
                                    s2TimeList.add(one.getCreateDate());
                                    s2List.add(cycle);
                                });
                                cowSet.remove(k);
                            } else if (!cowSet.contains("3")) {
                                cowSet.remove(k);
                            }
                            break;
                        case "8":
                            if (CollectionUtils.isNotEmpty(s2TimeList)) {
                                v.forEach(one -> {
                                    s2TimeList.add(one.getCreateDate());
                                    s2TimeList.sort(Comparator.naturalOrder());
                                    int place = s2TimeList.indexOf(one.getCreateDate());
                                    if (place == 0) {
                                        s2TimeList.remove(one.getCreateDate());
                                        return;
                                    }
                                    Integer existNum = orderLifeCycleDao.findHigherCount(one.getLoanId(), getS1Time(s2List, s2TimeList.get(place - 1)), BackConstant.XJX_OVERDUE_LEVEL_S3);
                                    if (existNum > 0) {
                                        s2TimeList.remove(one.getCreateDate());
                                        return;
                                    }
                                    OrderLifeCycle cycle = buildCycle(one);
                                    cycle.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S3));
                                    cycle.setS1Time(getS1Time(s2List, s2TimeList.get(place - 1)));
                                    setCurrentStatus(repayTime, cycle, renewTime, one);
                                    orderLifeCycleDao.insertSelective(cycle);
                                    s2TimeList.remove(one.getCreateDate());
                                    s3TimeList.add(one.getCreateDate());
                                    s3List.add(cycle);
                                });
                                cowSet.remove(k);
                            } else if (!cowSet.contains("4")) {
                                cowSet.remove(k);
                            }
                            break;
                        case "5":
                            if (CollectionUtils.isNotEmpty(s3TimeList)) {
                                v.forEach(one -> {
                                    s3TimeList.add(one.getCreateDate());
                                    s3TimeList.sort(Comparator.naturalOrder());
                                    int place = s3TimeList.indexOf(one.getCreateDate());
                                    if (place == 0) {
                                        s3TimeList.remove(one.getCreateDate());
                                        return;
                                    }
                                    OrderLifeCycle cycle = buildCycle(one);
                                    cycle.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_M1_M2));
                                    cycle.setS1Time(getS1Time(s3List, s3TimeList.get(place - 1)));
                                    setCurrentStatus(repayTime, cycle, renewTime, one);
                                    orderLifeCycleDao.insertSelective(cycle);
                                    s3TimeList.remove(one.getCreateDate());
                                });
                                cowSet.remove(k);
                            } else if (!cowSet.contains("8")) {
                                cowSet.remove(k);
                            }
                            break;
                        default:
                    }
                }
            }
        } else if (logs.size() == 1) {
            MmanLoanCollectionStatusChangeLog log = logs.get(0);
            if ("3".equals(log.getCurrentCollectionUserLevel())) {
                OrderLifeCycle cycle = buildCycle(log);
                cycle.setCurrentLevel(new Byte(BackConstant.XJX_OVERDUE_LEVEL_S1));
                cycle.setS1Time(log.getCreateDate());
                setCurrentStatus(repayTime, cycle, renewTime, log);
                orderLifeCycleDao.insertSelective(cycle);
            }
        }

    }

    private void setCurrentStatus(Date repayTime, OrderLifeCycle cycle, List<Date> renewTime, MmanLoanCollectionStatusChangeLog one) {
        if (repayTime != null) {
            try {
                int days = DateUtil.daysBetween(one.getCreateDate(), repayTime);
                if (days >= 0 && days < 3) {
                    cycle.setCurrentStatus(new Byte(Constant.STATUS_OVERDUE_FOUR));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (CollectionUtils.isNotEmpty(renewTime)) {
            for (Date oneTime : renewTime) {
                try {
                    int days = DateUtil.daysBetween(one.getCreateDate(), oneTime);
                    if (days >= 0 && days < 3) {
                        cycle.setCurrentStatus(new Byte(Constant.STATUS_OVERDUE_FIVE));
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (cycle.getCurrentStatus() == null) {
            cycle.setCurrentStatus(new Byte(Constant.STATUS_OVERDUE_ONE));
        }
    }

    private Date getS1Time(List<OrderLifeCycle> cycles, Date target) {
        Date result = null;
        for (OrderLifeCycle one : cycles) {
            if (one.getCreateTime().equals(target)) {
                result = one.getS1Time();
                break;
            }
        }
        return result;
    }

    private OrderLifeCycle buildCycle(MmanLoanCollectionStatusChangeLog one) {
        OrderLifeCycle cycle = new OrderLifeCycle();
        cycle.setLoanId(Integer.valueOf(one.getLoanId()));
        cycle.setPayId(Integer.valueOf(one.getRepayId()));
        cycle.setCreateTime(one.getCreateDate());
        return cycle;
    }

    @RequestMapping("handCountCollectionResult")
    public void handCountCollectionResult(HttpServletRequest request) {
        log.info("handCountCollectionResult start!");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        try {
            collectionStatisticsService.countCollectionResult(startTime, endTime);
        } catch (Exception e) {
            log.error("handCountCollectionResult error:{}", e);
        }
    }

    @RequestMapping("toCollectionCountPage")
    public String toCollectionCountPage(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        boolean isStartEmpty = params.get("startDate") == null || StringUtils.isEmpty(params.get("startDate").toString());
        boolean isEndEmpty = params.get("endDate") == null || StringUtils.isEmpty(params.get("endDate").toString());
        if (isEndEmpty) {
            params.put("endDate", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
        }
        if (!isStartEmpty) {
            PageConfig<CollectionSucCount> pm = collectionService.findCollecSucCountPage(params);
            model.addAttribute("pm", pm);
        }
        model.addAttribute("params", params);

        return "statistics/collect_suc_count";
    }

    /**
     * 催收绩效统计excel导出
     */
    @RequestMapping("/collectSucCountExcelReport")
    public void collectSucCountExcelReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            boolean isStartEmpty = params.get("startDate") == null || StringUtils.isEmpty(params.get("startDate").toString());
            boolean isEndEmpty = params.get("endDate") == null || StringUtils.isEmpty(params.get("endDate").toString());
            if (isStartEmpty && isEndEmpty) {
                params.put("startDate", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
            }
            int size = 50000;
            int total = 0;
            params.put(Constant.PAGE_SIZE, size);
            int totalPageNum = collectionService.findAllCount(params);
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            OutputStream os = response.getOutputStream();
            response.reset();// 清空输出流
            ExcelUtil.setFileDownloadHeader(request, response, "续还统计.xlsx");
            // 定义输出类型
            response.setContentType("application/msexcel");
            SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
            String[] titles = {"序号", "催收员", "进单量", "已催回订单量", "续期订单量", "还款订单量", "回款率"};
            for (int i = 1; i <= total; i++) {
                params.put(Constant.CURRENT_PAGE, i);
                PageConfig<CollectionSucCount> pm = collectionService.findCollecSucCountPage(params);
                List<CollectionSucCount> list = pm.getItems();
                List<Object[]> contents = new ArrayList<>();
                int index = 1;
                for (CollectionSucCount r : list) {
                    String[] conList = new String[titles.length];
                    conList[0] = index + "";
                    conList[1] = r.getUserName();
                    conList[2] = r.getIntoNum().toString();
                    conList[3] = r.getSucNum().toString();
                    conList[4] = r.getRenewNum().toString();
                    conList[5] = r.getRepayNum().toString();
                    conList[6] = r.getSucRate().toString();
                    contents.add(conList);
                    index++;
                }
                ExcelUtil.buildExcel(workbook, "续还统计", titles, contents, i, total, os);
            }
        } catch (Exception e) {
            log.error("collectSucCountExcelReport error : {}", e);
        }
    }

}
