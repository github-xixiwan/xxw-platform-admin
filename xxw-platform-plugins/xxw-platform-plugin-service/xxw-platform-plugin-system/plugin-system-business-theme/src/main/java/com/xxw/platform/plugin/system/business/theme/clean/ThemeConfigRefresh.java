package com.xxw.platform.plugin.system.business.theme.clean;

import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.config.api.ConfigInitCallbackApi;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 系统初始化之后的主题资源刷新
 *
 * @author fengshuonan
 * @date 2022/2/26 12:55
 */
@Component
public class ThemeConfigRefresh implements ConfigInitCallbackApi {

    @Resource(name = "themeCacheApi")
    private CacheOperatorApi<DefaultTheme> themeCacheApi;

    @Override
    public void initBefore() {

    }

    @Override
    public void initAfter() {
        themeCacheApi.remove(SystemConstants.THEME_GUNS_PLATFORM);
    }

}
