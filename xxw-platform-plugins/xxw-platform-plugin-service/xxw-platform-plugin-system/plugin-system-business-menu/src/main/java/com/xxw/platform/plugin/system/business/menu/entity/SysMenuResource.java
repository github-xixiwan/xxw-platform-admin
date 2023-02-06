package com.xxw.platform.plugin.system.business.menu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 菜单资源的关联
 *
 * @author liaoxiting
 * @date 2021/8/8 21:35
 */
@Data
@TableName("sys_menu_resource")
@EqualsAndHashCode(callSuper = true)
public class SysMenuResource extends BaseEntity implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "menu_resource_id")
    @ChineseDescription("主键")
    private Long menuResourceId;

    /**
     * 绑定资源的类型，1：菜单，2：菜单下按钮
     */
    @TableField(value = "business_type")
    @ChineseDescription("绑定资源的类型")
    private Integer businessType;

    /**
     * 菜单或按钮id
     */
    @TableField(value = "business_id")
    @ChineseDescription("菜单或按钮id")
    private Long businessId;

    /**
     * 资源的编码
     */
    @TableField(value = "resource_code")
    @ChineseDescription("资源的编码")
    private String resourceCode;

}
