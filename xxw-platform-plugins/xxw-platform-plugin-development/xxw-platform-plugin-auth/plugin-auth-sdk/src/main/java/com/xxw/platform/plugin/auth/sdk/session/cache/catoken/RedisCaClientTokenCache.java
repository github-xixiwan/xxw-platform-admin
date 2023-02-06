package com.xxw.platform.plugin.auth.sdk.session.cache.catoken;

import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 存放单点回调时候的token和本系统token的映射关系
 * <p>
 * key：    单点回调客户端时候的token
 * value：  本系统的token
 *
 * @author liaoxiting
 * @date 2022/5/20 11:37
 */
public class RedisCaClientTokenCache extends AbstractRedisCacheOperator<String> {

    public RedisCaClientTokenCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return AuthConstants.CA_CLIENT_TOKEN_CACHE_PREFIX;
    }

}
