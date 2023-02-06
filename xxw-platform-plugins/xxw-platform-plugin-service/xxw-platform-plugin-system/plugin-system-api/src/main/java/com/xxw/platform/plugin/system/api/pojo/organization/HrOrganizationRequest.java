package com.xxw.platform.plugin.system.api.pojo.organization;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.expand.api.pojo.ExpandDataInfo;
import com.xxw.platform.plugin.validator.api.validators.status.StatusValue;
import com.xxw.platform.plugin.validator.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 系统组织机构表
 *
 * @author liaoxiting
 * @date 2020/11/04 11:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HrOrganizationRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class, updateStatus.class})
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id
     */
    @NotNull(message = "父id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父id")
    private Long orgParentId;

    /**
     * 父ids
     */
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "组织编码存在重复",
            groups = {add.class, edit.class},
            tableName = "hr_organization",
            columnName = "org_code",
            idFieldName = "org_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @NotNull(message = "状态不能为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 税号
     */
    @TableUniqueValue(
            message = "企业税号存在重复",
            groups = {add.class, edit.class},
            tableName = "hr_organization",
            columnName = "tax_no",
            idFieldName = "org_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 描述
     */
    @ChineseDescription("描述")
    private String orgRemark;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = roleBindOrgScope.class)
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 用户id（作为查询条件）
     */
    @NotNull(message = "用户id不能为空", groups = userBindOrgScope.class)
    @ChineseDescription("用户id（作为查询条件）")
    private Long userId;

    /**
     * 动态表单数据
     */
    @ChineseDescription("动态表单数据")
    private ExpandDataInfo expandDataInfo;

    /**
     * 限制从哪些组织机构中查询，传组织机构id集合
     */
    @ChineseDescription("限制从哪些组织机构中查询")
    private List<Long> orgIdLimit;

    /**
     * 组织机构id集合
     */
    @ChineseDescription("组织机构id集合")
    @NotEmpty(message = "组织机构id集合不能为空", groups = batchQuery.class)
    private List<Long> orgIdList;

    /**
     * 组织机构树zTree形式
     */
    public @interface roleBindOrgScope {
    }

    /**
     * 查询用户的数据范围
     */
    public @interface userBindOrgScope {
    }

    /**
     * 批量根据id查询
     */
    public @interface batchQuery {
    }

}
