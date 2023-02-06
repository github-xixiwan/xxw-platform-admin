package com.xxw.platform.plugin.system.api.expander;

import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.config.api.context.ConfigContext;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;

/**
 * 系统的一些基础信息
 *
 * @author liaoxiting
 * @date 2020/12/27 17:13
 */
public class SystemConfigExpander {

    /**
     * 获取系统发布的版本号（防止css和js的缓存）
     *
     * @author liaoxiting
     * @date 2020/12/27 17:14
     */
    public static String getReleaseVersion() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_RELEASE_VERSION", String.class, SystemConstants.DEFAULT_SYSTEM_VERSION);
    }

    /**
     * 获取租户是否开启的标识，默认是关的
     *
     * @author liaoxiting
     * @date 2020/12/27 17:21
     */
    public static Boolean getTenantOpen() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_TENANT_OPEN", Boolean.class, SystemConstants.DEFAULT_TENANT_OPEN);
    }

    /**
     * 获取系统名称
     *
     * @author liaoxiting
     * @date 2020/12/27 17:22
     */
    public static String getSystemName() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_SYSTEM_NAME", String.class, SystemConstants.DEFAULT_SYSTEM_NAME);
    }

    /**
     * 获取默认密码
     *
     * @author liaoxiting
     * @date 2020/11/6 10:05
     */
    public static String getDefaultPassWord() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_DEFAULT_PASSWORD", String.class, AuthConstants.DEFAULT_PASSWORD);
    }
    /**
     * 获取开发开关的状态
     *
     * @return {@link Boolean}
     * @author liaoxiting
     * @date 2022/1/17 14:59
     **/
    public static Boolean getDevSwitchStatus() {
        return ConfigContext.me().getSysConfigValueWithDefault("DEVOPS_DEV_SWITCH_STATUS", Boolean.class, Boolean.FALSE);
    }
}
