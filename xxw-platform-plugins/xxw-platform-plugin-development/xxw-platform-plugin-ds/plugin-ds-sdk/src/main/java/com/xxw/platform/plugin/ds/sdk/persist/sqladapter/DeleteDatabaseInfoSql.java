package com.xxw.platform.plugin.ds.sdk.persist.sqladapter;

import com.xxw.platform.plugin.db.api.sqladapter.AbstractSql;
import lombok.Getter;

/**
 * 删除数据源sql
 *
 * @author liaoxiting
 * @date 2019-07-16-13:06
 */
@Getter
public class DeleteDatabaseInfoSql extends AbstractSql {

    @Override
    protected String mysql() {
        return "DELETE from sys_database_info where db_name = ?";
    }

    @Override
    protected String sqlServer() {
        return "DELETE from sys_database_info where db_name = ?";
    }

    @Override
    protected String pgSql() {
        return "DELETE from sys_database_info where db_name = ?";
    }

    @Override
    protected String oracle() {
        return "DELETE from sys_database_info where db_name = ?";
    }
}
