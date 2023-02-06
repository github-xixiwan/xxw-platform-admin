package com.xxw.platform.plugin.system.business.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色资源关联
 *
 * @author liaoxiting
 * @date 2020/11/5 下午4:30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role_resource")
public class SysRoleResource extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_resource_id", type = IdType.ASSIGN_ID)
    private Long roleResourceId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 资源编码
     */
    @TableField("resource_code")
    private String resourceCode;

    /**
     * 资源的业务类型：1-业务类型，2-系统类型
     */
    @TableField("resource_biz_type")
    private Integer resourceBizType;

}
