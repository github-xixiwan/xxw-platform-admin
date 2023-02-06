package com.xxw.platform.plugin.dict.business.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.api.validators.status.StatusValue;
import com.xxw.platform.plugin.validator.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 字典类型，请求参数封装
 *
 * @author liaoxiting
 * @date 2020/10/30 11:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictTypeRequest extends BaseRequest {

    /**
     * 字典类型id
     */
    @NotNull(message = "id不能为空", groups = {edit.class, delete.class, detail.class, updateStatus.class})
    @ChineseDescription("字典类型id")
    private Long dictTypeId;

    /**
     * 字典类型： 1-业务类型，2-系统类型，参考 DictTypeClassEnum
     */
    @NotNull(message = "字典类型不能为空", groups = {add.class, edit.class})
    @ChineseDescription("字典类型")
    private Integer dictTypeClass;

    /**
     * 字典类型业务编码
     */
    @ChineseDescription("字典类型业务编码")
    private String dictTypeBusCode;

    /**
     * 字典类型编码
     */
    @NotBlank(message = "字典类型编码不能为空", groups = {add.class, edit.class, validateCode.class})
    @TableUniqueValue(
            message = "字典类型编码存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_dict_type",
            columnName = "dict_type_code",
            idFieldName = "dict_type_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("字典类型编码")
    private String dictTypeCode;

    /**
     * 字典类型名称
     */
    @NotBlank(message = "字典类型名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("字典类型名称")
    private String dictTypeName;

    /**
     * 字典类型名词拼音
     */
    @ChineseDescription("字典类型名词拼音")
    private String dictTypeNamePinYin;

    /**
     * 字典类型描述
     */
    @ChineseDescription("字典类型描述")
    private String dictTypeDesc;

    /**
     * 字典类型的状态：1-启用，2-禁用，参考 StatusEnum
     */
    @NotNull(message = "状态不能为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    @ChineseDescription("字典类型的状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 排序，带小数
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal dictTypeSort;

    /**
     * 参数校验分组：校验code是否可用
     */
    public @interface validateCode {

    }

}
