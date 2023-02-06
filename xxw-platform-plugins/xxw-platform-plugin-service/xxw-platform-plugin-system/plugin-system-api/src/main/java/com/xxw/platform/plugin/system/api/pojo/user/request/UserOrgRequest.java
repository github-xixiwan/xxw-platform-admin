package com.xxw.platform.plugin.system.api.pojo.user.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 用户组织机构关联
 *
 * @author liaoxiting
 * @date 2021/2/3 10:51
 */
@Data
public class UserOrgRequest {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long userOrgId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 所属机构id
     */
    @ChineseDescription("所属机构id")
    private Long orgId;

    /**
     * 职位id
     */
    @ChineseDescription("职位id")
    private Long positionId;
}
