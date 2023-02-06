package com.xxw.platform.plugin.auth.api.pojo.auth;

import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 单点获取到的token
 *
 * @author liaoxiting
 * @date 2021/5/25 22:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginWithTokenRequest extends BaseRequest {

    /**
     * 从单点服务获取到的token
     */
    @NotBlank(message = "token不能为空")
    private String token;

}
