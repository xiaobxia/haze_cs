package com.info.back.controller;

import com.alibaba.fastjson.JSONArray;
import com.info.back.service.*;
import com.info.back.utils.BackConstant;
import com.info.back.utils.ExcelUtil;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.*;
import com.info.web.util.CompareUtils;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mmanLoanCollectionOrder")
public class MmanLoanCollectionOrderController extends BaseController {

    @Resource
    private IMmanLoanCollectionOrderService mmanLoanCollectionOrderService;
    @Resource
    private IMmanLoanCollectionCompanyService mmanLoanCollectionCompanyService;
    @Resource
    private IBackUserService backUserService;
    @Resource
    private ISysDictService sysDictService;
    @Resource
    private IBackRoleService backRoleService;


    @RequestMapping("/technologyOrderList")
    public String technologyOrderList(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            //查询公司列表
            MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
            PageConfig<OrderBaseResult> pageConfig = mmanLoanCollectionOrderService.getPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
            model.addAttribute("ListMmanLoanCollectionCompany", mmanLoanCollectionCompanyService.getList(mmanLoanCollectionCompany));
            model.addAttribute("dictMap", BackConstant.GROUP_NAME_MAP);
        } catch (Exception e) {
            log.error("getMmanLoanCollectionOrder error", e);
        }
        return "order/technologyOrderList";
    }

    @RequestMapping("/getMmanLoanCollectionOrderPage")
    public String getPage(HttpServletRequest request, Model model) {
        String url = "order/orderList";
        HashMap<String, Object> params = getParametersO(request);
        if (params.get("check_person") != null && StringUtils.isNotEmpty(params.get("check_person").toString())) {
            url = "order/check_person_orderList";
            model.addAttribute("check_person", true);
            params.put("status", "1");
        }
        BackUser backUser = getSessionUser(request);

        List<BackUserCompanyPermissions> companyPermissions = backUserService.findCompanyPermissions(backUser.getId());
        //指定公司的订单
        if (companyPermissions != null && companyPermissions.size() > 0) {
            params.put("CompanyPermissionsList", companyPermissions);
        }
        //若组没有 ，则默认查询S1 组
//			if(null==params.get("collectionGroup") || StringUtils.isBlank(String.valueOf(params.get("collectionGroup")))){
//				params.put("collectionGroup", "3");
//			}
        //查询公司列表
        MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
        PageConfig<OrderBaseResult> pageConfig = mmanLoanCollectionOrderService.getPage(params);
        model.addAttribute("pm", pageConfig);
        model.addAttribute("params", params);
        model.addAttribute("ListMmanLoanCollectionCompany", mmanLoanCollectionCompanyService.getList(mmanLoanCollectionCompany));
        model.addAttribute("dictMap", BackConstant.GROUP_NAME_MAP);
        String groupStr = params.get("collectionGroup") == null ? "" : params.get("collectionGroup").toString();
        JSONArray array = new JSONArray(3);
        switch (groupStr) {
            case "3":
                array.add("3");
                break;
            case "4":
                array.add("15");
                break;
            case "8":
                array.add("30");
                break;
            default:
                array.add("3");
                array.add("15");
                array.add("30");
                break;
        }
        model.addAttribute("term", array);

        return url;
    }

    /**
     * 订单导出
     */
    @RequestMapping("/execlTotalOder")
    public void reportManage(HttpServletResponse response, HttpServletRequest request) {
        HashMap<String, Object> params = getParametersO(request);
        try {
            if (MapUtils.isNotEmpty(params) && params.get("ids") != null) {
                params.put("ids", Arrays.asList(params.get("ids").toString().split(",")));
            }
            BackUser backUser = getSessionUser(request);

            List<BackUserCompanyPermissions> companyPermissions = backUserService.findCompanyPermissions(backUser.getId());
            if (companyPermissions != null) {
                params.put("CompanyPermissionsList", companyPermissions);
            }
            BackRole topCsRole = backRoleService.getTopCsRole();
            List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
            if (backUser.getRoleId() != null && roleChildIds.contains(Integer.valueOf(backUser.getRoleId()))) {
                params.put("roleUserId", backUser.getId());
            }
            int size = 50000;
            int total = 0;
            params.put(Constant.PAGE_SIZE, size);
            int totalPageNum = mmanLoanCollectionOrderService.findAllCount(params);
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            OutputStream os = response.getOutputStream();
            response.reset();// 清空输出流
            ExcelUtil.setFileDownloadHeader(request, response, "催收订单.xlsx");
            // 定义输出类型
            response.setContentType("application/msexcel");
            SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
            String[] titles = {"借款编号", "催收公司", "催收组", "借款人姓名", "借款手机号", "借款金额", "逾期天数",
                    "滞纳金", "催收状态", "应还时间", "已还金额", "最新还款时间", "最后催收时间", "承诺还款时间", "派单时间", "当前催收员", "上一催收员", "减免滞纳金",};
            List<SysDict> dictlist = sysDictService.getStatus("collection_group");
            HashMap<String, String> dictMap = BackConstant.orderState(dictlist);
            List<SysDict> statulist = sysDictService.getStatus("xjx_collection_order_state");
            HashMap<String, String> statuMap = BackConstant.orderState(statulist);
            for (int i = 1; i <= total; i++) {
                params.put(Constant.CURRENT_PAGE, i);
                PageConfig<OrderBaseResult> pm = mmanLoanCollectionOrderService.getPage(params);
                List<OrderBaseResult> list = pm.getItems();
                List<Object[]> contents = new ArrayList<>();
                for (OrderBaseResult r : list) {
                    String[] conList = new String[titles.length];
                    conList[0] = r.getLoanId();
                    conList[1] = r.getCompanyTile();
                    conList[2] = dictMap.get(r.getCollectionGroup() + "");
                    conList[3] = r.getRealName();
                    conList[4] = r.getPhoneNumber();
                    conList[5] = r.getLoanMoney() == null ? "0" : r.getLoanMoney() + "";
                    conList[6] = r.getOverdueDays() + "";
                    conList[7] = r.getLoanPenlty() + "";
                    conList[8] = statuMap.get(r.getCollectionStatus());
                    conList[9] = r.getLoanEndTime() == null ? "" : DateUtil.getDateFormat(r.getLoanEndTime(), "yyyy-MM-dd HH:mm:ss");
                    conList[10] = r.getReturnMoney() == null ? "0" : r.getReturnMoney() + "";
                    if (r.getReturnMoney() != null && CompareUtils.greaterThanZero(r.getReturnMoney())) {
                        conList[11] = r.getCurrentReturnDay() == null ? "" : DateUtil.getDateFormat(r.getCurrentReturnDay(), "yyyy-MM-dd HH:mm:ss");
                    } else {
                        conList[11] = "";
                    }
                    conList[12] = r.getLastCollectionTime() == null ? "" : DateUtil.getDateFormat(r.getLastCollectionTime(), "yyyy-MM-dd HH:mm:ss");
                    conList[13] = r.getPromiseRepaymentTime() == null ? "" : DateUtil.getDateFormat(r.getPromiseRepaymentTime(), "yyyy-MM-dd HH:mm:ss");
                    conList[14] = r.getDispatchTime() == null ? "" : DateUtil.getDateFormat(r.getDispatchTime(), "yyyy-MM-dd HH:mm:ss");
                    conList[15] = r.getCurrUserName() == null ? "" : r.getCurrUserName();
                    conList[16] = r.getLastUserName() == null ? "" : r.getLastUserName();
                    conList[17] = String.valueOf(r.getReductionMoney() == null ? "" : r.getReductionMoney());
                    contents.add(conList);
                }
                ExcelUtil.buildExcel(workbook, "催收订单", titles, contents, i, total, os);
            }
        } catch (Exception e) {
            log.error("催收订单导出excel失败", e);
        }
    }


}