package com.info.back.controller;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.annotation.OperaLogAnno;
import com.info.back.result.JsonResult;
import com.info.back.service.IAuditCenterService;
import com.info.back.service.IMmanLoanCollectionCompanyService;
import com.info.back.service.ISysDictService;
import com.info.back.utils.BackConstant;
import com.info.back.utils.DwzResult;
import com.info.back.utils.SpringUtils;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.AuditCenter;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;
import com.info.web.pojo.cspojo.SysDict;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信息审核
 *
 * @author xieyaling
 * @date 2017-02-24
 */
@Slf4j
@Controller
@RequestMapping("auditCenter/")
public class AuditCenterController extends BaseController {

    @Resource
    private IAuditCenterService auditCenterService;
    @Resource
    private ISysDictService sysDictService;
    @Resource
    private IMmanLoanCollectionCompanyService mmanLoanCollectionCompanyService;

    /**
     * 查询信息审核列表
     */
    @RequestMapping("getAuditCenterPage")
    public String getCompanyPage(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            //add by 170308 审核列表默认申请中状态
            if (!params.containsKey("status") && params.containsKey("_")) {
                params.put("status", "0");
                params.put("pageNum", 1);
            }
            BackUser backUser = this.loginAdminUser(request);
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            if (!"1".equals(backUser.getCompanyId())) {
                params.put("companyId", backUser.getCompanyId());
            }

            // 查询公司列表
            MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
            List<MmanLoanCollectionCompany> listMmanLoanCollectionCompany = mmanLoanCollectionCompanyService.getList(mmanLoanCollectionCompany);
            model.addAttribute("listMmanLoanCollectionCompany", listMmanLoanCollectionCompany);
            PageConfig<AuditCenter> pageConfig = auditCenterService.findPage(params);
            log.info("pageConfig: " + pageConfig);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
            model.addAttribute("userGropLeval", backUser.getRoleId());
            if (params.get("beginTime") == null && params.get("endTime") == null) {
                params.put("beginTime", DateUtil.getDateFormat("yyyy-MM-dd"));
                params.put("endTime", DateUtil.getDateFormat("yyyy-MM-dd"));

            }
            //审核类型
            List<SysDict> levellist = sysDictService.getStatus("xjx_auditcenter_type ");
            model.addAttribute("levellist", levellist);
            HashMap<String, String> levelMap = BackConstant.orderState(levellist);
            model.addAttribute("typeMap", levelMap);
        } catch (Exception e) {
            log.error("getCompanyPage error", e);
        }
        return "auditCenter/auditCenterList";
    }


    @ExcludeUrl
    @RequestMapping("addAuditCenter")
    public String addAuditCenter(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult("-1", "操作失败");
        Map<String, String> params = this.getParameters(request);
        try {
            BackUser backUser = this.loginAdminUser(request);
            params.put("operationUserId", backUser.getId().toString());
            result = auditCenterService.svueAuditCenter(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT, "");
        model.addAttribute("params", params);
        return null;
    }

    @RequestMapping("toUpdateAuditCenter")
    public String toUpdateAuditCenter(HttpServletRequest request, Model model) {
        Map<String, String> params = this.getParameters(request);
        model.addAttribute("params", params);
        return "auditCenter/updateAudit";
    }


    @OperaLogAnno(operationType = "审核中心", operationName = "减免审核")
    @RequestMapping("updateAuditCenter")
    public String updateAuditCenter(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult("-1", "操作失败");
        Map<String, String> params = this.getParameters(request);
        if (params.get("id") == null || "".equals(params.get("id"))) {
            result.setMsg("请选中!");
        } else {
            try {
                result = auditCenterService.updateAuditCenter(params);
            } catch (Exception e) {
                log.error("updateAuditCenter", e);
            }
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId"));
        model.addAttribute("params", params);
        return null;
    }

    @RequestMapping("toUpdateAuditCenterStatus")
    public String toUpdateAuditCenterStatus(HttpServletRequest request, Model model) {
        Map<String, String> params = this.getParameters(request);
        model.addAttribute("params", params);
        return "auditCenter/statusUpdate";
    }

    @RequestMapping("updateAuditCenterStatus")
    public String updateAuditCenterStatus(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult("-1", "操作失败");
        Map<String, String> params = this.getParameters(request);
        try {
            result = auditCenterService.updateAuditCenter(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId"));
        model.addAttribute("params", params);
        return null;
    }

    @RequestMapping("fingAllPageList")
    public String fingAllPageList(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            PageConfig<AuditCenter> pageConfig = auditCenterService.findAllPage(params);
            model.addAttribute("pm", pageConfig);

            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("getCompanyPage error", e);
        }
        return "mycollectionorder/getorderPage";
    }
}
