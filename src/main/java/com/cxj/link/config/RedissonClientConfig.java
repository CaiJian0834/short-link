package com.cxj.link.config;

import com.cxj.link.properties.RedissonProperties;
import com.cxj.link.service.impl.RedissonRedisService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author cxj
 */
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonClientConfig {

    @Autowired
    private RedissonProperties redissonProperties;

    /**
     * 单机模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(name = "cxj.redisson.host")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig
                .setDatabase(redissonProperties.getDatabase())
                .setTimeout(redissonProperties.getTimeout())
                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize())
                .setAddress("redis://" + redissonProperties.getHost() + ":" + redissonProperties.getPort());

        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            singleServerConfig.setPassword(redissonProperties.getPassword());
        }

        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }

    /**
     * 集群模式自动装配
     */
    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(name = "cxj.redisson.cluster-addresses")
    public RedissonClient redissonCluster() {

        String[] nodes = Arrays.stream(redissonProperties.getClusterAddresses().split(",")).map(r -> "redis://" + r).toArray(String[]::new);

        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .setScanInterval(redissonProperties.getClusterScanInterval())
                .setTimeout(redissonProperties.getTimeout())
                .addNodeAddress(nodes);

        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            clusterServersConfig.setPassword(redissonProperties.getPassword());
        }

        return Redisson.create(config);
    }

    /**
     * 哨兵模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(name = "cxj.redisson.sentinel-addresses")
    public RedissonClient redissonSentinel() {

        String[] nodes = Arrays.stream(redissonProperties.getSentinelAddresses().split(",")).map(r -> "redis://" + r).toArray(String[]::new);

        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(nodes)
                .setMasterName(redissonProperties.getSentinelMasterName())
                .setTimeout(redissonProperties.getTimeout())
                .setDatabase(redissonProperties.getDatabase())
                .setMasterConnectionPoolSize(redissonProperties.getSentinelMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redissonProperties.getSentinelSlaveConnectionPoolSize());

        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedissonRedisService redissonRedisService(RedissonClient redissonClient){
        return new RedissonRedisService(redissonClient);
    }



}
