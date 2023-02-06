package com.xxw.platform.plugin.ds.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据库信息表
 *
 * @author liaoxiting
 * @date 2020/11/1 0:15
 */
@TableName("sys_database_info")
@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "db_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long dbId;

    /**
     * 数据库名称（英文名称）
     */
    @TableField("db_name")
    @ChineseDescription("数据库名称（英文名称）")
    private String dbName;

    /**
     * jdbc的驱动类型
     */
    @TableField("jdbc_driver")
    @ChineseDescription("jdbc的驱动类型")
    private String jdbcDriver;

    /**
     * jdbc的url
     */
    @TableField("jdbc_url")
    @ChineseDescription("jdbc的url")
    private String jdbcUrl;

    /**
     * 数据库连接的账号
     */
    @TableField("username")
    @ChineseDescription("数据库连接的账号")
    private String username;

    /**
     * 数据库连接密码
     */
    @TableField("password")
    @ChineseDescription("数据库连接密码")
    private String password;

    /**
     * 数据库的schema名称，每种数据库的schema意义都不同
     */
    @TableField("schema_name")
    @ChineseDescription("数据库的schema名称")
    private String schemaName;

    /**
     * 状态标识：1-正常，2-无法连接
     */
    @TableField("status_flag")
    @ChineseDescription("状态标识：1-正常，2-无法连接")
    private Integer statusFlag;

    /**
     * 无法连接原因
     */
    @TableField("error_description")
    @ChineseDescription("无法连接原因")
    private String errorDescription;

    /**
     * 备注，摘要
     */
    @TableField("remarks")
    @ChineseDescription("备注")
    private String remarks;

    /**
     * 是否删除，Y-被删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    private String delFlag;

    /**
     * 分组名称
     */
    @TableField(exist = false)
    private String groupName;

}
