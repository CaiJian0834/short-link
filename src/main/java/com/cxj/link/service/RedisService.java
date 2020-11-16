package com.cxj.link.service;

import org.redisson.api.GeoPosition;
import org.redisson.api.GeoUnit;
import org.redisson.api.RLock;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis命令封装接口
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
public interface RedisService {

    void set(String key, String value);

    void setex(String key, String value, Long expire, TimeUnit timeUnit);

    boolean setnx(String key, String value);

    boolean setnx(String key, String value, Long time, TimeUnit timeUnit);

    String get(String key);

    Long incr(String key);

    Long incrBy(String key, Long value);

    Long decr(String key);

    boolean del(String key);

    boolean exists(String key);

    boolean expire(String key, Long value, TimeUnit timeUnit);

    Long pttl(String key);

    void hset(String key, String innerKey, String value);

    void hsetAll(String key, Map<String, String> fv);

    String hget(String key, String innerKey);

    Map<String, String> hgetall(String key);

    Long hincr(String key, String innerKey);

    Long hincrBy(String key, String innerKey, Long value);

    boolean hdel(String key, String innerKey);

    Long hdelAll(String key, Object... fs);

    Long hdelAll(String key, List<Object> fs);

    boolean hexists(String key, String innerKey);

    String spop(String key);

    Set<String> spop(String key, Integer amount);

    Integer scard(String key);

    boolean sadd(String key, String... values);

    boolean sadd(String key, List<String> values);

    Boolean srem(String key, String... values);

    Boolean srem(String key, List<String> values);

    Boolean sismember(String key, Object object);

    Set<String> smembers(String key);

    boolean zadd(String key, String innerKey, Double scores);

    Collection<String> zrevrange(String key, Integer begin, Integer end);

    Set<String> zrange(String key, Integer begin, Integer end);

    Integer zcard(String key);

    Integer zrank(String key, String innerKey);

    Integer zrevrank(String key, String innerKey);

    Double zincrBy(String key, String innerKey, Double delta);

    void rpush(String key, String value);

    Integer rpushx(String key, String... value);

    Integer rpushx(String key, List<String> value);

    String lpop(String key);

    void lpush(String key, String value);

    Integer lpushx(String key, List<String> value);

    Integer lpushx(String key, String... values);

    String rpop(String key);

    Integer llenth(String key);

    List<String> lrange(String key, Long start, Long end);

    boolean pexpire(String key, Long milliseconds, TimeUnit timeUnit);

    Long geoAdd(String key, Double longitude, Double latitude, String innerKey);

    Map<String, GeoPosition> geoPos(String key, String... innerKeys);

    Map<String, GeoPosition> geoPos(String key, List<String> innerKeys);

    Double geoDist(String key, String firstMember, String secondMember, GeoUnit geoUnit);

    List<String> geoRadius(String key, Double longitude, Double latitude, Double radius, GeoUnit geoUnit);

    List<String> geoRadiusByMember(String key, String innerKey, Double radius, GeoUnit geoUnit);

    RLock getLock(String key);

    boolean bfAdd(String key, String value);

    boolean bfExists(String key, String value);

}
