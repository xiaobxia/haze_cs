package com.info.back.controller;

import com.info.back.service.*;
import com.info.back.utils.BackConstant;
import com.info.back.utils.ExcelUtil;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.*;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("statistics/")
public class CollectionStatisticsController extends BaseController {

    @Resource
    private ICollectionStatisticsService collectionStatisticsService;

    @Resource
    private ICountCollectionAssessmentService countCollectionAssessmentService;
    @Resource
    private ICountCollectionManageService countCollectionManageService;
    @Resource
    private ISysDictService sysDictService;
    @Resource
    private IMmanLoanCollectionCompanyService mmanLoanCollectionCompanyService;

    /**
     * 业务量统计_金额
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("businessMoney")
    public String countByMoney(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            if (StringUtils.isEmpty(params.get("begDate")) && StringUtils.isEmpty(params.get("endDate"))) {
                params.put("endDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
                params.put("begDate", DateUtil.getDateForDayBefor(7, "yyyy-MM-dd"));
            }
            CollectionStatistics cs = collectionStatisticsService.countPrincipalNew(params);
            List<Object> line = collectionStatisticsService.countBySevenDay(params);
//			StatisticsDistribute bjfb = collectionStatisticsService.countByDistribute(params);
            List<StatisticsDistribute> bjfbList = collectionStatisticsService.countByDistributeNew(params);
            model.addAttribute("bjzj", cs);
//			model.addAttribute("line", line);
//			model.addAttribute("bjfb", bjfb);
            model.addAttribute("bjfbList", bjfbList);
            model.addAttribute("type", "line");
            model.addAttribute("params", params);

        } catch (Exception e) {
            log.error("countByMoney error", e);
        }
        return "statistics/business";
    }

    /**
     * 业务量统计
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("businessOrder")
    public String countByOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            if (StringUtils.isEmpty(params.get("begDate")) && StringUtils.isEmpty(params.get("endDate"))) {
                params.put("endDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
                params.put("begDate", DateUtil.getDateForDayBefor(7, "yyyy-MM-dd"));
            }
            CollectionStatisticsOrder cs = collectionStatisticsService.countPrincipalOrder(params);
            List<Object> line = collectionStatisticsService.countOrderBySevenDay(params);
            model.addAttribute("bjzj", cs);
//			model.addAttribute("line", line);
            model.addAttribute("type", "line");

            model.addAttribute("params", params);

        } catch (Exception e) {
            log.error("countByOrder error", e);
        }
        return "statistics/business_order";
    }

    /**
     * 考核统计
     *
     */
    @RequestMapping("/countAssessmentPage")
    public String getCountCollectionAssessmentPage(HttpServletRequest request,
                                                   HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            BackUser backUser = getSessionUser(request);

            if (StringUtils.isEmpty(params.get("begDate")) && StringUtils.isEmpty(params.get("endDate"))) {
                //如果当日是1号,展示上个月的1号到最后一天 ; 不是的话展示当月1号到当天
                if (DateUtil.getDayFirst().equals(new Date())) {
                    params.put("begDate", DateUtil.getDateFormat(DateUtil.getNextMon(-1), "yyyy-MM-dd"));
                    params.put("endDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
                } else {
                    params.put("begDate", DateUtil.getDateFormat(DateUtil.getDayFirst(), "yyyy-MM-dd"));
                    params.put("endDate", DateUtil.getDateForDayBefor(0, "yyyy-MM-dd"));
                }
            }
            // 统计考核-公司  默认S1
            if ("GS".equals(params.get("type"))) {
                params.put("groupId", 3);
                params.put("orderGroupId", 3);
            }
            if (!StringUtils.isEmpty(params.get("groupType"))) {
                params.put("groupId", String.valueOf(params.get("groupType")).substring(0).substring(0, 1));
                params.put("orderGroupId", String.valueOf(params.get("groupType")).substring(1));
            }
            // 如果用户是催收员,只能看自己的相关统计
            if (backUser.getRoleId().equals(BackConstant.COLLECTION_ROLE_ID.toString())) {
                params.put("personId", backUser.getUuid());
                params.put("roleId", backUser.getRoleId());
                params.put("personName", backUser.getUserName());
            }
            // 如果用户是委外经理,只能看自己公司的相关统计
            if (backUser.getRoleId().equals(BackConstant.OUTSOURCE_MANAGER_ROLE_ID.toString())) {
                params.put("companyId", backUser.getCompanyId());
            }

            PageConfig<CountCollectionAssessment> pageConfig = countCollectionAssessmentService.findPage(params);
            List<SysDict> dict = sysDictService.findDictByType("xjx_overdue_level");
            List<MmanLoanCollectionCompany> company = mmanLoanCollectionCompanyService.findCompanyByUserId(backUser);
            model.addAttribute("groupNameTypeMap", BackConstant.GROUP_NAME_TYPE_MAP);
            model.addAttribute("groupLevel", String.valueOf(params.get("groupType")));
            model.addAttribute("pm", pageConfig);
            model.addAttribute("dict", dict);
            model.addAttribute("company", company);
            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("getCountCollectionAssessmentPage error", e);
        }
        return "statistics/assessmentCountList";
    }

