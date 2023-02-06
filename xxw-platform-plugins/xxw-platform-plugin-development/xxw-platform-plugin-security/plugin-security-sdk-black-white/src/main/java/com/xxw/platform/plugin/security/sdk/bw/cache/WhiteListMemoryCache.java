package com.xxw.platform.plugin.security.sdk.bw.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CounterConstants;

/**
 * 白名单的缓存
 *
 * @author liaoxiting
 * @date 2020/11/15 15:26
 */
public class WhiteListMemoryCache extends AbstractMemoryCacheOperator<String> {

    public WhiteListMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CounterConstants.WHITE_LIST_CACHE_KEY_PREFIX;
    }

}
