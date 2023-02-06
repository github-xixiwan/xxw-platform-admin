package com.xxw.platform.plugin.system.api.pojo.role.dto;

import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色资源关联
 *
 * @author liaoxiting
 * @date 2020/11/5 下午4:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleResourceDTO extends BaseEntity {

    /**
     * 主键
     */
    private Long roleResourceId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源的类型，1-业务类，2-系统类
     */
    private Integer resourceBizType;

}
