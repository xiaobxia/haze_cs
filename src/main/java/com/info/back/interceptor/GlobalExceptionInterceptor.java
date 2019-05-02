package com.info.back.interceptor;

import com.alibaba.fastjson.JSON;
import com.info.back.exception.BusinessException;
import com.info.back.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常拦截
 *
 * @author cqry_2016
 * @create 2018-08-23 18:49
 **/

@Slf4j
public class GlobalExceptionInterceptor implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        log.error(e.getMessage(), e);
        ModelAndView mv = new ModelAndView("error");
        if (o == null || o instanceof ResourceHttpRequestHandler) {
            return mv;
        }
        HandlerMethod handler = (HandlerMethod) o;
        ResponseBody ajax = handler.getMethod().getAnnotation(ResponseBody.class);
        if (ajax != null) {
            return paseAjaxEx(response, e);
        } else {
            if (e instanceof BusinessException) {
                mv.addObject("code", ((BusinessException) e).getCode());
                mv.addObject("msg", e.getMessage());
            } else {
                mv.addObject("code", 500);
                mv.addObject("msg", "系统出错了.");
            }
            return mv;
        }


    }

    private ModelAndView paseAjaxEx(HttpServletResponse response, Exception e) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter writer = response.getWriter()) {
            JsonResult resp = new JsonResult();
            if (e instanceof BusinessException) {
                resp.setCode(((BusinessException) e).getCode());
                resp.setMsg(e.getMessage());
            } else {
                resp.setCode("fail");
                resp.setMsg("系统出错了.");
            }
            writer.write(JSON.toJSONString(resp));
            writer.flush();
        } catch (Exception e1) {
            log.error("GlobalExceptionInterceptor paseAjaxEx error.: {}", e1);
        }
        return null;
    }

    /**
     * 获取最原始的异常出处，即最初抛出异常的地方
     */
    private Throwable deepestException(Throwable e) {
        Throwable tmp = e;
        int breakPoint = 0;
        while (tmp.getCause() != null) {
            if (tmp.equals(tmp.getCause())) {
                break;
            }
            tmp = tmp.getCause();
            breakPoint++;
            if (breakPoint > 1000) {
                break;
            }
        }
        return tmp;
    }
}
