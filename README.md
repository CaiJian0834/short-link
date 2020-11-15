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
| Redis | 布隆过滤器，判断hash是否存在 | TODO |



## 系统设计



## 快速开始

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
mvn clean package -Dmaven.test.skip=true 
```

启动项目

```
java -jar short-link.jar
```



## 贡献





## 联系

QQ群：324491267

电子邮箱：735374036@qq.com



