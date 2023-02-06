package com.xxw.platform.plugin.system.api.pojo.role.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 角色数据范围
 *
 * @author liaoxiting
 * @date 2021/2/4 15:17
 */
@Data
public class SysRoleDataScopeRequest {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long roleDataScopeId;

    /**
     * 角色id
     */
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 机构id
     */
    @ChineseDescription("机构id")
    private Long organizationId;
}
