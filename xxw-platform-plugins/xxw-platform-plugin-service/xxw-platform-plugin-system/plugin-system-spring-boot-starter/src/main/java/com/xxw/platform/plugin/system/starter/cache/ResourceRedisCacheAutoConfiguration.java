package com.xxw.platform.plugin.system.starter.cache;

import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.cache.sdk.redis.util.CreateRedisTemplateUtil;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import com.xxw.platform.plugin.system.business.resource.cache.RedisResourceCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 资源缓存自动配置，Redis配置
 *
 * @author liaoxiting
 * @date 2022/11/8 23:26
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
public class ResourceRedisCacheAutoConfiguration {

    /**
     * 资源缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:27
     */
    @Bean
    public CacheOperatorApi<ResourceDefinition> resourceCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ResourceDefinition> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RedisResourceCache(redisTemplate);
    }

}
