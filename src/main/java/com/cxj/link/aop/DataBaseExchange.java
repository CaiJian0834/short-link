package com.cxj.link.aop;

import com.cxj.link.annotation.ShardingDataSource;
import com.cxj.link.database.ds.DataSourceContext;
import com.cxj.link.database.exchange.DataBaseShardingService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author cxj
 */
@Order(-1)
@Aspect
@Component
public class DataBaseExchange {

    /**
     * 数据源拦截的切面
     */
    public static final String POINT_STRING = "@within(com.cxj.link.annotation.ShardingDataSource) || @annotation(com.cxj.link.annotation.ShardingDataSource)";

    @Pointcut(value = POINT_STRING)
    public void point() { // default implementation ignored
    }

    @After(value = "point()")
    public void after(JoinPoint joinPoint) {
        DataSourceContext.clearCustomerType();
    }

    @Before(value = "point()")
    public void before(JoinPoint joinPoint) throws InstantiationException, IllegalAccessException {
        MethodSignature msj = ((MethodSignature) joinPoint.getSignature());
        Method declaredMethod = msj.getMethod();

        ShardingDataSource datasource = AnnotationUtils.findAnnotation(declaredMethod, ShardingDataSource.class);

        if (datasource == null) {
            Class<?> target = joinPoint.getTarget().getClass();
            datasource = target.getAnnotation(ShardingDataSource.class);
        }

        String shard = "";

        Class<? extends DataBaseShardingService> exchange = datasource.exchange();
        DataBaseShardingService shardingService = exchange.newInstance();
        shard = shardingService.exchange(joinPoint.getArgs());

        if (StringUtils.isEmpty(shard)) {
            shard = datasource.db();
        }

        DataSourceContext.setCustomerType(shard);
    }

}
