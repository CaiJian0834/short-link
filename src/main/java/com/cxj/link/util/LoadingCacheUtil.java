package com.cxj.link.util;

import com.cxj.link.constant.NumberConstant;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存工具类
 * guava实现
 */
@Slf4j
public class LoadingCacheUtil {

    private static LoadingCache<String, String> loadingCache;


    static {
        loadingCache = CacheBuilder.newBuilder()
                .expireAfterAccess(NumberConstant.REDIS_EXPIRE_MINUTE, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        return null;
                    }
                });
    }

    /**
     * 获取本地缓存
     * @param key
     * @return
     */
    public static String get(String key) {
        try {
            return loadingCache.get(key);
        } catch (CacheLoader.InvalidCacheLoadException e) {
            return null;
        } catch (Exception e) {
            log.warn("查询本地缓存异常，key={},e={}", key, LogExceptionStackTrace.erroStackTrace(e));
            return null;
        }
    }

    /**
     * 设置本地缓存
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        try {
            loadingCache.put(key, value);
        } catch (Exception e) {
            log.warn("设置本地缓存异常，key={},value={}", key, value);
        }
    }

}
