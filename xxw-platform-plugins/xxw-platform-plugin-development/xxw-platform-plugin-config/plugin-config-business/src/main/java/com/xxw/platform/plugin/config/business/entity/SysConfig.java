package com.xxw.platform.plugin.config.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 参数配置
 * </p>
 *
 * @author liaoxiting
 * @date 2019/6/20 13:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_config")
public class SysConfig extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long configId;

    /**
     * 名称
     */
    @TableField("config_name")
    @ChineseDescription("名称")
    private String configName;

    /**
     * 编码
     */
    @TableField("config_code")
    @ChineseDescription("编码")
    private String configCode;

    /**
     * 属性值
     */
    @TableField("config_value")
    @ChineseDescription("属性值")
    private String configValue;

    /**
     * 是否是系统参数：Y-是，N-否
     */
    @TableField("sys_flag")
    @ChineseDescription("是否是系统参数：Y-是，N-否")
    private String sysFlag;

    /**
     * 备注
     */
    @TableField("remark")
    @ChineseDescription("备注")
    private String remark;

    /**
     * 状态：1-正常，2停用
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-正常，2停用")
    private Integer statusFlag;

    /**
     * 常量所属分类的编码，来自于“常量的分类”字典
     */
    @TableField("group_code")
    @ChineseDescription("常量所属分类的编码")
    private String groupCode;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除：Y-被删除，N-未删除")
    private String delFlag;

}
