package com.xxw.platform.plugin.security.sdk.bw.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CounterConstants;

/**
 * 黑名单用户的缓存
 *
 * @author liaoxiting
 * @date 2020/11/20 15:50
 */
public class BlackListMemoryCache extends AbstractMemoryCacheOperator<String> {

    public BlackListMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CounterConstants.BLACK_LIST_CACHE_KEY_PREFIX;
    }

}
