package com.info.back.controller;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.dao.IBackUserDao;
import com.info.back.dao.IOdvRateDao;
import com.info.back.result.JsonResult;
import com.info.back.service.IBackRoleService;
import com.info.back.service.IBackUserService;
import com.info.back.service.ICollectionCompanyService;
import com.info.back.service.IMmanLoanCollectionRuleService;
import com.info.back.utils.BackConstant;
import com.info.back.utils.DwzResult;
import com.info.back.utils.SpringUtils;
import com.info.back.vo.OdvRate;
import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;
import com.info.web.pojo.cspojo.MmanLoanCollectionPerson;
import com.info.web.pojo.cspojo.MmanLoanCollectionRule;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("collectionRule/")
public class MmanLoanCollectionRuleController extends BaseController {


    @Resource
    private IMmanLoanCollectionRuleService mmanLoanCollectionRuleService;
    @Resource
    private ICollectionCompanyService collectionCompanyService;
    @Resource
    private IOdvRateDao odvRateDao;
    @Resource
    private IBackUserService backUserService;

    @RequestMapping("getMmanLoanCollectionRulePage")
    public String getCollectionRulePage(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);

            PageConfig<MmanLoanCollectionRule> pageConfig = mmanLoanCollectionRuleService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);

            List<MmanLoanCollectionCompany> companyList = collectionCompanyService.selectCompanyList();
            model.addAttribute("companyList", companyList);
            //用户组
            model.addAttribute("groupNameMap", BackConstant.GROUP_NAME_MAP);


        } catch (Exception e) {
            log.error("getCollectionRulePage error", e);
        }
        return "mmanLoanCollectionRule/mmanLoanCollectionRuleList";
    }

    @RequestMapping("updateMmanLoanCollectionRulePage")
    public String updateMmanLoanCollectionRulePage(HttpServletRequest request, HttpServletResponse response, Model model, MmanLoanCollectionRule collectionRule) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {

            List<MmanLoanCollectionCompany> companyList = collectionCompanyService.selectCompanyList();
            model.addAttribute("companyList", companyList);
            //用户组
            model.addAttribute("groupNameMap", BackConstant.GROUP_NAME_MAP);

            //更新页面转跳
            if ("toJsp".equals(params.get("type"))) {
                if (params.containsKey("id")) {
                    collectionRule = mmanLoanCollectionRuleService.getCollectionRuleById(params.get("id").toString());
                    model.addAttribute("collectionRule", collectionRule);
                }
                url = "mmanLoanCollectionRule/updateMmanLoanCollectionRule";
            } else {
                //更新或者添加操作(type非toJsp)
                JsonResult result = new JsonResult("-1", "操作失败");
                if (StringUtils.isNotBlank(collectionRule.getId())) {
                    result = mmanLoanCollectionRuleService.updateCollectionRule(collectionRule);
                }
//                else {
//					result = mmanLoanCollectionRuleService.insert(collectionRule);
//                }
                SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            if (e.getLocalizedMessage().contains("UK_user_account")) {
                erroMsg = "用户名重复！";
            }
            SpringUtils.renderDwzResult(response, false, "操作失败,原因："
                    + erroMsg, DwzResult.CALLBACK_CLOSECURRENT, params.get(
                    "parentId").toString());
            log.error("添加权限信息失败，异常信息:", e);
        }
        model.addAttribute(MESSAGE, erroMsg);
        model.addAttribute("params", params);
        return url;
    }


    @RequestMapping("getAssignRulePage")
    public String getAssignRulePage(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);

            PageConfig<MmanLoanCollectionRule> pageConfig = mmanLoanCollectionRuleService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);

            List<MmanLoanCollectionCompany> companyList = collectionCompanyService.selectCompanyList();
            model.addAttribute("companyList", companyList);

            model.addAttribute("groupNameMap", BackConstant.GROUP_NAME_MAP);//用户组


        } catch (Exception e) {
            log.error("getCollectionRulePage error", e);
        }
        return "mmanLoanCollectionRule/AssignRuleList";
    }

    @ExcludeUrl
    @RequestMapping("assignRateData")
    @ResponseBody
    public void assignRateData(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        MmanLoanCollectionRule rule = mmanLoanCollectionRuleService.getCollectionRuleById(id);
        if (rule != null) {
            List<OdvRate> data = odvRateDao.selectByGroupLevel(Integer.valueOf(rule.getCollectionGroup()));
            //初始化数据,查询该催收等级下所有可用催收员
            HashMap<String, Object> personMap = new HashMap<>(2);
            personMap.put("groupLevel", rule.getCollectionGroup());
            List<MmanLoanCollectionPerson> allPeople = backUserService.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(personMap);
            if (CollectionUtils.isEmpty(data)) {
                allPeople.forEach(this::intoOdv);
            } else {
                allPeople.forEach(one2 -> {
                    int count = odvRateDao.selectByOdvId(Integer.valueOf(one2.getId()));
                    if (count < 1) {
                        intoOdv(one2);
                    }
                });
            }
            data = odvRateDao.selectByGroupLevel(Integer.valueOf(rule.getCollectionGroup()));
            SpringUtils.renderJson(response,
                    JSONArray.fromObject(data).toString());
        }
    }

    private void intoOdv(MmanLoanCollectionPerson one) {
        OdvRate rate = new OdvRate();
        rate.setOdvId(Integer.valueOf(one.getId()));
        rate.setCompanyId(one.getCompanyId());
        rate.setGroupLevel(one.getGroupLevel());
        rate.setAssignRate(0.0);
        rate.setCreateTime(new Date());
        rate.setUpdateTime(rate.getCreateTime());
        odvRateDao.insert(rate);
    }


    @ExcludeUrl
    @RequestMapping("updateAssignToAuto")
    @ResponseBody
    public void updateAssignToAuto(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult("-1", "修改失败");
        HashMap<String, Object> params = this.getParametersO(request);
        try {
            String ruleId = request.getParameter("id");
            MmanLoanCollectionRule rule = mmanLoanCollectionRuleService.getCollectionRuleById(ruleId);
            if (rule != null) {
                rule.setAssignType(OdvRate.AUTO);
                mmanLoanCollectionRuleService.updateCollectionRule(rule);
            }
            result.setCode("0");
            result.setMsg("修改成功!");
        } catch (Exception e) {
            log.error("updateAssignToAuto error", e);
            result.setMsg("系异常请稍后重试");
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                DwzResult.CALLBACK_CLOSECURRENT, "");
    }

    @ExcludeUrl
    @RequestMapping("updateAssignRate")
    @ResponseBody
    public void updateAssignRate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> params = request.getParameterMap();
        JsonResult result = new JsonResult("-1", "修改失败");
        try {
            /*派单方式改为手动*/
            String relId = request.getParameter("rel_id");
            MmanLoanCollectionRule rule = new MmanLoanCollectionRule();
            rule.setId(relId);
            rule.setAssignType(OdvRate.HAND);
            mmanLoanCollectionRuleService.updateCollectionRule(rule);
            String[] ids = params.get("id");
            String[] assignRate = params.get("assignRate");
            double countSum = 0;
            for (String s : assignRate) {
                if (StringUtils.isEmpty(s)) {
                    throw new RuntimeException("修改失败!不能输入空字符!");
                }
                countSum += Double.parseDouble(s);
                if (countSum > 1.0) {
                    throw new RuntimeException("修改失败!,概率和超过1!");
                }

            }
            for (int i = 0; i < ids.length; i++) {
                OdvRate newRate = new OdvRate();
                newRate.setId(Integer.valueOf(ids[i]));
                newRate.setAssignRate(Double.valueOf(assignRate[i]));
                odvRateDao.updateByPrimaryKeySelective(newRate);
            }
            result.setMsg("修改成功!");
            result.setCode("0");
        } catch (Exception e) {
            log.error("updateAssignRate error", e);
            result.setMsg(e.getMessage());
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                DwzResult.CALLBACK_CLOSECURRENT, "");
    }

}
