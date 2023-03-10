package com.xxw.platform.plugin.auth.api.expander;

import com.xxw.platform.plugin.config.api.context.ConfigContext;

/**
 * 登录相关配置快速获取
 *
 * @author liaoxiting
 * @date 2022/1/24 15:47
 */
public class LoginConfigExpander {

    /**
     * 获取帐号错误次数校验开关
     *
     * @author liaoxiting
     * @date 2022/1/24 15:48
     */
    public static boolean getAccountErrorDetectionFlag() {
        return ConfigContext.me().getSysConfigValueWithDefault("ACCOUNT_ERROR_DETECTION", Boolean.class, false);
    }
}
