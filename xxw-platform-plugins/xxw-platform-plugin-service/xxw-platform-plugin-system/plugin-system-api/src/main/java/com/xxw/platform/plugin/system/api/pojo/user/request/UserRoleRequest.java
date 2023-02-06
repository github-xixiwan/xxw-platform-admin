package com.xxw.platform.plugin.system.api.pojo.user.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 用户角色
 *
 * @author liaoxiting
 * @date 2021/2/3 14:53
 */
@Data
public class UserRoleRequest {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long userRoleId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 角色id
     */
    @ChineseDescription("角色id")
    private Long roleId;

}
