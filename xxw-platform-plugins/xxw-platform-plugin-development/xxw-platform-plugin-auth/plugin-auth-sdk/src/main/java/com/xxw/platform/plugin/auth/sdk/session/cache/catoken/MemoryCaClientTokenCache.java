package com.xxw.platform.plugin.auth.sdk.session.cache.catoken;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;

/**
 * 存放单点回调时候的token和本系统token的映射关系
 * <p>
 * key：    单点回调客户端时候的token
 * value：  本系统的token
 *
 * @author liaoxiting
 * @date 2022/5/20 11:40
 */
public class MemoryCaClientTokenCache extends AbstractMemoryCacheOperator<String> {

    public MemoryCaClientTokenCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return AuthConstants.CA_CLIENT_TOKEN_CACHE_PREFIX;
    }

}
