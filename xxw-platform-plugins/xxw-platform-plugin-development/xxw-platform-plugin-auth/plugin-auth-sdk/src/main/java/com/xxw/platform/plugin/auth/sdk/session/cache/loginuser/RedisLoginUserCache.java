package com.xxw.platform.plugin.auth.sdk.session.cache.loginuser;

import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于redis的登录用户缓存
 *
 * @author liaoxiting
 * @date 2020/12/24 19:16
 */
public class RedisLoginUserCache extends AbstractRedisCacheOperator<LoginUser> {

    public RedisLoginUserCache(RedisTemplate<String, LoginUser> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return AuthConstants.LOGGED_TOKEN_PREFIX;
    }

}
