package com.xxw.platform.plugin.system.business.resource.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;

/**
 * 基于内存的资源缓存
 *
 * @author liaoxiting
 * @date 2021/5/17 16:05
 */
public class MemoryResourceCache extends AbstractMemoryCacheOperator<ResourceDefinition> {

    /**
     * TimedCache的key是资源url，value是ResourceDefinition
     *
     * @author liaoxiting
     * @date 2021/5/17 16:06
     */
    public MemoryResourceCache(TimedCache<String, ResourceDefinition> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return ScannerConstants.RESOURCE_CACHE_KEY;
    }

}
