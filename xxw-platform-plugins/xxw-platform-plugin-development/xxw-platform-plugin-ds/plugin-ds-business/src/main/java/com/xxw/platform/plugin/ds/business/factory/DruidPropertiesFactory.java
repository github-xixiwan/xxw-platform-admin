package com.xxw.platform.plugin.ds.business.factory;

import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import com.xxw.platform.plugin.ds.business.entity.DatabaseInfo;

/**
 * Druid数据源创建
 *
 * @author liaoxiting
 * @date 2020/11/1 21:44
 */
public class DruidPropertiesFactory {

    /**
     * 创建druid配置
     *
     * @author liaoxiting
     * @date 2019-06-15 20:05
     */
    public static DruidProperties createDruidProperties(DatabaseInfo databaseInfo) {
        DruidProperties druidProperties = new DruidProperties();
        druidProperties.setDriverClassName(databaseInfo.getJdbcDriver());
        druidProperties.setUsername(databaseInfo.getUsername());
        druidProperties.setPassword(databaseInfo.getPassword());
        druidProperties.setUrl(databaseInfo.getJdbcUrl());
        return druidProperties;
    }

}
