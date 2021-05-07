package com.cxj.link.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.cxj.link.database.ds.MultipleDataSource;
import com.cxj.link.properties.DataBaseConfigProperties;
import com.cxj.link.properties.DataBaseDataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author cxj
 */
@Slf4j
@AutoConfigureOrder(-1)
@Configuration
@EnableConfigurationProperties(DataBaseDataSourceProperties.class)
public class DataBaseDataSourceInitConfig {

    /**
     * 默认数据源名称
     */
    public static final String TARGET_DATASOURCE = "default";

    /**
     * 数据源信息配置
     *
     * @return
     */
    @Autowired
    public DataBaseDataSourceProperties dataBaseDataSourceProperties;

    @Value("${cxj.db.dataSourcePool:DruidDataSource}")
    public String dataSourcePool;

    @Bean
    public Map<Object, Object> dataSourceCollection() {
        Map<Object, Object> datasource = new ConcurrentHashMap<>();

        dataBaseDataSourceProperties.getConfig().forEach((dbName, dbConfig) -> {

            try {
                DataSource initDataSource = getDataSource(dbConfig);
                datasource.put(dbName, initDataSource);

                if (dbConfig.isDefaultTarget()) {
                    datasource.put(TARGET_DATASOURCE, initDataSource);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        return datasource;
    }

    /**
     * 加载数据源
     *
     * @return
     */
    @Bean
    public AbstractRoutingDataSource dataSource() {
        MultipleDataSource dynamicDataSource = new MultipleDataSource();
        dynamicDataSource.setTargetDataSources(dataSourceCollection());
        Object defaultDataSource = dataSourceCollection().get(TARGET_DATASOURCE);
        if (null == defaultDataSource) {
            throw new IllegalArgumentException("请设置默认数据源[isDefaultTarget属性为true]");
        }
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

    /**
     * 根据配置获取数据源的实现
     *
     * @param data
     * @return
     * @throws SQLException
     */
    private DataSource getDataSource(DataBaseConfigProperties data) throws SQLException {

        switch (dataSourcePool) {
            case "HikariDataSource":
                return getDriverManagerHikariDataSource(data);
            case "DruidDataSource":
            default:
                return getDriverManagerDruidDataSource(data);
        }
    }

    /**
     * Hikari数据源
     *
     * @param data
     * @return
     */
    private HikariDataSource getDriverManagerHikariDataSource(DataBaseConfigProperties data) {
        if (null == data) {
            return null;
        }
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(data.getDriverClassName());
        hikariDataSource.setJdbcUrl(data.getUrl());
        hikariDataSource.setUsername(data.getUsername());
        hikariDataSource.setPassword(data.getPassword());
        hikariDataSource.setMaximumPoolSize(100);
        hikariDataSource.setMinimumIdle(10);
        hikariDataSource.setConnectionTestQuery("select 1 from dual");

        Properties dsProperties = new Properties();
        dsProperties.setProperty("cachePrepStmts", "true");
        dsProperties.setProperty("prepStmtCacheSize", "250");
        dsProperties.setProperty("prepStmtCacheSqlLimit", "2048");
        dsProperties.setProperty("useServerPrepStmts", "true");
        hikariDataSource.setDataSourceProperties(dsProperties);

        return hikariDataSource;
    }

    /**
     * druid数据源
     *
     * @param dataBaseConfigProperties
     * @return
     * @throws SQLException
     */
    private DruidDataSource getDriverManagerDruidDataSource(DataBaseConfigProperties dataBaseConfigProperties) throws SQLException {
        if (null == dataBaseConfigProperties) {
            return null;
        }

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataBaseConfigProperties.getDriverClassName());
        druidDataSource.setUrl(dataBaseConfigProperties.getUrl());
        druidDataSource.setUsername(dataBaseConfigProperties.getUsername());
        druidDataSource.setPassword(dataBaseConfigProperties.getPassword());
        druidDataSource.setMaxActive(300);
        druidDataSource.setMaxWait(60000);

        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(100);
        druidDataSource.setDefaultAutoCommit(true);
        druidDataSource.setFilters("stat");
        druidDataSource.setMinEvictableIdleTimeMillis(30000);
        druidDataSource.setRemoveAbandonedTimeout(600);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setLogAbandoned(true);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setUseUnfairLock(true);
        druidDataSource.setValidationQuery("select 1");
        List<String> connectionInitSqls = new ArrayList<>();
        connectionInitSqls.add("set names utf8mb4");
        druidDataSource.setConnectionInitSqls(connectionInitSqls);
        return druidDataSource;
    }

}
