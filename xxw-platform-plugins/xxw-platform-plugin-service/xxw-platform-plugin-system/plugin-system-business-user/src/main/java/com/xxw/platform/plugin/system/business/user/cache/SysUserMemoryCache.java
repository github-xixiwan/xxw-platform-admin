package com.xxw.platform.plugin.system.business.user.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;

/**
 * 用户的缓存
 *
 * @author liaoxiting
 * @date 2021/2/28 10:23
 */
public class SysUserMemoryCache extends AbstractMemoryCacheOperator<SysUserDTO> {

    public SysUserMemoryCache(TimedCache<String, SysUserDTO> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.USER_CACHE_PREFIX;
    }

}
