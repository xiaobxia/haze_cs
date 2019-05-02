package com.info.back.controller;


import com.info.back.annotation.ExcludeUrl;
import com.info.back.result.JsonResult;
import com.info.back.service.TaskJobMiddleService;
import com.info.back.utils.DwzResult;
import com.info.back.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 白名单请求url
 */
@Slf4j
@Controller
@RequestMapping("white_url/")
public class WhiteReqController {


    @Resource
    private TaskJobMiddleService taskJobMiddleService;

    /**
     * 手动触发中智城黑名单提交
     */

    @ExcludeUrl
    @RequestMapping("push_zzc_black_hard")
    @ResponseBody
    public void pushZzcBlackListInHard(HttpServletResponse response) {
        log.info("hand push zzc blackList start!");
        JsonResult result = new JsonResult("-1", "失败");
        try {
            taskJobMiddleService.pushZzcBlackList();
            result.setCode("0");
            result.setMsg("成功");
            log.info("hand push zzc blackList end!");
        } catch (Exception e) {
            result.setMsg("系统异常请稍后重试!");
            log.error("zzc blacklist error!", e);
        }
        log.info("hand push zzc blackList end!");
        SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
                DwzResult.CALLBACK_CLOSECURRENT, "");
    }
}
