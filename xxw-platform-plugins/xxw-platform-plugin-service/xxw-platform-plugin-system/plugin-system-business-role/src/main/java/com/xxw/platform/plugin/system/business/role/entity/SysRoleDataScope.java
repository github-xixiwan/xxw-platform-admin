package com.xxw.platform.plugin.system.business.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统角色数据范围表
 *
 * @author liaoxiting
 * @date 2020/11/5 下午4:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role_data_scope")
public class SysRoleDataScope extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_data_scope_id", type = IdType.ASSIGN_ID)
    private Long roleDataScopeId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 机构id
     */
    @TableField("organization_id")
    private Long organizationId;

}
