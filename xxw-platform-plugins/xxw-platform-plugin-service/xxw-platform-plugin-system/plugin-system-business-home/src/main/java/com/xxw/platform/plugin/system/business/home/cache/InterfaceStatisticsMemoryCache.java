package com.xxw.platform.plugin.system.business.home.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.StatisticsCacheConstants;

import java.util.Map;

/**
 * 接口统计内存缓存
 * <p>
 * 缓存的key是用户ID，缓存的value是Map<Long, Integer>
 * <p>
 * map的key是statUrlId，value是次数
 *
 * @author liaoxiting
 * @date 2022/2/9 16:36
 */
public class InterfaceStatisticsMemoryCache extends AbstractMemoryCacheOperator<Map<Long, Integer>> {

    public InterfaceStatisticsMemoryCache(TimedCache<String, Map<Long, Integer>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return StatisticsCacheConstants.INTERFACE_STATISTICS_PREFIX;
    }
}
