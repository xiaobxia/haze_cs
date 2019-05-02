package com.info.back.controller;

import com.info.back.result.JsonResult;
import com.info.back.service.IFengKongService;
import com.info.back.utils.DwzResult;
import com.info.back.utils.SpringUtils;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.FengKong;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("fengKong/")
public class FengKongController extends BaseController {


    @Resource
    private IFengKongService fengKongService;

    @RequestMapping("getFengKongPage")
    public String toFengKong(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
            PageConfig<FengKong> pageConfig = fengKongService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("getFengKongPage error", e);
        }
        return "mycollectionorder/fengKongList";
    }

    @RequestMapping("saveUpdateFengKong")
    public String saveUpdateFengKong(FengKong fengKong, HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            //更新页面转跳
            if ("toJsp".equals(params.get("type"))) {
                if (params.containsKey("id")) {
                    int id = Integer.valueOf(params.get("id").toString());
                    fengKong = fengKongService.getFengKongById(id);
                    model.addAttribute("fengKong", fengKong);
                }
                url = "mycollectionorder/saveUpdateFengKong";
            } else {
                //更新或者添加操作(type非toJsp)
                JsonResult result;
                if (fengKong.getId() != null) {
                    result = fengKongService.updateFengKong(fengKong);
                } else {
                    result = fengKongService.insert(fengKong);
                }
                SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            log.error("saveUpdateFengKong异常", e);
        }
        model.addAttribute(MESSAGE, erroMsg);
        model.addAttribute("params", params);
        return url;
    }
}
