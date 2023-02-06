package com.xxw.platform.plugin.db.mp.dbid;

import com.xxw.platform.frame.common.enums.DbTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 数据库id选择器，用在一个mapper.xml中包含多种数据库的sql时候
 * <p>
 * 提供给mybatis能识别不同数据库的标识
 *
 * @author liaoxiting
 * @date 2020/10/16 17:02
 */
@Slf4j
public class CustomDatabaseIdProvider implements DatabaseIdProvider {

    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {

        String url = "";

        // 常规获取url的方式
        try {
            url = dataSource.getConnection().getMetaData().getURL();
        } catch (Exception e) {

            // 兼容对seata数据源的判断
            try {
                Class<?> XAClass = Class.forName("io.seata.rm.datasource.xa.DataSourceProxyXA");

                // xa的dataSource
                if (XAClass.isInstance(dataSource)) {
                    Method xaMethod = XAClass.getMethod("getResourceId");
                    Object xaResult = xaMethod.invoke(dataSource);
                    url = xaResult.toString();
                }

            } catch (Exception e2) {
                log.warn("CustomDatabaseIdProvider无法判断当前数据源类型，默认选择Mysql类型");
                return DbTypeEnum.MYSQL.getXmlDatabaseId();
            }
        }

        // 达梦和oracle使用同一种
        if (url.contains(DbTypeEnum.ORACLE.getUrlWords())) {
            return DbTypeEnum.ORACLE.getXmlDatabaseId();
        }
        if (url.contains(DbTypeEnum.DM.getUrlWords())) {
            return DbTypeEnum.ORACLE.getXmlDatabaseId();
        }

        if (url.contains(DbTypeEnum.MS_SQL.getUrlWords())) {
            return DbTypeEnum.MS_SQL.getXmlDatabaseId();
        }
        if (url.contains(DbTypeEnum.PG_SQL.getUrlWords())) {
            return DbTypeEnum.PG_SQL.getXmlDatabaseId();
        }

        return DbTypeEnum.MYSQL.getXmlDatabaseId();
    }

}