    /**
     * 考核统计导出
     *
     */
    @RequestMapping("/reportAssessment")
    public void reportAssessment(HttpServletResponse response, HttpServletRequest request) {
        HashMap<String, Object> params = getParametersO(request);
        try {
            BackUser backUser = getSessionUser(request);

            if (BackConstant.COLLECTION_ROLE_ID.equals(backUser.getRoleId())) {
                params.put("personId", backUser.getUuid());
            }
            params.put("groupId", null);
            params.put("orderGroupId", null);
            if (!StringUtils.isEmpty(params.get("groupType"))) {
                params.put("groupId", String.valueOf(params.get("groupType")).substring(0, 1));
                params.put("orderGroupId", String.valueOf(params.get("groupType")).substring(1));
            }
            params.put("method", "C");
            int size = 50000;
            int total = 0;
            params.put(Constant.PAGE_SIZE, size);
            int totalPageNum = countCollectionAssessmentService.findAllCount(params);
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            String title = "考核统计-每日";
            String[] titles = {"日期", "催收公司", "催收组", "订单组", "催收员", "本金金额",
                    "已还本金", "未还本金", "本金摧回率", "迁徙率", "滞纳金金额", "已还滞纳金",
                    "未还滞纳金", "滞纳金摧回率", "订单量", "已还订单量", "订单还款率"}; // "已操作订单","风控标记单量",
            if ("LJ".equals(params.get("type"))) {
                titles[0] = "序号";
                title = "考核统计-累计";
            }
            OutputStream os = response.getOutputStream();
            response.reset();// 清空输出流
            ExcelUtil.setFileDownloadHeader(request, response, title + ".xls");
            response.setContentType("application/msexcel");// 定义输出类型
            SXSSFWorkbook workbook = new SXSSFWorkbook(10000);

            int j = 1;
            for (int i = 1; i <= total; i++) {
                params.put(Constant.CURRENT_PAGE, i);
                PageConfig<CountCollectionAssessment> pm = countCollectionAssessmentService.findPage(params);
                List<CountCollectionAssessment> list = pm.getItems();
                List<Object[]> contents = new ArrayList<Object[]>();
                for (CountCollectionAssessment r : list) {
                    String[] conList = new String[titles.length];
                    if ("LJ".equals(params.get("type"))) {
                        conList[0] = j + "";
                    } else {
                        conList[0] = DateUtil.getDateFormat(r.getCountDate(), "yyyy-MM-dd");
                    }
                    conList[1] = r.getCompanyTitle();
                    conList[2] = r.getGroupName();
                    conList[3] = r.getGroupOrderName();
                    conList[4] = r.getPersonName();
                    conList[5] = r.getLoanMoney().toString();
                    conList[6] = r.getRepaymentMoney().toString();
                    conList[7] = r.getNotYetRepaymentMoney().toString();
                    conList[8] = r.getRepaymentReta() + "%";
                    conList[9] = r.getMigrateRate() == null ? "--" : r.getMigrateRate().compareTo(new BigDecimal(-1)) == 0 ? "--" : r.getMigrateRate() + "%";
                    conList[10] = r.getPenalty().toString();
                    conList[11] = r.getRepaymentPenalty().toString();
                    conList[12] = r.getNotRepaymentPenalty().toString();
                    conList[13] = r.getPenaltyRepaymentReta() + "%";
                    conList[14] = r.getOrderTotal().toString();
//					conList[15] = r.getDisposeOrderNum().toString();
//					conList[16] = r.getRiskOrderNum().toString();
                    conList[15] = r.getRepaymentOrderNum().toString();
                    conList[16] = r.getRepaymentOrderRate() + "%";
                    contents.add(conList);
                    j++;
                }
                ExcelUtil.buildExcel(workbook, title, titles, contents, i, pm.getTotalPageNum(), os);
            }
        } catch (Exception e) {
            log.error("考核统计-每日导出excel失败", e);
        }
    }

