package com.xxw.platform.plugin.config.api.context;

import com.xxw.platform.plugin.config.api.ConfigApi;
import com.xxw.platform.plugin.config.api.exception.ConfigException;
import com.xxw.platform.plugin.config.api.exception.enums.ConfigExceptionEnum;

/**
 * 系统配置表相关的api
 * <p>
 * 系统配置表默认由数据库实现，可实现在线管理，也可以拓展redis等实现
 * <p>
 * 使用之前请调用setConfigApi初始化
 *
 * @author liaoxiting
 * @date 2020/10/17 10:27
 */
public class ConfigContext {

    private static ConfigApi CONFIG_API = null;

    /**
     * 获取config操作接口
     *
     * @author liaoxiting
     * @date 2020/10/17 14:30
     */
    public static ConfigApi me() {
        if (CONFIG_API == null) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_CONTAINER_IS_NULL);
        }
        return CONFIG_API;
    }

    /**
     * 设置config api的实现
     *
     * @author liaoxiting
     * @date 2020/12/4 14:35
     */
    public static void setConfigApi(ConfigApi configApi) {
        CONFIG_API = configApi;
    }

}
