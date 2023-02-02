package com.xxw.platform.plugin.config.business.sqladapter;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.config.api.SysConfigDataApi;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Mysql数据库的系统配置表获取
 *
 * @author fengshuonan
 * @date 2021/3/27 21:18
 */
@Slf4j
public class MysqlSysConfigData implements SysConfigDataApi {

    @Override
    public List<Entity> getConfigs(Connection conn) throws SQLException {
        return SqlExecutor.query(conn, getConfigListSql(), new EntityListHandler(), StatusEnum.ENABLE.getCode(), YesOrNotEnum.N.getCode());
    }

    @Override
    public String getConfigListSql() {
        return "select config_code, config_value from sys_config where status_flag = ? and del_flag = ?";
    }

}
