package com.xxw.platform.plugin.cache.memory.starter;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import com.xxw.platform.plugin.cache.sdk.memory.operator.DefaultMemoryCacheOperator;
import com.xxw.platform.plugin.cache.sdk.memory.operator.DefaultStringMemoryCacheOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于内存缓存的默认配置
 *
 * @author liaoxiting
 * @date 2021/1/31 20:32
 */
@Configuration
public class MemoryCacheAutoConfiguration {

    /**
     * 创建默认的value是string类型的内存缓存
     *
     * @author liaoxiting
     * @date 2021/1/31 20:39
     */
    @Bean
    @ConditionalOnMissingBean(name = "defaultStringCacheOperator")
    public DefaultStringMemoryCacheOperator defaultStringCacheOperator() {
        TimedCache<String, String> stringTimedCache = CacheUtil.newTimedCache(CacheConstants.DEFAULT_CACHE_TIMEOUT);
        return new DefaultStringMemoryCacheOperator(stringTimedCache);
    }

    /**
     * 创建默认的value是object类型的内存缓存
     *
     * @author liaoxiting
     * @date 2021/1/31 20:39
     */
    @Bean
    @ConditionalOnMissingBean(name = "defaultMemoryCacheOperator")
    public DefaultMemoryCacheOperator defaultMemoryCacheOperator() {
        TimedCache<String, Object> objectTimedCache = CacheUtil.newTimedCache(CacheConstants.DEFAULT_CACHE_TIMEOUT);
        return new DefaultMemoryCacheOperator(objectTimedCache);
    }

}
