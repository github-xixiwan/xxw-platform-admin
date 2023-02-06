package com.xxw.platform.plugin.cache.sdk.redis.operator;

import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 默认redis缓存的实现，value存放Object类型
 *
 * @author liaoxiting
 * @date 2021/2/24 22:16
 */
public class DefaultRedisCacheOperator extends AbstractRedisCacheOperator<Object> {

    public DefaultRedisCacheOperator(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CacheConstants.DEFAULT_OBJECT_CACHE_PREFIX;
    }

}
