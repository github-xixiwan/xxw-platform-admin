package com.xxw.platform.plugin.system.api.pojo.user.request;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 用户数据范围
 *
 * @author liaoxiting
 * @date 2021/2/3 15:35
 */
@Data
public class UserDataScopeRequest {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long userDataScopeId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 机构id
     */
    @ChineseDescription("机构id")
    private Long orgId;
}
