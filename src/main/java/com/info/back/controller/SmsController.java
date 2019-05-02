package com.info.back.controller;

import com.info.back.service.ISmsService;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.InfoSms;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 短信模板Controller
 *
 * @author yangyaofei
 * @date 2017-02-17
 */
@Slf4j
@Controller
@RequestMapping("infoMsg/")
public class SmsController extends BaseController {


    @Resource
    private ISmsService smsService;

    @RequestMapping("findList")
    public String getSmsPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            PageConfig<InfoSms> pageConfig = smsService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("getSmsPage error", e);
        }
        return "infoSms/infoSmsList";
    }
}
