package com.xxw.platform.plugin.system.business.user.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 用户角色的redis缓存
 * <p>
 * key为userId，value是Long类型集合，为角色的集合
 *
 * @author liaoxiting
 * @date 2021/7/29 22:54
 */
public class UserRoleRedisCache extends AbstractRedisCacheOperator<List<Long>> {

    public UserRoleRedisCache(RedisTemplate<String, List<Long>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.USER_ROLES_CACHE_PREFIX;
    }

}
