package com.cxj.link.annotation;

import java.lang.annotation.*;


/**
 * 接口入参，出参日志
 * @author cxj
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogParamsResponse {

    /** 入参日志 */
    boolean request() default true;
    /** 出参日志 */
    boolean response() default true;
    /** 异常日志 */
    boolean exception() default true;

}
