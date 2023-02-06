package com.xxw.platform.plugin.dict.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 字典类型表，一个字典类型下有多个字典
 *
 * @author liaoxiting
 * @date 2020/10/30 10:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict_type")
public class SysDictType extends BaseEntity {

    /**
     * 字典类型id
     */
    @TableId(value = "dict_type_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("字典类型id")
    private Long dictTypeId;

    /**
     * 字典类型： 1-业务类型，2-系统类型，参考 DictTypeClassEnum
     */
    @TableField("dict_type_class")
    @ChineseDescription("字典类型")
    private Integer dictTypeClass;

    /**
     * 字典类型编码
     */
    @TableField("dict_type_code")
    @ChineseDescription("字典类型编码")
    private String dictTypeCode;

    /**
     * 字典类型业务编码
     */
    @TableField("dict_type_bus_code")
    @ChineseDescription("字典类型业务编码")
    private String dictTypeBusCode;

    /**
     * 字典类型名称
     */
    @TableField("dict_type_name")
    @ChineseDescription("字典类型名称")
    private String dictTypeName;

    /**
     * 字典类型名称拼音
     */
    @TableField("dict_type_name_pinyin")
    @ChineseDescription("字典类型名词拼音")
    private String dictTypeNamePinyin;

    /**
     * 字典类型描述
     */
    @TableField("dict_type_desc")
    @ChineseDescription("字典类型描述")
    private String dictTypeDesc;

    /**
     * 字典类型的状态：1-启用，2-禁用，参考 StatusEnum
     */
    @TableField("status_flag")
    @ChineseDescription("字典类型的状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 删除标记 Y-已删除，N-未删除，参考 YesOrNotEnum
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("删除标记")
    private String delFlag;

    /**
     * 排序，带小数点
     */
    @TableField(value = "dict_type_sort")
    @ChineseDescription("排序")
    private BigDecimal dictTypeSort;

}
