package com.cxj.link.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cxj.link.annotation.LogParamsResponse;
import com.cxj.link.util.LogExceptionStackTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author cxj
 */
@Slf4j
@Order(5)
@Aspect
@Component
public class ControllerLogAop {

    /**
     * 数据源拦截的切面
     */
    public static final String POINT_STRING = "@within(com.cxj.link.annotation.LogParamsResponse) || @annotation(com.cxj.link.annotation.LogParamsResponse)";

    @Pointcut(POINT_STRING)
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant begin = new Date().toInstant();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Class<?> target = joinPoint.getTarget().getClass();
        Method declaredMethod = methodSignature.getMethod();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                args[i] = null;
            }
            if (args[i] instanceof HttpServletResponse) {
                args[i] = null;
            }
        }

        // 是否是 controller
        boolean isController = false;
        Controller controller = target.getAnnotation(Controller.class);
        RestController restController = target.getAnnotation(RestController.class);
        if ( controller != null || restController != null) {
            isController = true;
        }

        LogParamsResponse logParamsResponse = AnnotationUtils.findAnnotation(declaredMethod, LogParamsResponse.class);
        if (logParamsResponse == null) {
            logParamsResponse = target.getAnnotation(LogParamsResponse.class);
        }

        if (logParamsResponse.request()) {
            if (isController) {
                log.info("[接口调用开始] requestUrI:{},参数名:{},传参:{}"
                        , JSON.toJSONString(request.getRequestURI())
                        , JSON.toJSONString(methodSignature.getParameterNames())
                        , JSON.toJSONString(joinPoint.getArgs(), SerializerFeature.IgnoreNonFieldGetter));
            } else {
                log.info("[方法调用开始] class:{}:{},参数名:{},传参:{}"
                        , methodSignature.getDeclaringTypeName()
                        , methodSignature.getName()
                        , JSON.toJSONString(methodSignature.getParameterNames())
                        , JSON.toJSONString(joinPoint.getArgs(), SerializerFeature.IgnoreNonFieldGetter));
            }
        }

        Object proceed = joinPoint.proceed();
        String result = JSON.toJSONString(proceed);
        Instant end = new Date().toInstant();

        if (logParamsResponse.response()) {
            if (isController) {
                log.info("[接口调用结束] requestUrI:{},参数名:{},result:{},耗时:{}"
                        , JSON.toJSONString(request.getRequestURI())
                        , JSON.toJSONString(methodSignature.getParameterNames())
                        , JSON.toJSONString(result)
                        , Duration.between(begin, end).toMillis());
            } else {
                log.info("[方法调用结束] class:{}:{},参数名:{},result:{},耗时:{}"
                        , methodSignature.getDeclaringTypeName()
                        , methodSignature.getName()
                        , JSON.toJSONString(methodSignature.getParameterNames())
                        , JSON.toJSONString(result)
                        , Duration.between(begin, end).toMillis());
            }
        }

        return proceed;
    }

    /**
     * 拦截web层异常，记录异常日志
     * 目前只拦截Exception
     *
     * @param ex 异常对象
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void handleThrowing(JoinPoint pjp, Throwable ex) {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method declaredMethod = methodSignature.getMethod();
        LogParamsResponse logParamsResponse = AnnotationUtils.findAnnotation(declaredMethod, LogParamsResponse.class);

        if (logParamsResponse == null) {
            Class<?> target = pjp.getTarget().getClass();
            logParamsResponse = target.getAnnotation(LogParamsResponse.class);
        }

        if (logParamsResponse.exception()) {
            String errorMsg = String.format("[接口调用异常],class:{%s},method:{%s},e:{%s}"
                    , pjp.getSignature().getDeclaringTypeName()
                    , pjp.getSignature().getName()
                    , LogExceptionStackTrace.erroStackTrace(ex));

            log.warn(errorMsg);
        }
    }

}
