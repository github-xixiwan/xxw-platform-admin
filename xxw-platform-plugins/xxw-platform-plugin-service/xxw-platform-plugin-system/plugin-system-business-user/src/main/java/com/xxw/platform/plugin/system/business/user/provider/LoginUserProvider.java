package com.xxw.platform.plugin.system.business.user.provider;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.loginuser.api.LoginUserRemoteApi;
import com.xxw.platform.plugin.auth.api.loginuser.pojo.LoginUserRequest;
import com.xxw.platform.plugin.auth.api.loginuser.pojo.SessionValidateResponse;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.user.SysUserExceptionEnum;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 图形验证码
 *
 * @author liaoxiting
 * @date 2021/1/15 15:11
 */
@RestController
public class LoginUserProvider implements LoginUserRemoteApi {

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private UserServiceApi userServiceApi;

    @Override
    public LoginUser getLoginUserByToken(@RequestBody LoginUserRequest loginUserRequest) {
        if (StrUtil.isBlank(loginUserRequest.getToken())) {
            throw new SystemModularException(SysUserExceptionEnum.TOKEN_EMPTY);
        }
        return sessionManagerApi.getSession(loginUserRequest.getToken());
    }

    @Override
    public SessionValidateResponse haveSession(@RequestParam("token") String token) {
        boolean validateFlag = sessionManagerApi.haveSession(token);
        return new SessionValidateResponse(validateFlag);
    }

    @Override
    public LoginUser getEffectiveLoginUser(@RequestBody LoginUser loginUser) {
        return userServiceApi.getEffectiveLoginUser(loginUser);
    }

}
