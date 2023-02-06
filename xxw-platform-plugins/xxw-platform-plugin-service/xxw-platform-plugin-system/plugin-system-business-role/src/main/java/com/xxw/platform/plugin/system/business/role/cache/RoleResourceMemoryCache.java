package com.xxw.platform.plugin.system.business.role.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;

import java.util.List;

/**
 * 角色绑定资源的缓存
 *
 * @author liaoxiting
 * @date 2021/7/30 23:20
 */
public class RoleResourceMemoryCache extends AbstractMemoryCacheOperator<List<String>> {

    public RoleResourceMemoryCache(TimedCache<String, List<String>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.ROLE_RESOURCE_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}
