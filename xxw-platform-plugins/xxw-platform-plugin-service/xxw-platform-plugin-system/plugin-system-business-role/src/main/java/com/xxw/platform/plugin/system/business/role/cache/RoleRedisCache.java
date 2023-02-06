package com.xxw.platform.plugin.system.business.role.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 角色信息的缓存
 *
 * @author liaoxiting
 * @date 2021/7/29 23:29
 */
public class RoleRedisCache extends AbstractRedisCacheOperator<SysRole> {

    public RoleRedisCache(RedisTemplate<String, SysRole> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.ROLE_INFO_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}