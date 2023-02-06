package com.xxw.platform.plugin.system.api.pojo.user;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 管理员用户的响应
 *
 * @author liaoxiting
 * @date 2022/9/30 11:25
 */
@Data
public class SysUserAdminDTO {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long userId;

    /**
     * 角色id
     */
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 姓名
     */
    @ChineseDescription("姓名")
    private String realName;

}
