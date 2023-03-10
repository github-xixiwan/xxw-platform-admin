package com.xxw.platform.plugin.dict.business.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.api.validators.status.StatusValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 字典请求参数封装
 *
 * @author liaoxiting
 * @date 2020/10/30 11:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictRequest extends BaseRequest {

    /**
     * 字典id
     */
    @NotNull(message = "id不能为空", groups = {edit.class, delete.class, detail.class, updateStatus.class})
    @ChineseDescription("字典id")
    private Long dictId;

    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空", groups = {add.class, edit.class, validateAvailable.class})
    @ChineseDescription("字典编码")
    private String dictCode;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("字典名称")
    private String dictName;

    /**
     * 字典名称拼音
     */
    @ChineseDescription("字典名称拼音")
    private String dictNamePinYin;

    /**
     * 字典编码
     */
    @ChineseDescription("字典编码")
    private String dictEncode;

    /**
     * 字典类型编码
     */
    @NotBlank(message = "字典类型编码不能为空", groups = {add.class, edit.class, treeList.class, dictZTree.class})
    @ChineseDescription("字典类型编码")
    private String dictTypeCode;

    /**
     * 字典简称
     */
    @ChineseDescription("字典简称")
    private String dictShortName;

    /**
     * 字典简称的编码
     */
    @ChineseDescription("字典简称的编码")
    private String dictShortCode;

    /**
     * 上级字典的id
     * <p>
     * 字典列表是可以有树形结构的，但是字典类型没有树形结构
     * <p>
     * 如果没有上级字典id，则为-1
     */
    @ChineseDescription("上级字典的id")
    private Long dictParentId;

    /**
     * 状态(1:启用,2:禁用)，参考 StatusEnum
     */
    @NotNull(message = "状态不能为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    @ChineseDescription("状态")
    private Integer statusFlag;

    /**
     * 排序，带小数点
     */
    @ChineseDescription("排序")
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    private BigDecimal dictSort;

    /**
     * 所有的父级id,逗号分隔
     */
    @ChineseDescription("所有的父级id")
    private String dictPids;

    /**
     * 字典类型id，用在作为查询条件
     */
    @ChineseDescription("字典类型id")
    private Long dictTypeId;

    /**
     * 获取树形列表
     */
    public @interface treeList {

    }

    /**
     * 校验编码是否重复
     */
    public @interface validateAvailable {

    }

    /**
     * 校验ztree必备参数
     */
    public @interface dictZTree {

    }

}
