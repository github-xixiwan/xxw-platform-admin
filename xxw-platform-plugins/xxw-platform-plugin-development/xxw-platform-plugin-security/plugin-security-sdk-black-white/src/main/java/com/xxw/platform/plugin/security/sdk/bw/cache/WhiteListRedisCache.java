package com.xxw.platform.plugin.security.sdk.bw.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CounterConstants;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 白名单的缓存
 *
 * @author liaoxiting
 * @date 2020/11/15 15:26
 */
public class WhiteListRedisCache extends AbstractRedisCacheOperator<String> {

    public WhiteListRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CounterConstants.WHITE_LIST_CACHE_KEY_PREFIX;
    }

}
