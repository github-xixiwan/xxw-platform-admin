package com.xxw.platform.plugin.dict.business.init;

import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import com.xxw.platform.plugin.db.api.sqladapter.AbstractSql;
import com.xxw.platform.plugin.db.init.actuator.DbInitializer;
import com.xxw.platform.plugin.dict.business.entity.SysDict;
import com.xxw.platform.plugin.dict.business.sqladapter.DictSql;
import org.springframework.stereotype.Component;

/**
 * 字典数据库初始化程序
 *
 * @author liaoxiting
 * @date 2020/12/9 上午11:02
 * @see AbstractSql
 */
@Component
public class DictInitializer extends DbInitializer {

    @Override
    protected String getTableInitSql() {
        DruidProperties druidProperties = getDruidProperties();
        return new DictSql().getSql(druidProperties.getUrl());
    }

    @Override
    protected String getTableName() {
        return "sys_dict";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SysDict.class;
    }
}
