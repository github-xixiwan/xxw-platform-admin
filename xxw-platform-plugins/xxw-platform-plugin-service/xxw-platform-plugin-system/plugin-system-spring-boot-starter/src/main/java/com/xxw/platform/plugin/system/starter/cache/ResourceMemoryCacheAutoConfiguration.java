package com.xxw.platform.plugin.system.starter.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import com.xxw.platform.plugin.system.business.resource.cache.MemoryResourceCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 资源缓存自动配置
 *
 * @author liaoxiting
 * @date 2021/5/17 16:44
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.connection.RedisConnectionFactory")
public class ResourceMemoryCacheAutoConfiguration {

    /**
     * 资源缓存
     *
     * @author liaoxiting
     * @date 2021/5/17 16:44
     */
    @Bean
    public CacheOperatorApi<ResourceDefinition> resourceCache() {
        TimedCache<String, ResourceDefinition> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new MemoryResourceCache(timedCache);
    }

}
