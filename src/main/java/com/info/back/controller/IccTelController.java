/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IccTelController
 * Author:   Liubing
 * Date:     2018/3/9 10:22
 * Description: 呼叫中心
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.controller;

import com.info.web.pojo.cspojo.BackUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈呼叫中心〉
 *
 * @author Liubing
 * @create 2018/3/9
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("iccTel/")
public class IccTelController extends BaseController {

    /**
     * 打开通话界面
     *
     * @return
     */
    @RequestMapping("dail")
    public String dail(String phoneNum, Model model, HttpServletRequest request) {
        BackUser backUser = loginAdminUser(request);
        log.debug("跳转到通话界面");
        model.addAttribute("phoneNumber", phoneNum);
        model.addAttribute("seatExt", backUser.getSeatExt());
        return "iccTel/dail";
    }
}
