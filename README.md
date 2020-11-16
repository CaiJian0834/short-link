# 高性能分布式短链接服务



## 功能实现计划


| 所用技术     | 功能            | 完成度 |
|:------------|:---------------|:------|
| Spring Boot | 框架            | 已完成 |
| MySQL       | 数据存储        | 已完成 |
| Redis       | 二级缓存        | 已完成 |
| OpenResty   | 代理 + 一级缓存  | TODO |
| Guava | 一级缓存      | 已完成 |
| Redisson | 分布式锁        | 已完成   |
| Redis | 布隆过滤器，判断hash是否存在 | 已完成 |



## 快速开始

数据库初始化：

```mysql
CREATE TABLE `shortlink_url_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hash_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'hash码',
  `hash_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '短链接',
  `really_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原url',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '是否有效 1 有效 0 无效',
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
  `end_time_number` int NOT NULL DEFAULT '0' COMMENT '失效时间 单位：分钟',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `hash_value` (`hash_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短链接生成记录';
```

配置文件说明：

```properties
server.port=9080
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8

# 短链接域名
cxj.short.link.host=http://localhost:9080/
# 短链接 protocol
cxj.short.link.protocol=http
# hash冲突时的重试次数
cxj.short.link.retry.number=5
#是否使用redis布隆过滤器
cxj.short.link.redis.bloom=false

#db 读写分离配置
#写库
cxj.db.datasource.config.write.defaultTarget=true
cxj.db.datasource.config.write.url=jdbc:mysql://192.168.1.11:3306/short_link?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
cxj.db.datasource.config.write.username=root
cxj.db.datasource.config.write.password=root
cxj.db.datasource.config.write.driverClassName=com.mysql.cj.jdbc.Driver
#读库
cxj.db.datasource.config.read.defaultTarget=false
cxj.db.datasource.config.read.url=jdbc:mysql://192.168.1.11:3306/short_link?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
cxj.db.datasource.config.read.username=root
cxj.db.datasource.config.read.password=root
cxj.db.datasource.config.read.driverClassName=com.mysql.cj.jdbc.Driver

#redis 配置
#单机版
cxj.redisson.host=192.168.1.11
cxj.redisson.port=6379
cxj.redisson.database=0
#cxj.redisson.password=root

```

项目打包

```shell
mvn -U clean package -Dmaven.test.skip=true -Dautoconfig.skip -Ptesting 
```

启动项目

```
java -jar short-link.jar
```



## 系统设计

生成短链接



短链接访问



hash码查询原链接



访问统计



## 联系

QQ群：324491267

电子邮箱：735374036@qq.com



