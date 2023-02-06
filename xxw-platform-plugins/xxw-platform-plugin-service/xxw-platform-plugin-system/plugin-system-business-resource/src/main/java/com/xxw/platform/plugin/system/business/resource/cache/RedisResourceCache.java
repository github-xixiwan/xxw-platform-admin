package com.xxw.platform.plugin.system.business.resource.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisHashCacheOperator;
import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 基于redis的资源缓存
 *
 * @author liaoxiting
 * @date 2021/5/17 16:05
 */
public class RedisResourceCache extends AbstractRedisHashCacheOperator<ResourceDefinition> {

    /**
     * RedisTemplate的key是资源url，value是ResourceDefinition
     *
     * @author liaoxiting
     * @date 2021/5/17 16:06
     */
    public RedisResourceCache(RedisTemplate<String, ResourceDefinition> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * hash结构的key
     *
     * @author liaoxiting
     * @date 2021/5/17 17:34
     */
    @Override
    public String getCommonKeyPrefix() {
        return ScannerConstants.RESOURCE_CACHE_KEY;
    }

}
