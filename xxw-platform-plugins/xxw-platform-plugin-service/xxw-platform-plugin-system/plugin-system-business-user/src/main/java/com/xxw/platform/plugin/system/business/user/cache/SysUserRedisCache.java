package com.xxw.platform.plugin.system.business.user.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 用户的缓存
 *
 * @author liaoxiting
 * @date 2021/2/28 10:23
 */
public class SysUserRedisCache extends AbstractRedisCacheOperator<SysUserDTO> {

    public SysUserRedisCache(RedisTemplate<String, SysUserDTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.USER_CACHE_PREFIX;
    }

}
