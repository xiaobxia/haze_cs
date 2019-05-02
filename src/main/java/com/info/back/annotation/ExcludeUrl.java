package com.info.back.annotation;

import java.lang.annotation.*;

/**
 * 免拦截
 *
 * @author cqry_2016
 * @create 2018-09-11 10:42
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcludeUrl {
}
