package com.cxj.link.database.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author cxj
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    private Logger log = LoggerFactory.getLogger(MultipleDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DataSourceContext.getCustomerType();
        if (key != null) {
            log.info("当前线程使用的数据源标识为 [{}].", key);
        }
        return key;
    }
}
