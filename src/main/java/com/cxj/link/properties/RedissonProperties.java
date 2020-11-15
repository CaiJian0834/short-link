package com.cxj.link.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

/**
 * redisson连接配置
 *
 * @author cxj
 */
@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "cxj.redisson")
public class RedissonProperties {

    /**
     * 单机版
     * 连接地址
     */
    private String host = "localhost";

    /**
     * 单机版
     * 连接端口
     */
    private int port = 6379;

    /**
     * 库号
     */
    private int database = 0;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间
     */
    private int timeout = 3000;

    /**
     * 连接池最大连接数
     */
    private int connectionPoolSize = 64;

    /**
     * 连接池空闲时间保留连接数
     */
    private int connectionMinimumIdleSize = 10;

    /**
     * 集群地址
     */
    private String clusterAddresses;

    /**
     * 集群扫描间隔毫秒
     */
    private int clusterScanInterval = 5000;

    /**
     * 哨兵地址
     */
    private String sentinelAddresses;

    /**
     * 主服务器名称
     */
    private String sentinelMasterName;

    /**
     *  Redis“从”服务器连接池大小
     */
    private int sentinelSlaveConnectionPoolSize = 250;

    /**
     *  Redis“主”服务器连接池大小
     */
    private int sentinelMasterConnectionPoolSize = 250;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    public String getClusterAddresses() {
        return clusterAddresses;
    }

    public void setClusterAddresses(String clusterAddresses) {
        this.clusterAddresses = clusterAddresses;
    }

    public int getClusterScanInterval() {
        return clusterScanInterval;
    }

    public void setClusterScanInterval(int clusterScanInterval) {
        this.clusterScanInterval = clusterScanInterval;
    }

    public String getSentinelAddresses() {
        return sentinelAddresses;
    }

    public void setSentinelAddresses(String sentinelAddresses) {
        this.sentinelAddresses = sentinelAddresses;
    }

    public String getSentinelMasterName() {
        return sentinelMasterName;
    }

    public void setSentinelMasterName(String sentinelMasterName) {
        this.sentinelMasterName = sentinelMasterName;
    }

    public int getSentinelSlaveConnectionPoolSize() {
        return sentinelSlaveConnectionPoolSize;
    }

    public void setSentinelSlaveConnectionPoolSize(int sentinelSlaveConnectionPoolSize) {
        this.sentinelSlaveConnectionPoolSize = sentinelSlaveConnectionPoolSize;
    }

    public int getSentinelMasterConnectionPoolSize() {
        return sentinelMasterConnectionPoolSize;
    }

    public void setSentinelMasterConnectionPoolSize(int sentinelMasterConnectionPoolSize) {
        this.sentinelMasterConnectionPoolSize = sentinelMasterConnectionPoolSize;
    }
}
