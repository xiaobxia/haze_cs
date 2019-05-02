package com.info.back.controller;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.exception.BusinessException;
import com.info.back.service.IBackUserService;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 类描述：后台登录类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 下午02:57:46 <br>
 */
@Slf4j
@Controller
public class LoginController extends BaseController {
    private static final String PROCESS_URL = "processUrl";
    private static final String RETURN_URL = "returnUrl";

    @Resource
    private IBackUserService backUserService;

    /**
     * 获得地址
     */
    private String getView(String processUrl, String returnUrl) {
        if (!StringUtils.isBlank(processUrl)) {
            StringBuilder sb = new StringBuilder("redirect:");
            sb.append(processUrl);
            if (!StringUtils.isBlank(returnUrl)) {
                sb.append("?").append(RETURN_URL).append("=").append(returnUrl);
            }
            return sb.toString();
        } else if (!StringUtils.isBlank(returnUrl)) {
            return "redirect:" + returnUrl;
        } else {
            return null;
        }
    }

    @ExcludeUrl
    @ResponseBody
    @RequestMapping("heartBeat")
    public String testAlive() {
        return "are you ok?";
    }

    @ExcludeUrl
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, Model model) {
        try {
            String processUrl = request.getParameter(PROCESS_URL);
            String returnUrl = request.getParameter(RETURN_URL);
            String message = request.getParameter(MESSAGE);
            BackUser backUser = getSessionUser(request);
            if (backUser != null) {
                String view = getView(processUrl, returnUrl);
                if (view != null) {
                    return view;
                } else {
                    model.addAttribute("seatExt", backUser.getSeatExt());
                    return "redirect:/";
                }
            }
            if (!StringUtils.isBlank(processUrl)) {
                model.addAttribute(PROCESS_URL, processUrl);
            }
            if (!StringUtils.isBlank(returnUrl)) {
                model.addAttribute(RETURN_URL, returnUrl);
            }
            if (!StringUtils.isBlank(message)) {
                model.addAttribute(MESSAGE, message);
            }
        } catch (Exception e) {
            log.error("back login error:{}", e);
        }
        return "login";
    }

    @ExcludeUrl
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String submit(HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = null;
        BackUser backUser;
        try {
            params = this.getParametersO(request);
            String password = String.valueOf(params.get("userPassword"));
            if (!validateSubmit(request)) {
                throw new BusinessException("verification code error.", "验证码错误.");
            }
            backUser = backUserService.findOneUser(params);
            if (backUser == null) {
                throw new BusinessException("post username error.", "该用户不存在.");
            }
            if (!backUser.getUserPassword().equals(Md5coding.getInstance().code(password))) {
                throw new BusinessException("password error.", "密码错误.");
            }
            if (backUser.getUserStatus().equals(BackUser.STATUS_DELETE)) {
                throw new BusinessException("account deleted.", "账号已被删除.");
            }
        } catch (BusinessException e) {
            log.info("post login business error code: {}, msg：{}, params: {}", e.getCode(), e.getMessage(), params);
            model.addAttribute(MESSAGE, e.getMessage());
            return "login";
        } catch (Exception e) {
            String errMsg = "服务器异常，稍后重试！";
            model.addAttribute(MESSAGE, errMsg);
            log.error("post login system error params:{}", params, e);
            return "login";
        }
        // 保存session
        setSessionUser(response, backUser);
        return "redirect:/";
    }

    public static void main(String[] args) {
        System.out.println(Md5coding.getInstance().code("zhanghj811"));
    }

    @ExcludeUrl
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        removeSessionUser(request);
        return "redirect:login";
    }
}