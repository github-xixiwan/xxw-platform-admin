package com.xxw.platform.plugin.system.business.user.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;

import java.util.List;

/**
 * 用户角色的内存缓存
 * <p>
 * key为userId，value是Long类型集合，为角色的集合
 *
 * @author liaoxiting
 * @date 2021/7/29 22:54
 */
public class UserRoleMemoryCache extends AbstractMemoryCacheOperator<List<Long>> {

    public UserRoleMemoryCache(TimedCache<String, List<Long>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.USER_ROLES_CACHE_PREFIX;
    }

}