    /**
     * 管理跟踪统计
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/countManagePage")
    public String getCountCollectionManagePage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        try {
            BackUser backUser = getSessionUser(request);

            if (StringUtils.isEmpty(params.get("begDate")) && StringUtils.isEmpty(params.get("endDate"))) {
                params.put("endDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
                params.put("begDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
            }

            // 如果用户是催收员,只能看自己的相关统计
            if (backUser.getRoleId().equals(BackConstant.COLLECTION_ROLE_ID.toString())) {
                params.put("personId", backUser.getUuid());
                params.put("roleId", backUser.getRoleId());
                params.put("personName", backUser.getUserName());
            }
            // 如果用户是委外经理,只能看自己公司的相关统计
            if (backUser.getRoleId().equals(BackConstant.OUTSOURCE_MANAGER_ROLE_ID.toString())) {
                params.put("companyId", backUser.getCompanyId());
            }

            PageConfig<CountCollectionManage> pageConfig = countCollectionManageService.findPage(params);
            List<SysDict> dict = sysDictService.findDictByType("xjx_overdue_level");
            List<MmanLoanCollectionCompany> company = mmanLoanCollectionCompanyService.findCompanyByUserId(backUser);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("dict", dict);
            model.addAttribute("company", company);
        } catch (Exception e) {
            log.error("getCountCollectionManagePage error", e);
        }
        model.addAttribute("params", params);
        return "statistics/manageCountList";
    }

    /**
     * 考核统计导出
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/reportManage")
    public void reportManage(HttpServletResponse response, HttpServletRequest request, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        try {
            BackUser backUser = getSessionUser(request);

            if (BackConstant.COLLECTION_ROLE_ID.equals(backUser.getRoleId())) {
                params.put("personId", backUser.getUuid());
            }
            int size = 50000;
            int total = 0;
            params.put(Constant.PAGE_SIZE, size);
            int totalPageNum = countCollectionManageService.findAllCount(params);
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            OutputStream os = response.getOutputStream();
            response.reset();// 清空输出流
            ExcelUtil.setFileDownloadHeader(request, response, "管理跟踪统计.xls");
            response.setContentType("application/msexcel");// 定义输出类型
            SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
            String[] titles = {"日期", "催收公司", "催收组", "订单组", "催收员", "本金金额",
                    "已还本金", "未还本金", "本金摧回率", "迁徙率", "滞纳金金额", "已还滞纳金",
                    // ,"已操作订单"
                    "未还滞纳金", "滞纳金摧回率", "订单量", "已还订单量", "订单还款率"};
            for (int i = 1; i <= total; i++) {
                params.put(Constant.CURRENT_PAGE, i);
                PageConfig<CountCollectionManage> pm = countCollectionManageService.findPage(params);
                List<CountCollectionManage> list = pm.getItems();
                List<Object[]> contents = new ArrayList<Object[]>();
                for (CountCollectionManage r : list) {
                    String[] conList = new String[titles.length];
                    if (r.getCountDate() != null) {
                        conList[0] = DateUtil.getDateFormat(r.getCountDate(), "yyyy-MM-dd");
                    }
                    conList[1] = r.getCompanyTitle();
                    conList[2] = r.getGroupName();
                    conList[3] = r.getGroupOrderName();
                    conList[4] = r.getPersonName();
                    conList[5] = r.getLoanMoney().toString();
                    conList[6] = r.getRepaymentMoney().toString();
                    conList[7] = r.getNotYetRepaymentMoney().toString();
                    conList[8] = r.getRepaymentReta() + "%";
                    conList[9] = r.getMigrateRate() + "%";
                    conList[10] = r.getPenalty().toString();
                    conList[11] = r.getRepaymentPenalty().toString();
                    conList[12] = r.getNotRepaymentPenalty().toString();
                    conList[13] = r.getPenaltyRepaymentReta() + "%";
                    conList[14] = r.getOrderTotal().toString();
//					conList[15] = r.getDisposeOrderNum().toString();
                    conList[15] = r.getRepaymentOrderNum().toString();
                    conList[16] = r.getRepaymentOrderRate() + "%";
                }
                ExcelUtil.buildExcel(workbook, "管理跟踪统计", titles, contents, i, pm.getTotalPageNum(), os);
            }
        } catch (Exception e) {
            log.error("管理跟踪统计导出excel失败", e);
        }
    }


    @RequestMapping("/countCashBusinessPage")
    public String getCountCashBusinessPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        try {
            BackUser backUser = getSessionUser(request);

            if (StringUtils.isEmpty(params.get("begDate")) && StringUtils.isEmpty(params.get("endDate"))) {
                params.put("endDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
                params.put("begDate", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
            }
            if (BackConstant.COLLECTION_ROLE_ID.equals(backUser.getRoleId())) {
                params.put("personId", backUser.getUuid());
            }
            PageConfig<CountCashBusiness> pageConfig = countCollectionManageService.getCountCashBusinessPage(params);
            model.addAttribute("pm", pageConfig);
        } catch (Exception e) {
            log.error("getCountCashBusinessPage error", e);
        }
        model.addAttribute("params", params);
        return "statistics/cashBusiness";
    }
}