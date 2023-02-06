package com.xxw.platform.plugin.auth.api.pojo.auth;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 登录的请求参数
 *
 * @author liaoxiting
 * @date 2020/10/19 14:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequest extends BaseRequest {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    @NotBlank(message = "账号不能为空", groups = cancelFreeze.class)
    @ChineseDescription("账号")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ChineseDescription("密码")
    private String password;

    /**
     * 记录会话，7天免登录
     */
    @ChineseDescription("记录会话，7天免登录")
    private Boolean rememberMe = false;

    /**
     * 验证码图形对应的缓存key
     */
    @ChineseDescription("验证码图形对应的缓存key")
    private String verKey;

    /**
     * 用户输入的验证码的值
     */
    @ChineseDescription("用户输入的验证码的值")
    private String verCode;

    /**
     * 是否写入cookie会话信息
     */
    @ChineseDescription("是否写入cookie会话信息")
    private Boolean createCookie = false;

    /**
     * 租户编码
     */
    @ChineseDescription("租户编码")
    private String tenantCode;

    /**
     * 取消账号冻结
     */
    public @interface cancelFreeze {
    }

}
