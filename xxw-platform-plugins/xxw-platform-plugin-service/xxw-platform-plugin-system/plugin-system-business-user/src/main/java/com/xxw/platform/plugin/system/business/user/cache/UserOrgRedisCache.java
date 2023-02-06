package com.xxw.platform.plugin.system.business.user.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserOrgDTO;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 用户组织机构缓存
 *
 * @author liaoxiting
 * @date 2021/2/28 10:23
 */
public class UserOrgRedisCache extends AbstractRedisCacheOperator<SysUserOrgDTO> {

    public UserOrgRedisCache(RedisTemplate<String, SysUserOrgDTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.USER_ORG_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}
