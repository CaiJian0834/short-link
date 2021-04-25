package com.cxj.link.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库
 * 配置
 *
 * @author cxj
 * @date 2020/8/17
 */
@ConfigurationProperties(prefix = "cxj.db.datasource")
public class DataBaseDataSourceProperties {

    private Map<String, DataBaseConfigProperties> config = new HashMap<>();

    public Map<String, DataBaseConfigProperties> getConfig() {
        return config;
    }

    public void setConfig(Map<String, DataBaseConfigProperties> config) {
        this.config = config;
    }


}
