package com.xxw.platform.plugin.system.business.role.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;

import java.util.List;

/**
 * 角色绑定的数据范围
 *
 * @author liaoxiting
 * @date 2021/7/31 17:53
 */
public class RoleDataScopeMemoryCache extends AbstractMemoryCacheOperator<List<Long>> {

    public RoleDataScopeMemoryCache(TimedCache<String, List<Long>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.ROLE_DATA_SCOPE_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}
