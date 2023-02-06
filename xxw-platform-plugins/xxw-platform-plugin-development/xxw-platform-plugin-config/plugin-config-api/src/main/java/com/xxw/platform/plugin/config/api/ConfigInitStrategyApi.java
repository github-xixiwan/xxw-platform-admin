package com.xxw.platform.plugin.config.api;

import com.xxw.platform.plugin.config.api.pojo.ConfigInitItem;

import java.util.List;

/**
 * 配置初始化的策略
 *
 * @author liaoxiting
 * @date 2021/7/8 17:33
 */
public interface ConfigInitStrategyApi {

    /**
     * 获取这个配置组下的标题
     *
     * @author liaoxiting
     * @date 2022/10/24 15:03
     */
    String getTitle();

    /**
     * 获取这个配置组的详细说明
     *
     * @author liaoxiting
     * @date 2022/10/24 15:03
     */
    String getDescription();

    /**
     * 获取需要被初始化的配置集合
     *
     * @return 需要被初始化的配置集合
     * @author liaoxiting
     * @date 2021/7/8 17:40
     */
    List<ConfigInitItem> getInitConfigs();

}
