package com.xxw.platform.plugin.security.sdk.count.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.security.api.constants.CounterConstants;

/**
 * 计数用的缓存
 *
 * @author liaoxiting
 * @date 2020/11/15 15:26
 */
public class CountValidateMemoryCache extends AbstractMemoryCacheOperator<Long> {

    public CountValidateMemoryCache(TimedCache<String, Long> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CounterConstants.COUNT_VALIDATE_CACHE_KEY_PREFIX;
    }

}
