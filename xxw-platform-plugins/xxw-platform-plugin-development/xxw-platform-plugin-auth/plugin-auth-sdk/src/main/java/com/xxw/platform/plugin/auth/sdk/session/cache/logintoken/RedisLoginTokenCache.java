package com.xxw.platform.plugin.auth.sdk.session.cache.logintoken;

import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
/**
 * 基于redis的token的缓存
 *
 * @author liaoxiting
 * @date 2020/12/24 19:16
 */
public class RedisLoginTokenCache extends AbstractRedisCacheOperator<Set<String>> {

    public RedisLoginTokenCache(RedisTemplate<String, Set<String>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return AuthConstants.LOGGED_USERID_PREFIX;
    }

}
