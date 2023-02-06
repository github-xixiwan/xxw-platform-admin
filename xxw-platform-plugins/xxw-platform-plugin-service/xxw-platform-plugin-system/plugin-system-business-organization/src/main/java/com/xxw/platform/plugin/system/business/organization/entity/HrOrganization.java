package com.xxw.platform.plugin.system.business.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 系统组织机构表
 *
 * @author liaoxiting
 * @date 2020/11/04 11:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hr_organization")
public class HrOrganization extends BaseEntity {

    /**
     * 主键
     */
    @TableId("org_id")
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id，一级节点父id是0
     */
    @TableField("org_parent_id")
    @ChineseDescription("父id，一级节点父id是0")
    private Long orgParentId;

    /**
     * 父ids
     */
    @TableField("org_pids")
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织编码
     */
    @TableField("org_code")
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @TableField("org_sort")
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField(value = "status_flag",fill = FieldFill.INSERT)
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @TableField(value = "org_type")
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 税号
     */
    @TableField(value = "tax_no")
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 组织机构描述
     */
    @TableField("org_remark")
    @ChineseDescription("组织机构描述")
    private String orgRemark;

    /**
     * 删除标记（Y-已删除，N-未删除）
     */
    @TableField(value = "del_flag",fill = FieldFill.INSERT)
    @ChineseDescription("删除标记（Y-已删除，N-未删除）")
    private String delFlag;

}
