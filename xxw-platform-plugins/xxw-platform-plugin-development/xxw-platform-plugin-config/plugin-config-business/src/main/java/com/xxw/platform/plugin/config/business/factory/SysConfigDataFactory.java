package com.xxw.platform.plugin.config.business.factory;

import com.xxw.platform.frame.common.enums.DbTypeEnum;
import com.xxw.platform.frame.common.util.DatabaseTypeUtil;
import com.xxw.platform.plugin.config.api.SysConfigDataApi;
import com.xxw.platform.plugin.config.business.sqladapter.MssqlSysConfigData;
import com.xxw.platform.plugin.config.business.sqladapter.MysqlSysConfigData;
import com.xxw.platform.plugin.config.business.sqladapter.OracleSysConfigData;
import com.xxw.platform.plugin.config.business.sqladapter.PgsqlSysConfigData;

/**
 * SysConfigDataApi的创建工厂
 *
 * @author liaoxiting
 * @date 2021/3/27 21:27
 */
public class SysConfigDataFactory {

    /**
     * 通过jdbc url获取api
     *
     * @author liaoxiting
     * @date 2021/3/27 21:27
     */
    public static SysConfigDataApi getSysConfigDataApi(String jdbcUrl) {
        DbTypeEnum dbType = DatabaseTypeUtil.getDbType(jdbcUrl);
        if (DbTypeEnum.MYSQL.equals(dbType)) {
            return new MysqlSysConfigData();
        } else if (DbTypeEnum.PG_SQL.equals(dbType)) {
            return new PgsqlSysConfigData();
        } else if (DbTypeEnum.MS_SQL.equals(dbType)) {
            return new MssqlSysConfigData();
        } else if (DbTypeEnum.ORACLE.equals(dbType)) {
            return new OracleSysConfigData();
        } else if (DbTypeEnum.DM.equals(dbType)) {
            return new OracleSysConfigData();
        }
        return new MysqlSysConfigData();
    }

}
