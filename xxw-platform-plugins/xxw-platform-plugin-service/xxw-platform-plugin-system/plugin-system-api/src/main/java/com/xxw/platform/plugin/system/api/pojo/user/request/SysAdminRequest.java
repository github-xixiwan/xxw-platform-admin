package com.xxw.platform.plugin.system.api.pojo.user.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户管理员相关的请求
 *
 * @author liaoxiting
 * @date 2022/9/30 11:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAdminRequest extends BaseRequest {

    /**
     * 用户id集合
     */
    @ChineseDescription("用户id集合")
    @NotEmpty(message = "用户id集合不能为空", groups = add.class)
    private List<Long> userIdList;

    /**
     * 单个用户id
     */
    @ChineseDescription("用户id")
    @NotNull(message = "用户id不能为空", groups = delete.class)
    private Long userId;

}
