package com.xxw.platform.plugin.system.business.user.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserOrgDTO;

/**
 * 用户组织机构缓存
 *
 * @author liaoxiting
 * @date 2021/7/30 23:07
 */
public class UserOrgMemoryCache extends AbstractMemoryCacheOperator<SysUserOrgDTO> {

    public UserOrgMemoryCache(TimedCache<String, SysUserOrgDTO> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.USER_ORG_CACHE_PREFIX;
    }

    @Override
    public Boolean divideByTenant() {
        return true;
    }

}
