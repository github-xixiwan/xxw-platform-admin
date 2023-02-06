package com.xxw.platform.plugin.cache.sdk.redis;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
/**
 * 基于redis的缓存封装，hash结构
 *
 * @author liaoxiting
 * @date 2020/7/9 10:09
 */
@SuppressWarnings("all")
public abstract class AbstractRedisHashCacheOperator<T> implements CacheOperatorApi<T> {

    private final RedisTemplate<String, T> redisTemplate;

    public AbstractRedisHashCacheOperator(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, T value) {
        redisTemplate.boundHashOps(getCommonKeyPrefix()).put(key, value);
    }

    @Override
    public void put(String key, T value, Long timeoutSeconds) {
        // 不能设置单个的过期时间
        this.put(key, value);
    }

    @Override
    public T get(String key) {
        return (T) redisTemplate.boundHashOps(getCommonKeyPrefix()).get(key);
    }

    @Override
    public void remove(String... key) {
        redisTemplate.boundHashOps(getCommonKeyPrefix()).delete(key);
    }

    @Override
    public void expire(String key, Long expiredSeconds) {
        // 设置整个hash的
        redisTemplate.boundHashOps(getCommonKeyPrefix()).expire(expiredSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean contains(String key) {
        return redisTemplate.boundHashOps(getCommonKeyPrefix()).hasKey(key);
    }

    @Override
    public Collection<String> getAllKeys() {
        Set<Object> keys = redisTemplate.boundHashOps(getCommonKeyPrefix()).keys();
        if (keys != null) {
            // 去掉缓存key的common prefix前缀
            return keys.stream().map(Object::toString).collect(Collectors.toSet());
        } else {
            return CollectionUtil.newHashSet();
        }
    }

    @Override
    public Collection<T> getAllValues() {
        Collection<String> allKeys = getAllKeys();
        if (allKeys != null) {
            return (Collection<T>) redisTemplate.boundHashOps(getCommonKeyPrefix()).multiGet(Collections.singleton(allKeys));
        } else {
            return CollectionUtil.newArrayList();
        }
    }

    @Override
    public Map<String, T> getAllKeyValues() {
        Collection<String> allKeys = this.getAllKeys();
        HashMap<String, T> results = MapUtil.newHashMap();
        for (String key : allKeys) {
            results.put(key, this.get(key));
        }
        return results;
    }

    /**
     * 获取RedisTemplate
     *
     * @author liaoxiting
     * @date 2021/2/8 9:40
     */
    public RedisTemplate<String, T> getRedisTemplate() {
        return this.redisTemplate;
    }

}
