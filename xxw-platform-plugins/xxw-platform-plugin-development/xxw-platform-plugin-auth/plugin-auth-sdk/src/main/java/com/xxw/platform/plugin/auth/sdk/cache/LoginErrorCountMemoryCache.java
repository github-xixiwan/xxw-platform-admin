package com.xxw.platform.plugin.auth.sdk.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.auth.api.constants.LoginCacheConstants;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;

/**
 * 记录用户登录失败次数的缓存
 * <p>
 * key是用户账号，value是登录失败错误次数
 *
 * @author liaoxiting
 * @date 2022/3/15 17:09
 */
public class LoginErrorCountMemoryCache extends AbstractMemoryCacheOperator<Integer> {

    public LoginErrorCountMemoryCache(TimedCache<String, Integer> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return LoginCacheConstants.LOGIN_ERROR_CACHE_PREFIX;
    }

}