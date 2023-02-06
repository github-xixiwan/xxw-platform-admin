package com.xxw.platform.plugin.system.business.app.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统应用表
 *
 * @author liaoxiting
 * @date 2020/11/24 21:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_app")
public class SysApp extends BaseEntity {

    /**
     * 主键id
     */
    @TableId("app_id")
    @ChineseDescription("主键id")
    private Long appId;

    /**
     * 应用名称
     */
    @TableField("app_name")
    @ChineseDescription("应用名称")
    private String appName;

    /**
     * 编码
     */
    @TableField("app_code")
    @ChineseDescription("编码")
    private String appCode;

    /**
     * 应用图标
     */
    @TableField("app_icon")
    @ChineseDescription("应用图标")
    private String appIcon;

    /**
     * 是否默认激活：Y-是，N-否，激活的应用下的菜单会在首页默认展开
     */
    @TableField("active_flag")
    @ChineseDescription("是否默认激活：Y-是，N-否，激活的应用下的菜单会在首页默认展开")
    private String activeFlag;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 排序-升序
     */
    @TableField("app_sort")
    @ChineseDescription("排序-升序")
    private Integer appSort;

    /**
     * 是否是devops应用：Y-是，N-否
     * <p>
     * devops应用为了在本地集成devops时，做筛选用
     */
    @TableField(value = "devops_flag")
    @ChineseDescription("是否是devops应用：Y-是，N-否")
    private String devopsFlag;

    /**
     * 是否删除：Y-已删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除：Y-已删除，N-未删除")
    private String delFlag;

}
