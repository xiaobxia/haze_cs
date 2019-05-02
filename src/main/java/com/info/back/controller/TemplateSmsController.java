package com.info.back.controller;

import com.info.back.result.JsonResult;
import com.info.back.service.ITemplateSmsService;
import com.info.back.utils.DwzResult;
import com.info.back.utils.SpringUtils;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.TemplateSms;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信模板Controller
 *
 * @author yangyaofei
 * @date 2017-02-17
 */
@Slf4j
@Controller
@RequestMapping("templateSms/")
public class TemplateSmsController extends BaseController {


    @Resource
    private ITemplateSmsService templateSmsService;

    @RequestMapping("getTemplateSmsPage")
    public String getTemplateSmsPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            PageConfig<TemplateSms> pageConfig = templateSmsService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("getTemplateSmsPage error", e);
        }
        return "templateSms/templateSmsList";
    }

    @RequestMapping("saveUpdateTemplateSms")
    public String saveUpdateTemplateSms(HttpServletRequest request, HttpServletResponse response, Model model, TemplateSms templateSms) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            //更新页面转跳
            if ("toJsp".equals(params.get("type"))) {
                if (params.containsKey("id")) {
                    templateSms = templateSmsService.getTemplateSmsById(params.get("id").toString());
                    model.addAttribute("templateSms", templateSms);
                }
                url = "templateSms/saveUpdateTemplateSms";
            } else {
                //更新或者添加操作(type非toJsp)
                JsonResult result = new JsonResult("-1", "操作失败");
                if (StringUtils.isNotBlank(templateSms.getId())) {
                    result = templateSmsService.updateTemplateSms(templateSms);
                } else {
                    result = templateSmsService.insert(templateSms);
                }
                SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            if (url == null) {
                erroMsg = "服务器异常，请稍后重试！";
                if (e.getLocalizedMessage().indexOf("UK_user_account") >= 0) {
                    erroMsg = "用户名重复！";
                }
                SpringUtils.renderDwzResult(response, false, "操作失败,原因："
                        + erroMsg, DwzResult.CALLBACK_CLOSECURRENT, params.get(
                        "parentId").toString());
            }
            log.error("添加权限信息失败，异常信息:", e);
        }
        model.addAttribute(MESSAGE, erroMsg);
        model.addAttribute("params", params);
        return url;
    }

    @RequestMapping("deleteTemplateSms")
    public void deleteTemplateSms(HttpServletRequest request, HttpServletResponse response, Model model) {

        Map<String, String> params = this.getParameters(request);
        JsonResult result = new JsonResult("-1", "删除短信模板失败");
        try {
            String id = params.get("id") == null ? "" : params.get("id");
            if (StringUtils.isNotBlank(id)) {
                result = templateSmsService.deleteTemplateSms(id);
            } else {
                result.setMsg("参数错误");
            }
        } catch (Exception e) {
            log.error("deleteTemplateSms error", e);
        }
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get("parentId").toString());
    }
}
