package com.info.web.util;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BussinessLogUtil {

    /**
     * @param msg
     * @param module 模块名
     * @param e
     */
    public static void error(String msg, String module, Exception e) {
//        log.error(module + " " + msg + " " + e.getClass().toString() + " message:" + e.getMessage());
        log.error(module + " " + msg + " " + e.getClass().toString());
    }

    /**
     * @param msg
     * @param module 模块名
     */
    public static void warn(String msg, String module) {
        log.warn(module + " " + msg);
    }

    /**
     * @param msg
     * @param module 模块名
     */
    public static void info(String msg, String module) {
        log.info(module + " " + msg);
    }

}
