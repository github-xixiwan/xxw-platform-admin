package com.xxw.platform.plugin.system.business.theme.cache;

import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.sdk.memory.AbstractMemoryCacheOperator;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;

/**
 * 系统主题缓存
 *
 * @author liaoxiting
 * @date 2022/1/11 9:37
 */
public class ThemeMemoryCache extends AbstractMemoryCacheOperator<DefaultTheme> {

    public ThemeMemoryCache(TimedCache<String, DefaultTheme> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SystemCachesConstants.SYSTEM_THEME_CACHE_PREFIX;
    }

}
