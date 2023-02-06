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
public class CreateDatabaseSql extends AbstractSql {

    @Override
    protected String mysql() {
        return "CREATE DATABASE IF NOT EXISTS ? DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";
    }

    @Override
    protected String sqlServer() {
        return "if not exists (select * from sysobjects where name=? and xtype='U')\n" +
                "    create table ? ;" +
                "go";
    }

    @Override
    protected String pgSql() {
        return "CREATE DATABASE ?;";
    }

    @Override
    protected String oracle() {
        return "create tablespace ? datafile '/opt/oracle/xxw.dbf' size 500m autoextend on next 5m maxsize unlimited;";
    }
}
