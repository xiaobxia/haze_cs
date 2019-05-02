package com.info.back.annotation;

import java.lang.annotation.*;

/**
 * 操作日志
 *
 * @author cqry_2016
 * @create 2018-09-11 10:42
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperaLogAnno {
    /**
     * 操作类型
     **/
    public String operationType() default "";

    /**
     * 描述
     **/
    public String operationName() default "";

}
