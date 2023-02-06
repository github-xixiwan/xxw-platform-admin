package com.xxw.platform.plugin.system.api.pojo.app;

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
public class SysAppResult extends BaseEntity {

    /**
     * 主键id
     */
    @ChineseDescription("主键id")
    private Long appId;

    /**
     * 应用名称
     */
    @ChineseDescription("应用名称")
    private String appName;

    /**
     * 编码
     */
    @ChineseDescription("编码")
    private String appCode;

    /**
     * 应用图标
     */
    @ChineseDescription("应用图标")
    private String appIcon;

    /**
     * 是否默认激活：Y-是，N-否，激活的应用下的菜单会在首页默认展开
     */
    @ChineseDescription("是否默认激活：Y-是，N-否，激活的应用下的菜单会在首页默认展开")
    private String activeFlag;

    /**
     * 状态：1-启用，2-禁用
     */
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 是否删除：Y-已删除，N-未删除
     */
    @ChineseDescription("是否删除：Y-已删除，N-未删除")
    private String delFlag;

    /**
     * 排序-升序
     */
    @ChineseDescription("排序-升序")
    private Integer appSort;
}
