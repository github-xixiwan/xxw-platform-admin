package com.xxw.platform.plugin.system.business.role.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;

/**
 * 角色信息的缓存
 *
 * @author liaoxiting
 * @date 2021/7/29 23:28
 */
public class RoleMemoryCache extends AbstractMemoryCacheOperator<SysRole> {

    public RoleMemoryCache(TimedCache<String, SysRole> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.ROLE_INFO_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}
