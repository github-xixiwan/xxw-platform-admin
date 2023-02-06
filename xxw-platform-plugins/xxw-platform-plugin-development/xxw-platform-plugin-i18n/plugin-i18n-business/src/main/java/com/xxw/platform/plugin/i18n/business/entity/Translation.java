package com.xxw.platform.plugin.i18n.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 多语言表
 *
 * @author liaoxiting
 * @date 2021/1/24 19:12
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_translation")
@Data
public class Translation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "tran_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long tranId;

    /**
     * 编码
     */
    @TableField("tran_code")
    @ChineseDescription("编码")
    private String tranCode;

    /**
     * 多语言条例名称
     */
    @TableField("tran_name")
    @ChineseDescription("多语言条例名称")
    private String tranName;

    /**
     * 语种字典
     */
    @TableField("tran_language_code")
    @ChineseDescription("语种字典")
    private String tranLanguageCode;

    /**
     * 翻译的值
     */
    @TableField("tran_value")
    @ChineseDescription("翻译的值")
    private String tranValue;

}
