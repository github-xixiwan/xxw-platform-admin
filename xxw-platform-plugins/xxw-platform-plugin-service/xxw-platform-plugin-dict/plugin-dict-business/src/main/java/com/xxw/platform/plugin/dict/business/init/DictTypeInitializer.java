package com.xxw.platform.plugin.dict.business.init;

import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import com.xxw.platform.plugin.db.api.sqladapter.AbstractSql;
import com.xxw.platform.plugin.db.init.actuator.DbInitializer;
import com.xxw.platform.plugin.dict.business.entity.SysDictType;
import com.xxw.platform.plugin.dict.business.sqladapter.DictTypeSql;
import org.springframework.stereotype.Component;

/**
 * 字典数据库初始化程序
 *
 * @author liaoxiting
 * @date 2020/12/9 上午11:02
 * @see AbstractSql
 */
@Component
public class DictTypeInitializer extends DbInitializer {

    @Override
    protected String getTableInitSql() {
        DruidProperties druidProperties = getDruidProperties();
        return new DictTypeSql().getSql(druidProperties.getUrl());
    }

    @Override
    protected String getTableName() {
        return "sys_dict_type";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysDictType.class;
    }
}
