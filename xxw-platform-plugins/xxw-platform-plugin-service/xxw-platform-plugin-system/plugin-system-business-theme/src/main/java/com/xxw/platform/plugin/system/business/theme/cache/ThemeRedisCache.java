package com.xxw.platform.plugin.system.business.theme.cache;

import com.xxw.platform.plugin.cache.sdk.redis.AbstractRedisCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 系统主题缓存
 *
 * @author liaoxiting
 * @date 2022/1/11 9:37
 */
public class ThemeRedisCache extends AbstractRedisCacheOperator<DefaultTheme> {

    public ThemeRedisCache(RedisTemplate<String, DefaultTheme> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.SYSTEM_THEME_CACHE_PREFIX;
    }

}
