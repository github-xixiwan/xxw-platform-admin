package com.xxw.platform.plugin.db.api.sqladapter.database;

import com.xxw.platform.plugin.db.api.sqladapter.AbstractSql;
import lombok.Getter;

/**
 * 创建数据库的sql，可用在租户的创建
 *
 * @author liaoxiting
 * @date 2019-07-16-13:06
 */
@Getter
public class GetDatabasesSql extends AbstractSql {

    @Override
    protected String mysql() {
        return "show databases;";
    }

    @Override
    protected String sqlServer() {
        return "";
    }

    @Override
    protected String pgSql() {
        return "";
    }

    @Override
    protected String oracle() {
        return "";
    }
}
