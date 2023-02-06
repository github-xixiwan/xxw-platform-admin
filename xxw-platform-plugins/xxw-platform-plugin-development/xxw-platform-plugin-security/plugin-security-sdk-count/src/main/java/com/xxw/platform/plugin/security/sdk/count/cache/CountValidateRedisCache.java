package com.xxw.platform.plugin.security.sdk.count.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CounterConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 图形验证码缓存
 *
 * @author liaoxiting
 * @date 2021/1/15 13:44
 */
public class CountValidateRedisCache extends AbstractRedisCacheOperator<Long> {

    public CountValidateRedisCache(RedisTemplate<String, Long> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CounterConstants.COUNT_VALIDATE_CACHE_KEY_PREFIX;
    }

}
