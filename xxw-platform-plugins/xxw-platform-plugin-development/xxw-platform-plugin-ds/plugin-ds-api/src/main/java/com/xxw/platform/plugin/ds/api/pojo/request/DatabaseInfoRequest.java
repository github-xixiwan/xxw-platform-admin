package com.xxw.platform.plugin.ds.api.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据库信息表
 *
 * @author liaoxiting
 * @date 2020/11/1 21:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DatabaseInfoRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @NotNull(message = "dbId不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键id")
    private Long dbId;

    /**
     * 数据库名称（英文名称）
     */
    @NotBlank(message = "数据库名称不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "数据库名称存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_database_info",
            columnName = "db_name",
            idFieldName = "db_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("数据库名称（英文名称）")
    private String dbName;

    /**
     * jdbc的驱动类型
     */
    @NotBlank(message = "jdbc的驱动类型为空", groups = {add.class, edit.class})
    @ChineseDescription("jdbc的驱动类型")
    private String jdbcDriver;

    /**
     * jdbc的url
     */
    @NotBlank(message = "jdbc的url", groups = {add.class, edit.class})
    @ChineseDescription("jdbc的url")
    private String jdbcUrl;

    /**
     * 数据库连接的账号
     */
    @NotBlank(message = "数据库连接的账号", groups = {add.class, edit.class})
    @ChineseDescription("数据库连接的账号")
    private String username;

    /**
     * 数据库连接密码
     */
    @NotBlank(message = "数据库连接密码", groups = {add.class, edit.class})
    @ChineseDescription("数据库连接密码")
    private String password;

    /**
     * 数据库schemaName，注意，每种数据库的schema意义不同
     */
    @ChineseDescription("数据库schemaName")
    private String schemaName;

    /**
     * 状态标识：1-正常，2-无法连接
     */
    @ChineseDescription("状态标识：1-正常，2-无法连接")
    private Integer statusFlag;

    /**
     * 备注，摘要
     */
    @ChineseDescription("备注")
    private String remarks;

}
