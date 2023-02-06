package com.xxw.platform.plugin.dict.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 字典实体
 *
 * @author liaoxiting
 * @date 2020/12/26 22:37
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
@Data
public class SysDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @TableId(value = "dict_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("字典id")
    private Long dictId;

    /**
     * 字典编码
     */
    @TableField("dict_code")
    @ChineseDescription("字典编码")
    private String dictCode;

    /**
     * 字典名称
     */
    @TableField("dict_name")
    @ChineseDescription("字典名称")
    private String dictName;

    /**
     * 字典名称首字母
     */
    @TableField("dict_name_pinyin")
    @ChineseDescription("字典名称首字母")
    private String dictNamePinyin;

    /**
     * 字典编码
     */
    @TableField("dict_encode")
    @ChineseDescription("字典编码")
    private String dictEncode;

    /**
     * 字典类型的编码
     */
    @TableField("dict_type_code")
    @ChineseDescription("字典类型的编码")
    private String dictTypeCode;

    /**
     * 字典简称
     */
    @TableField("dict_short_name")
    @ChineseDescription("字典简称")
    private String dictShortName;

    /**
     * 字典简称的编码
     */
    @TableField("dict_short_code")
    @ChineseDescription("字典简称的编码")
    private String dictShortCode;

    /**
     * 上级字典的id(如果没有上级字典id，则为-1)
     */
    @TableField("dict_parent_id")
    @ChineseDescription("上级字典的id(如果没有上级字典id，则为-1)")
    private Long dictParentId;

    /**
     * 状态：(1-启用,2-禁用),参考 StatusEnum
     */
    @TableField("status_flag")
    @ChineseDescription("状态：(1-启用,2-禁用)")
    private Integer statusFlag;

    /**
     * 排序，带小数点
     */
    @TableField("dict_sort")
    @ChineseDescription("排序")
    private BigDecimal dictSort;

    /**
     * 父id集合
     */
    @TableField("dict_pids")
    @ChineseDescription("父id集合")
    private String dictPids;

    /**
     * 是否删除，Y-被删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除，Y-被删除，N-未删除")
    private String delFlag;

    /**
     * 字典类型的名称
     */
    @ChineseDescription("字典类型的名称")
    private transient String dictTypeName;

    /**
     * 字典上级的名称（字典有上下级，字典类型没有上下级）
     */
    @ChineseDescription("字典上级的名称（字典有上下级，字典类型没有上下级）")
    private transient String parentName;

}
