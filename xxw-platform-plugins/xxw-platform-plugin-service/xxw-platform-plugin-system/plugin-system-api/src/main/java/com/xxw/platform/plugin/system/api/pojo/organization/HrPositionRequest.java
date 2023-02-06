package com.xxw.platform.plugin.system.api.pojo.organization;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.api.validators.status.StatusValue;
import com.xxw.platform.plugin.validator.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 系统职位表
 *
 * @author liaoxiting
 * @date 2020/11/04 11:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HrPositionRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, detail.class, delete.class})
    @ChineseDescription("主键")
    private Long positionId;

    /**
     * 职位名称
     */
    @NotBlank(message = "职位名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("职位名称")
    private String positionName;

    /**
     * 职位编码
     */
    @NotBlank(message = "职位编码不能为空", groups = {add.class})
    @TableUniqueValue(
            message = "职位编码存在重复",
            groups = {add.class, edit.class},
            tableName = "hr_position",
            columnName = "position_code",
            idFieldName = "position_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("职位编码")
    private String positionCode;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal positionSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @NotNull(message = "状态不能为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 备注
     */
    @ChineseDescription("备注")
    private String positionRemark;

    /**
     * 职位id集合（用在批量操作）
     */
    @NotNull(message = "职位id集合不能为空", groups = {batchDelete.class, batchQuery.class})
    @ChineseDescription("职位id集合（用在批量操作）")
    private List<Long> positionIds;

    /**
     * 参数校验分组：批量查询
     */
    public @interface batchQuery {
    }

}
