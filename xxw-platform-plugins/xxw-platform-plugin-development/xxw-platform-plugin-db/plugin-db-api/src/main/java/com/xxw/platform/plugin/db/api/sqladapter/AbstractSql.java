package com.xxw.platform.plugin.db.api.sqladapter;

import com.xxw.platform.frame.common.enums.DbTypeEnum;

/**
 * 异构sql获取基类，通过继承此类，编写使用不同数据库的sql
 *
 * @author liaoxiting
 * @date 2020/10/31 23:44
 */
public abstract class AbstractSql {

    /**
     * 获取异构sql
     *
     * @param jdbcUrl 数据连接的url
     * @return 具体的sql
     * @author liaoxiting
     * @date 2020/10/31 23:44
     */
    public String getSql(String jdbcUrl) {
        if (jdbcUrl.contains(DbTypeEnum.ORACLE.getUrlWords())) {
            return oracle();
        }
        if (jdbcUrl.contains(DbTypeEnum.DM.getUrlWords())) {
            return oracle();
        }
        if (jdbcUrl.contains(DbTypeEnum.MS_SQL.getUrlWords())) {
            return sqlServer();
        }
        if (jdbcUrl.contains(DbTypeEnum.PG_SQL.getUrlWords())) {
            return pgSql();
        }
        return mysql();
    }

    /**
     * 获取mysql的sql语句
     *
     * @return 具体的sql
     * @author liaoxiting
     * @date 2020/10/31 23:45
     */
    protected abstract String mysql();

    /**
     * 获取sqlServer的sql语句
     *
     * @return 具体的sql
     * @author liaoxiting
     * @date 2020/10/31 23:45
     */
    protected abstract String sqlServer();

    /**
     * 获取pgSql的sql语句
     *
     * @return 具体的sql
     * @author liaoxiting
     * @date 2020/10/31 23:45
     */
    protected abstract String pgSql();

    /**
     * 获取oracle的sql语句
     *
     * @return 具体的sql
     * @author liaoxiting
     * @date 2020/10/31 23:45
     */
    protected abstract String oracle();

}
