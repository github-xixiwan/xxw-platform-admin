package com.xxw.platform.plugin.security.sdk.captcha.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CaptchaConstants;

/**
 * 图形验证码缓存
 *
 * @author liaoxiting
 * @date 2021/1/15 13:44
 */
public class CaptchaMemoryCache extends AbstractMemoryCacheOperator<String> {

    public CaptchaMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CaptchaConstants.CAPTCHA_CACHE_KEY_PREFIX;
    }

}
