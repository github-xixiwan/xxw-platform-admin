package com.xxw.platform.plugin.system.business.role.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 角色绑定资源的缓存
 *
 * @author liaoxiting
 * @date 2021/7/29 23:29
 */
public class RoleResourceRedisCache extends AbstractRedisCacheOperator<List<String>> {

    public RoleResourceRedisCache(RedisTemplate<String, List<String>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.ROLE_RESOURCE_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }
}