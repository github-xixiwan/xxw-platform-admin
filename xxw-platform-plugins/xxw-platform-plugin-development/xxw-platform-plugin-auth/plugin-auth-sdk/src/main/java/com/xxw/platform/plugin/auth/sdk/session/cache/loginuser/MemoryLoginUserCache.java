package com.xxw.platform.plugin.auth.sdk.session.cache.loginuser;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;

/**
 * 基于内存的登录用户缓存
 *
 * @author liaoxiting
 * @date 2020/12/24 19:16
 */
public class MemoryLoginUserCache extends AbstractMemoryCacheOperator<LoginUser> {

    public MemoryLoginUserCache(TimedCache<String, LoginUser> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return AuthConstants.LOGGED_TOKEN_PREFIX;
    }

}
