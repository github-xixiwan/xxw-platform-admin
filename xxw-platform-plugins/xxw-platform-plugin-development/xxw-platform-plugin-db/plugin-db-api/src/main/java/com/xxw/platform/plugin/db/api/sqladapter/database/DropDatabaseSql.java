package com.xxw.platform.plugin.db.api.sqladapter.database;

import com.xxw.platform.plugin.db.api.sqladapter.AbstractSql;
import lombok.Getter;

/**
 * 数据库删除，可用在租户的删除
 *
 * @author liaoxiting
 * @date 2020/9/4
 */
@Getter
public class DropDatabaseSql extends AbstractSql {

    @Override
    protected String mysql() {
        return "DROP DATABASE ?;";
    }

    @Override
    protected String sqlServer() {
        return "DROP DATABASE ?;";
    }

    @Override
    protected String pgSql() {
        return "DROP DATABASE ?;";
    }

    @Override
    protected String oracle() {
        return "DROP DATASPACE ?;";
    }

}
