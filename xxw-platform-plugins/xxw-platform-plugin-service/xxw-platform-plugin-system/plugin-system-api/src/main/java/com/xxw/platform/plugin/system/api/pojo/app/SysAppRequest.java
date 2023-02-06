package com.xxw.platform.plugin.system.api.pojo.app;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.api.validators.status.StatusValue;
import com.xxw.platform.plugin.validator.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统应用参数
 *
 * @author liaoxiting
 * @date 2020/3/24 20:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "appId不能为空", groups = {edit.class, delete.class, detail.class, updateActiveFlag.class, updateStatus.class})
    @ChineseDescription("主键")
    private Long appId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "名称存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_app",
            columnName = "app_name",
            idFieldName = "app_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("名称不能为空")
    private String appName;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "编码存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_app",
            columnName = "app_code",
            idFieldName = "app_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("应用编码")
    private String appCode;

    /**
     * 应用图标
     */
    @NotBlank(message = "应用图标不能为空", groups = {add.class, edit.class})
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
    @NotNull(message = "状态为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 排序-升序
     */
    @ChineseDescription("排序-升序")
    private Integer appSort;

    /**
     * 设置为默认状态
     */
    public @interface updateActiveFlag {
    }

}
