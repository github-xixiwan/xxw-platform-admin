package com.xxw.platform.plugin.cache.sdk.redis.operator;

import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 默认redis缓存的实现，value存放String类型
 *
 * @author liaoxiting
 * @date 2021/2/24 22:16
 */
public class DefaultStringRedisCacheOperator extends AbstractRedisCacheOperator<String> {

    public DefaultStringRedisCacheOperator(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CacheConstants.DEFAULT_STRING_CACHE_PREFIX;
    }

}
