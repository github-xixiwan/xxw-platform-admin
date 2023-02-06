package com.xxw.platform.plugin.cache.sdk.memory.operator;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;

/**
 * 默认内存缓存的实现，value存放Object类型
 *
 * @author liaoxiting
 * @date 2021/2/24 22:16
 */
public class DefaultMemoryCacheOperator extends AbstractMemoryCacheOperator<Object> {

    public DefaultMemoryCacheOperator(TimedCache<String, Object> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return CacheConstants.DEFAULT_OBJECT_CACHE_PREFIX;
    }

}
