package com.xxw.platform.plugin.system.api.pojo.menu;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.api.validators.flag.FlagValue;
import com.xxw.platform.plugin.validator.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 系统菜单参数
 *
 * @author liaoxiting
 * @date 2020/3/26 20:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "menuId不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键")
    private Long menuId;

    /**
     * 父id
     */
    @NotNull(message = "menuParentId不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父id")
    private Long menuParentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "菜单名称存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_menu",
            columnName = "menu_name",
            idFieldName = "menu_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("菜单名称")
    private String menuName;

    /**
     * 菜单的编码
     */
    @NotBlank(message = "菜单的编码不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "菜单的编码存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_menu",
            columnName = "menu_code",
            idFieldName = "menu_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("菜单的编码")
    private String menuCode;

    /**
     * 应用分类（应用编码）
     */
    @NotBlank(message = "appCode不能为空", groups = {add.class, edit.class, getAppMenusAntdVue.class})
    @ChineseDescription("应用分类（应用编码）")
    private String appCode;

    /**
     * 是否可见（Y-是，N-否）
     */
    @FlagValue(message = "是否可见格式错误，正确格式应该Y或者N", groups = {add.class, edit.class}, required = false)
    @ChineseDescription("是否可见（Y-是，N-否）")
    private String visible;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal menuSort;

    /**
     * 备注
     */
    @ChineseDescription("备注")
    private String remark;

    /**
     * 菜单的路径（适用于layui-beetl版本）
     */
    @ChineseDescription("菜单的路径（适用于layui-beetl版本）")
    private String layuiPath;

    /**
     * 菜单的图标（适用于layui-beetl版本）
     */
    @ChineseDescription("菜单的图标（适用于layui-beetl版本）")
    private String layuiIcon;

    /**
     * 路由地址，浏览器显示的URL，例如/menu（适用于antd vue版本）
     */
    @ChineseDescription("路由地址，浏览器显示的URL，例如/menu（适用于antd vue版本）")
    private String antdvRouter;

    /**
     * 图标（适用于antd vue版本）
     */
    @ChineseDescription("图标（适用于antd vue版本）")
    private String antdvIcon;

    /**
     * 前端组件名（适用于antd vue版本）
     */
    @ChineseDescription("前端组件名（适用于antd vue版本）")
    private String antdvComponent;

    /**
     * 外部链接打开方式：1-内置打开外链，2-新页面外链（适用于antd vue版本）
     */
    @ChineseDescription("外部链接打开方式：1-内置打开外链，2-新页面外链（适用于antd vue版本）")
    private Integer antdvLinkOpenType;

    /**
     * 外部链接地址（适用于antd vue版本）
     */
    @ChineseDescription("外部链接地址（适用于antd vue版本）")
    private String antdvLinkUrl;

    /**
     * 前台还是后台菜单：1-前台，2-后台，3-前后台都显示
     */
    @ChineseDescription("前台还是后台菜单")
    private Integer antdvFrontType;

    /**
     * 用于非菜单显示页面的重定向url设置
     */
    @ChineseDescription("用于非菜单显示页面的重定向url设置")
    private String antdvActiveUrl;

    /**
     * 获取主页左侧菜单列表（适配Antd Vue的版本）
     */
    public @interface getAppMenusAntdVue {
    }

}
