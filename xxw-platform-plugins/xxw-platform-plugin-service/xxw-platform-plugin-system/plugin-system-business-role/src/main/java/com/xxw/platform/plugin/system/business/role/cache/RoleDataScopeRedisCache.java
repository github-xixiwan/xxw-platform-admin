package com.xxw.platform.plugin.system.business.role.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 角色绑定的数据范围
 *
 * @author liaoxiting
 * @date 2021/7/31 17:54
 */
public class RoleDataScopeRedisCache extends AbstractRedisCacheOperator<List<Long>> {

    public RoleDataScopeRedisCache(RedisTemplate<String, List<Long>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.ROLE_DATA_SCOPE_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}
