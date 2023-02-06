package com.xxw.platform.plugin.ds.api.constants;

/**
 * 数据源容器的常量
 *
 * @author liaoxiting
 * @date 2020/10/31 21:58
 */
public interface DatasourceContainerConstants {

    /**
     * db模块的名称
     */
    String DS_CTN_MODULE_NAME = "xxw-platform-plugin-ds";

    /**
     * 异常枚举的步进值
     */
    String DS_CTN_EXCEPTION_STEP_CODE = "16";

    /**
     * 主数据源名称
     */
    String MASTER_DATASOURCE_NAME = "master";

    /**
     * 多数据源切换的aop的顺序
     */
    int MULTI_DATA_SOURCE_EXCHANGE_AOP = 1;

    /**
     * 数据源的分组标识
     */
    String DATASOURCE_GROUP_CODE = "DATASOURCE";

}
