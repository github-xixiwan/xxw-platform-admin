package com.xxw.platform.plugin.config.api;

/**
 * 系统配置表相关的api
 * <p>
 * 系统配置表的实现可以用内存，数据库或redis
 *
 * @author liaoxiting
 * @date 2020/10/17 10:27
 */
public interface InitConfigApi {

    /**
     * 获取配置是否初始化的标志
     *
     * @return true-系统已经初始化，false-系统没有初始化
     * @author liaoxiting
     * @date 2021/7/8 17:20
     */
    Boolean getInitConfigFlag();

}
