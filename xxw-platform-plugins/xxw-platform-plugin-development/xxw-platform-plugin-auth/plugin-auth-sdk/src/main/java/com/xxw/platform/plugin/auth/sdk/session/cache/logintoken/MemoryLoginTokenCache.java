package com.xxw.platform.plugin.auth.sdk.session.cache.logintoken;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;

import java.util.Set;
/**
 * 基于内存的token缓存
 *
 * @author liaoxiting
 * @date 2020/12/24 19:16
 */
public class MemoryLoginTokenCache extends AbstractMemoryCacheOperator<Set<String>> {

    public MemoryLoginTokenCache(TimedCache<String, Set<String>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return AuthConstants.LOGGED_USERID_PREFIX;
    }

}
