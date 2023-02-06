package com.xxw.platform.plugin.security.sdk.captcha.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CaptchaConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 图形验证码缓存
 *
 * @author liaoxiting
 * @date 2021/1/15 13:44
 */
public class CaptchaRedisCache extends AbstractRedisCacheOperator<String> {

    public CaptchaRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CaptchaConstants.CAPTCHA_CACHE_KEY_PREFIX;
    }

}
