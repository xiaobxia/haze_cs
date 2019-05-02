package com.info.back.aspect;

import com.info.back.annotation.OperaLogAnno;
import com.info.back.controller.BaseController;
import com.info.back.dao.OperaLogDao;
import com.info.back.utils.RequestUtils;
import com.info.back.vo.OperaLog;
import com.info.web.pojo.cspojo.BackUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 用户操作日志
 *
 * @author cqry_2016
 * @create 2018-09-11 11:43
 **/

@Slf4j
@Aspect
@Component
public class OperaLogAspect extends BaseController {

    @Resource
    private OperaLogDao logDao;

    @Pointcut("execution (* com.info.back.controller.*.*(..))")
    public void controllerAspect() {
    }

    private OperaLog getLog(JoinPoint joinPoint) throws Exception {
        OperaLog log = new OperaLog();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        BackUser user = this.getSessionUser(request, true);
        if (user == null) {
            return null;
        }
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        String params = null;
        int maxLen = 200;
        if (arguments != null && arguments.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object argument : arguments) {
                if (StringUtils.isNotEmpty(sb.toString())) {
                    sb.append(";");
                }
                sb.append(argument);
            }
            params = sb.toString();
            if (params.length() > maxLen) {
                params = params.substring(0, maxLen);
            }
        }
        Class targetClass;
        targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String operationName = "";
        String operationType = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs != null && clazzs.length == arguments.length && method.getAnnotation(OperaLogAnno.class) != null) {
                    operationName = method.getAnnotation(OperaLogAnno.class).operationName();
                    operationType = method.getAnnotation(OperaLogAnno.class).operationType();
                    break;
                }
            }
        }
        if ("".equals(operationName)) {
            return null;
        }
        log.setLogType(1);
        log.setDescription(operationName);
        log.setMethod(operationName);
        log.setIpAddr(RequestUtils.getIpAddr(request));
        log.setOperaUser(user.getUserName());
        log.setParams(params);
        log.setType(operationType);
        log.setCreateAt(new Date());
        return log;
    }

    @After("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            OperaLog log = getLog(joinPoint);
            if (log != null) {
                logDao.insertSelective(log);
            }
        } catch (Exception e) {
            log.error("OperaLogAspect after error: {}", e);
        }
    }


    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            OperaLog log = getLog(joinPoint);
            if (log != null) {
                log.setErrorCode(e.getClass().getName());
                log.setErrorMsg(e.getMessage());
                logDao.insertSelective(log);
            }
        } catch (Exception e1) {
            log.error("OperaLogAspect doAfterThrowing error: {}", e1);
        }
    }


}
