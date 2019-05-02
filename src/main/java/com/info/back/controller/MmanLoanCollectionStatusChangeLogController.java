package com.info.back.controller;

import com.info.back.service.IBackRoleService;
import com.info.back.service.IBackUserService;
import com.info.back.service.IMmanLoanCollectionStatusChangeLogService;
import com.info.back.service.ISysDictService;
import com.info.back.utils.BackConstant;
import com.info.web.pojo.cspojo.*;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/collectionRecordStatusChangeLog")
public class MmanLoanCollectionStatusChangeLogController extends BaseController {

    @Resource
    private IMmanLoanCollectionStatusChangeLogService mmanLoanCollectionStatusChangeLogService;
    @Resource
    private IBackUserService backUserService;
    @Resource
    private ISysDictService sysDictService;
    @Resource
    private IBackRoleService backRoleService;

    @RequestMapping("/getMmanLoanCollectionStatusChangeLog")
    public String getView(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);

            BackUser backUser = getSessionUser(request);

            List<BackUserCompanyPermissions> companyPermissions = backUserService
                    .findCompanyPermissions(backUser.getId());
            if (companyPermissions != null
                    && companyPermissions.size() > 0) {// 指定公司的订单
                params.put("CompanyPermissionsList", companyPermissions);
            }
            // 催收员可以看自己的
            BackRole topCsRole = backRoleService.getTopCsRole();
            List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
            if (backUser.getRoleId() != null && roleChildIds.contains(Integer.valueOf(backUser.getRoleId()))) {
                params.put("userRoleId", backUser.getUuid());
            }
//			if (StringUtils.isEmpty(params.get("type"))) {
//				params.put("type", "2");
//			}
            if ("0".equals(params.get("type"))) {
                params.put("type", "");
            }
            // 默认查看近一周的信息
            if (StringUtils.isEmpty(params.get("beginTime")) && StringUtils.isEmpty(params.get("endTime"))) {
                params.put("beginTime", DateUtil.getDateFormat(DateUtil.getDayFirst(), "yyyy-MM-dd"));
//				params.put("beginTime",DateUtil.getDateFormat(DateUtil.getBeforeOrAfter(new Date(),-7),"yyyy-MM-dd"));
                params.put("endTime", DateUtil.getDateFormat("yyyy-MM-dd"));
            }
            // // 用户是经理或者催收主管可以查看全部催收流转日志
            // if (backUser.getRoleId() != null
            // && (BackConstant.COLLECTION_MANAGE_ROLE_ID.toString()
            // .equals(backUser.getRoleId()) || BackConstant.MANAGER_ROLE_ID
            // .toString().equals(backUser.getRoleId()))) {
            // params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            // }
            // // 委外经理看自己公司的
            // if (backUser.getRoleId() != null
            // && BackConstant.OUTSOURCE_MANAGER_ROLE_ID.toString().equals(
            // backUser.getRoleId())) {
            // params.put("outSourceCompanyId", backUserDao
            // .getCompanyId(backUser.getId()));
            // }
            PageConfig<MmanLoanCollectionStatusChangeLog> pageConfig = mmanLoanCollectionStatusChangeLogService
                    .findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
            // 催收组(逾期等级)
            List<SysDict> groupist = sysDictService
                    .getStatus("collection_group");
            Map<String, String> dictMap = BackConstant.orderState(groupist);
            model.addAttribute("dictMap", dictMap);
            // 订单状态
            List<SysDict> orderStatusList = sysDictService
                    .getStatus("xjx_collection_order_state");
            Map<String, String> orderStatusMap = BackConstant.orderState(orderStatusList);
            model.addAttribute("orderStatusMap", orderStatusMap);
            // 催收状态流转类型
            List<SysDict> collectionStatusMoveTypeList = sysDictService
                    .getStatus("xjx_collection_status_move_type");
            Map<String, String> collectionStatusMoveTypeMap = BackConstant.orderState(collectionStatusMoveTypeList);
            model.addAttribute("collectionStatusMoveTypeMap", collectionStatusMoveTypeMap);
            // List<SysDict> orderlist =
            // sysDictService.getStatus("collection_group");
            // HashMap<String, String> orderMap =
            // BackConstant.orderState(orderlist);
            // model.addAttribute("currentCollectionOrderLevel", orderlist);
            // model.addAttribute("dictMap", orderMap);
        } catch (Exception e) {
            log.error("getMmanLoanCollectionStatusChangeLog error", e);
        }
        return "mmanLoanCollectionStatusChangeLog/mmanLoanCollectionStatusChangeLogList";
    }
}