package com.xxw.platform.plugin.system.business.theme.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统主题模板属性关联表
 *
 * @author xixiaowei
 * @date 2021/12/17 9:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_theme_template_rel")
public class SysThemeTemplateRel extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "relation_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键ID")
    private Long relationId;

    /**
     * 模板ID
     */
    @TableField("template_id")
    @ChineseDescription("模板ID")
    private Long templateId;

    /**
     * 属性编码
     */
    @TableField("field_code")
    @ChineseDescription("属性编码")
    private String fieldCode;
}
