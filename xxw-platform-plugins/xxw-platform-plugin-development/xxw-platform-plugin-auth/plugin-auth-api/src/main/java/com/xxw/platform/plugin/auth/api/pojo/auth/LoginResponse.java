package com.xxw.platform.plugin.auth.api.pojo.auth;

import cn.hutool.core.bean.BeanUtil;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import lombok.Data;

/**
 * 登录操作的响应结果
 *
 * @author liaoxiting
 * @date 2020/10/19 14:17
 */
@Data
public class LoginResponse {

    /**
     * 登录人的信息
     */
    @ChineseDescription("登录人的信息")
    private LoginUser loginUser;

    /**
     * 登录人的token
     */
    @ChineseDescription("登录人的token")
    private String token;

    /**
     * 到期时间
     */
    @ChineseDescription("到期时间")
    private Long expireAt;

    /**
     * 使用单点登录
     */
    @ChineseDescription("使用单点登录")
    private Boolean ssoLogin;

    /**
     * 单点登录的loginCode
     */
    @ChineseDescription("单点登录的loginCode")
    private String ssoLoginCode;

    /**
     * 用于普通登录的组装
     *
     * @author liaoxiting
     * @date 2021/5/25 22:31
     */
    public LoginResponse(LoginUser loginUser, String token, Long expireAt) {
        this.loginUser = uselessFilter(loginUser);
        this.token = token;
        this.expireAt = expireAt;
    }

    /**
     * 用于单点登录，返回用户loginCode
     *
     * @author liaoxiting
     * @date 2021/5/25 22:31
     */
    public LoginResponse(String loginCode) {
        this.ssoLogin = true;
        this.ssoLoginCode = loginCode;
    }

    /**
     * 过滤无用的用户信息返回给登录用户
     *
     * @author liaoxiting
     * @date 2021/7/13 11:23
     */
    private LoginUser uselessFilter(LoginUser loginUser) {
        LoginUser tempUser = new LoginUser();
        BeanUtil.copyProperties(loginUser, tempUser);

        // 过滤一些内容
        tempUser.setDataScopeTypeEnums(null);
        tempUser.setResourceUrls(null);
        tempUser.setToken(null);
        return tempUser;
    }

}
