package com.cxj.link.service.impl;

import com.cxj.link.service.RedisService;
import com.google.common.collect.Lists;
import org.redisson.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author cxj
 * @emall 735374036@qq.com
 */
public class RedissonRedisService implements RedisService {

    private RedissonClient redissonClient;

    public RedissonRedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void set(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    @Override
    public void setex(String key, String value, Long expire, TimeUnit timeUnit) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, expire, timeUnit);
    }

    @Override
    public boolean setnx(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.trySet(value);
    }

    @Override
    public boolean setnx(String key, String value, Long time, TimeUnit timeUnit) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.trySet(value, time, timeUnit);
    }

    @Override
    public String get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public Long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

    @Override
    public Long incrBy(String key, Long value) {
        return redissonClient.getAtomicLong(key).addAndGet(value);
    }

    @Override
    public Long decr(String key) {
        return redissonClient.getAtomicLong(key).decrementAndGet();
    }

    @Override
    public boolean del(String key) {
        return redissonClient.getBucket(key).delete();
    }

    @Override
    public boolean exists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    @Override
    public boolean expire(String key, Long value, TimeUnit timeUnit) {
        return redissonClient.getBucket(key).expire(value, timeUnit);
    }

    /**
     * 返回key剩余时间
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1
     *
     * @param key
     * @return 毫秒
     */
    @Override
    public Long pttl(String key) {
        return redissonClient.getBucket(key).remainTimeToLive();
    }

    @Override
    public void hset(String key, String innerKey, String value) {
        RMap<String, String> map = redissonClient.getMap(key);
        map.fastPut(innerKey, value);
    }

    /**
     * 不保证原子性
     *
     * @param key
     * @param fv
     */
    @Override
    public void hsetAll(String key, Map<String, String> fv) {
        RMap<String, String> map = redissonClient.getMap(key);
        for (Map.Entry<String, String> kv : fv.entrySet()) {
            map.put(kv.getKey(), kv.getValue());
        }
    }

    @Override
    public String hget(String key, String innerKey) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.get(innerKey);
    }

    @Override
    public Map<String, String> hgetall(String key) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.readAllMap();
    }

    @Override
    public Long hincr(String key, String innerKey) {
        return hincrBy(key, innerKey, 1L);
    }

    @Override
    public Long hincrBy(String key, String innerKey, Long value) {
        RMap<String, Long> map = redissonClient.getMap(key);
        return map.addAndGet(innerKey, value);
    }

    @Override
    public boolean hdel(String key, String innerKey) {
        RMap<String, String> map = redissonClient.getMap(key);
        Long sum = map.fastRemove(key);
        return sum == 1 ? true : false;
    }

    @Override
    public Long hdelAll(String key, Object... fs) {
        RMap<Object, Object> map = redissonClient.getMap(key);
        return map.fastRemove(fs);
    }

    @Override
    public Long hdelAll(String key, List<Object> fs) {
        return hdelAll(key, fs.toArray());
    }

    @Override
    public boolean hexists(String key, String innerKey) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.containsKey(innerKey);
    }

    @Override
    public String spop(String key) {
        RSet<String> set = redissonClient.getSet(key);
        return set.removeRandom();
    }

    @Override
    public Set<String> spop(String key, Integer amount) {
        RSet<String> set = redissonClient.getSet(key);
        return set.removeRandom(amount);
    }

    @Override
    public Integer scard(String key) {
        RSet<String> map = redissonClient.getSet(key);
        return map.size();
    }

    @Override
    public boolean sadd(String key, String... values) {
        return sadd(key, Lists.newArrayList(values));
    }

    @Override
    public boolean sadd(String key, List<String> values) {
        RSet<String> set = redissonClient.getSet(key);
        return set.addAll(values);
    }

    @Override
    public Boolean srem(String key, String... values) {
        return srem(key, Lists.newArrayList(values));
    }

    @Override
    public Boolean srem(String key, List<String> values) {
        RSet<String> set = redissonClient.getSet(key);
        return set.removeAll(values);
    }

    @Override
    public Boolean sismember(String key, Object object) {
        RSet<String> set = redissonClient.getSet(key);
        return set.contains(object);
    }

    @Override
    public Set<String> smembers(String key) {
        RSet<String> set = redissonClient.getSet(key);
        return set.readAll();
    }

    @Override
    public boolean zadd(String key, String innerKey, Double scores) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        return scoredSortedSet.add(scores, innerKey);
    }

    @Override
    public Collection<String> zrevrange(String key, Integer begin, Integer end) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        Collection<String> values = scoredSortedSet.valueRangeReversed(begin, end);
        return new HashSet<>(values);
    }

    @Override
    public Set<String> zrange(String key, Integer begin, Integer end) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        Collection<String> values = scoredSortedSet.valueRange(begin, end);
        return new HashSet<>(values);
    }

    @Override
    public Integer zcard(String key) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        return scoredSortedSet.size();
    }

    @Override
    public Integer zrank(String key, String innerKey) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        return scoredSortedSet.rank(innerKey);
    }

    @Override
    public Integer zrevrank(String key, String innerKey) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        return scoredSortedSet.revRank(innerKey);
    }

    @Override
    public Double zincrBy(String key, String innerKey, Double delta) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        return scoredSortedSet.addScore(innerKey, delta);
    }

    @Override
    public String rpop(String key) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.pollLast();
    }

    @Override
    public void rpush(String key, String value) {
        RDeque<String> deque = redissonClient.getDeque(key);
        deque.addLast(value);
    }

    @Override
    public Integer rpushx(String key, String... value) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.addLastIfExists(value);
    }

    @Override
    public Integer rpushx(String key, List<String> value) {
        return rpushx(key, (String[]) value.toArray());
    }

    @Override
    public String lpop(String key) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.pollFirst();
    }

    @Override
    public void lpush(String key, String value) {
        RDeque<String> deque = redissonClient.getDeque(key);
        deque.addFirst(value);
    }


    @Override
    public Integer lpushx(String key, List<String> value) {
        return lpushx(key, (String[]) value.toArray());
    }

    @Override
    public Integer lpushx(String key, String... values) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.addFirstIfExists(values);
    }

    @Override
    public Integer llenth(String key) {
        return redissonClient.getList(key).size();
    }

    @Override
    public List<String> lrange(String key, Long start, Long end) {
        RList<String> list = redissonClient.getList(key);
        return list.readAll();
    }

    @Override
    public boolean pexpire(String key, Long milliseconds, TimeUnit timeUnit) {
        RPermitExpirableSemaphore permitExpirableSemaphore = redissonClient.getPermitExpirableSemaphore(key);
        return permitExpirableSemaphore.expire(milliseconds, timeUnit);
    }

    @Override
    public Long geoAdd(String key, Double longitude, Double latitude, String innerKey) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.add(longitude, latitude, innerKey);
    }

    @Override
    public Map<String, GeoPosition> geoPos(String key, String... innerKeys) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.pos(innerKeys);
    }

    @Override
    public Map<String, GeoPosition> geoPos(String key, List<String> innerKey) {
        return geoPos(key, (String[]) innerKey.toArray());
    }

    @Override
    public Double geoDist(String key, String firstMember, String secondMember, GeoUnit geoUnit) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.dist(firstMember, secondMember, geoUnit);
    }

    @Override
    public List<String> geoRadius(String key, Double longitude, Double latitude, Double radius, GeoUnit geoUnit) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.radius(longitude, latitude, radius, geoUnit);
    }

    @Override
    public List<String> geoRadiusByMember(String key, String innerKey, Double radius, GeoUnit geoUnit) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.radius(innerKey, radius, geoUnit);
    }

    @Override
    public RLock getLock(String key){
        return redissonClient.getLock(key);
    }


}
