package com.xxw.platform.plugin.system.api.pojo.role.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 角色按钮
 *
 * @author liaoxiting
 * @date 2021/1/9 17:33
 */
@Data
public class SysRoleMenuButtonRequest {

    /**
     * 按钮id
     */
    @ChineseDescription("按钮id")
    private Long buttonId;

    /**
     * 按钮编码
     */
    @ChineseDescription("按钮编码")
    private String buttonCode;

}
