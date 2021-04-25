package com.cxj.link.annotation;

import com.cxj.link.database.exchange.DataBaseDefaultShardingService;
import com.cxj.link.database.exchange.DataBaseShardingService;

import java.lang.annotation.*;


/**
 * 读写分离
 * @author cxj
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingDataSource {

    String db() default WRITE;

    Class<? extends DataBaseShardingService> exchange() default DataBaseDefaultShardingService.class;

    String WRITE = "write";
    String READ = "read";

}
