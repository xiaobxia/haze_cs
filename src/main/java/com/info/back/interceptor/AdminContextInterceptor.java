package com.info.back.interceptor;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.controller.BaseController;
import com.info.back.exception.BusinessException;
import com.info.back.service.IBackModuleService;
import com.info.back.service.IBackUserService;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：后台拦截器，包括session验证和权限信息 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-27 下午05:38:34 <br>
 */
@Slf4j
public class AdminContextInterceptor extends BaseController implements HandlerInterceptor {
    @Resource
    private IBackUserService backUserService;
    @Resource
    private IBackModuleService backModuleService;

    @SuppressWarnings("unused")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BackUser user = this.getSessionUser(request, true);
        String url = request.getRequestURI();
        Map paras = request.getParameterMap();
        Integer userId = user == null ? null : user.getId();
        if (handler instanceof HandlerMethod) {
            ExcludeUrl annotation = ((HandlerMethod) handler).getMethodAnnotation(ExcludeUrl.class);
            if (annotation != null) {
                return true;
            }
        }
        if (user == null) {
            return parseNoLogin(request, response);
        }
        String uri = getURI(request);
        // 用户的登陆密码与session不一致强制退出登陆
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("id", user.getId());
        BackUser currentUser = backUserService.findOneUser(params);
        if (!user.getUserPassword().equals(currentUser.getUserPassword())) {
            this.removeSessionUser(request);
            return parseNoLogin(request, response);
        }
        if (Constant.ADMINISTRATOR_ID.intValue() != user.getId().intValue()) {
            params.put("moduleUrl", uri);
            int count = backModuleService.findModuleByUrl(params);
//            if (count > 0) {
//                return true;
//            } else {
//                throw new BusinessException("no auth", "权限不足.");
//            }
            return true;

        } else {
            return true;
        }
    }

    private boolean parseNoLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ajaxHeadValue = "XMLHttpRequest";
        String ajaxHead = "x-requested-with";
        if (request.getHeader(ajaxHead) != null && ajaxHeadValue.equalsIgnoreCase(request.getHeader(ajaxHead))) {
            // 在响应头设置session状态
            response.setHeader("statusCode", "301");
            return false;
        }
        String loginUrl = "/back/login";
        response.sendRedirect(loginUrl);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView mav) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {

    }


    /**
     * 获得第二个路径分隔符的位置
     *
     * @throws IllegalStateException 访问路径错误，没有二(三)个'/'
     */
    private static String getURI(HttpServletRequest request) throws BusinessException {
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        String ctxPath = helper.getOriginatingContextPath(request);
        int start = 0, i = 0, count = 1;
        if (!StringUtils.isBlank(ctxPath)) {
            count++;
        }
        while (i < count && start != -1) {
            start = uri.indexOf('/', start + 1);
            i++;
        }
        if (start <= 0) {
            throw new BusinessException("uri error", "访问路径有误.");
        }
        return uri.substring(start + 1);
    }
}