package com.info.back.controller;

import com.google.gson.Gson;
import com.info.back.utils.RequestUtils;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.pojo.cspojo.User;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class BaseController {
    public static final String MESSAGE = "message";

    @Resource
    JedisCluster jedisCluster;


    /**
     * 获取IP地址
     */
    public String getIpAddr(HttpServletRequest request) {
        return RequestUtils.getIpAddr(request);
    }

    /**
     * 得到session中的admin user对象
     */
    public BackUser loginAdminUser(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        return this.getSessionUser(request);
    }


    public BackUser getSessionUser(HttpServletRequest request) {
        BackUser backUser;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (Constant.SESSION_ID.equalsIgnoreCase(c.getName())) {
                    String sessionId = c.getValue();
                    try {
                        String userJson = jedisCluster.get(sessionId);
                        Gson gson = new Gson();
                        backUser = gson.fromJson(userJson, BackUser.class);
                        return backUser;
                    } catch (Exception e) {
                        log.error(" loadAdminUser is error ", e);
                        return null;
                    }
                }
            }
        }
        return null;
    }

    protected void setSessionUser(HttpServletResponse response, BackUser backUser) {
        Gson gson = new Gson();
        String userJson = gson.toJson(backUser);
        String cookieValue = Md5coding.md5(userJson + System.currentTimeMillis());
        Cookie sessionCookie = new Cookie(Constant.SESSION_ID, cookieValue);
        response.addCookie(sessionCookie);
        jedisCluster.setex(cookieValue, Constant.SESSION_EXPIRE_SECOND, userJson);

    }

    /**
     * 得到session中的admin user对象
     */
    public User loginFrontUser(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        return (User) request.getSession().getAttribute(
                Constant.FRONT_USER);
    }

    /**
     * 验证码
     */
    public boolean validateSubmit(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            Cookie cookie = null;
            String value = "";
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (Constant.JCAPTCHA_CODE.equalsIgnoreCase(c.getName())) {
                        cookie = c;
                    }
                }
            }
            if (cookie != null) {
                String key = cookie.getValue();
                value = jedisCluster.get(key);
                jedisCluster.del(key);
            }
            return request.getParameter("captcha").toLowerCase().equals(value);
        } catch (Exception e) {
            log.error("validateSubmit error:{}", e);
            return false;
        }

    }

    /**
     * 获得request中的参数
     *
     * @return string object类型的map
     */
    public HashMap<String, Object> getParametersO(HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {
            Map<String, Object> p = new HashMap<>();
            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    if (((Object[]) value).length > 1) {
                        v = value;
                    } else {
                        v = ((Object[]) value)[0];
                    }
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String))) {
                    String s = ((String) v).trim();
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            hashMap.putAll(p);
            // 读取cookie
            hashMap.putAll(readcookiemap(request));
        }
        return hashMap;
    }

    /**
     * 得到页面传递的参数封装成map
     */

    public Map<String, String> getParameters(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        Map<String, String> p = new HashMap<>();
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {

            Collection keys = req.keySet();
            for (Object key1 : keys) {
                String key = (String) key1;
                Object value = req.get(key);
                Object v;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    v = ((Object[]) value)[0];
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = (String) v;
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            //读取cookie
            p.putAll(readcookiemap(request));
            return p;
        }
        return p;
    }

    /**
     * 将cookie封装到Map里面
     */
    private static Map<String, String> readcookiemap(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }

    public void removeSessionUser(HttpServletRequest request) {
        Cookie sessionCookie = this.getSessionCookie(request);
        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            jedisCluster.del(sessionId);
        }
    }

    private Cookie getSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie c : cookies) {
            String cName = c.getName();
            if (Constant.SESSION_ID.equalsIgnoreCase(cName)) {
                return c;
            }
        }
        return null;
    }

    public BackUser getSessionUser(HttpServletRequest request, boolean expire) {
        BackUser backUser = null;
        Cookie sessionCookie = this.getSessionCookie(request);
        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            String backUserJson = jedisCluster.get(sessionId);
            if (StringUtils.isEmpty(backUserJson)) {
                return null;
            }
            Gson gson = new Gson();
            backUser = gson.fromJson(backUserJson, BackUser.class);
            jedisCluster.expire(sessionId, Constant.SESSION_EXPIRE_SECOND);
        }
        return backUser;
    }
}