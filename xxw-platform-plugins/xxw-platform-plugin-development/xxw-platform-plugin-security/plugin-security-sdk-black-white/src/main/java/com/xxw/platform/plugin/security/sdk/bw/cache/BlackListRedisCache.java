package com.xxw.platform.plugin.security.sdk.bw.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CounterConstants;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 黑名单用户的缓存
 *
 * @author liaoxiting
 * @date 2020/11/20 15:50
 */
public class BlackListRedisCache extends AbstractRedisCacheOperator<String> {

    public BlackListRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CounterConstants.BLACK_LIST_CACHE_KEY_PREFIX;
    }

}
