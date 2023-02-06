package com.xxw.platform.plugin.auth.sdk.auth;

import com.xxw.platform.plugin.auth.api.LoginUserApi;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.context.LoginUserHolder;
import com.xxw.platform.plugin.auth.api.exception.AuthException;
import com.xxw.platform.plugin.auth.api.exception.enums.AuthExceptionEnum;
import com.xxw.platform.plugin.auth.api.loginuser.CommonLoginUserUtil;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 当前登陆用户的接口实现
 *
 * @author liaoxiting
 * @date 2020/10/21 18:09
 */
@Service
public class LoginUserImpl implements LoginUserApi {

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private UserServiceApi userServiceApi;

    @Override
    public String getToken() {
        return CommonLoginUserUtil.getToken();
    }

    @Override
    public LoginUser getLoginUser() throws AuthException {

        // 先从ThreadLocal中获取
        LoginUser currentUser = LoginUserHolder.get();
        if (currentUser != null) {
            return currentUser;
        }

        // 获取用户的token
        String token = getToken();

        // 获取session中该token对应的用户
        LoginUser session = sessionManagerApi.getSession(token);

        // session为空抛出异常
        if (session == null) {
            throw new AuthException(AuthExceptionEnum.AUTH_EXPIRED_ERROR);
        }

        return session;
    }

    @Override
    public LoginUser getLoginUserNullable() {

        // 先从ThreadLocal中获取
        LoginUser currentUser = LoginUserHolder.get();
        if (currentUser != null) {
            return currentUser;
        }

        // 获取用户的token
        String token = null;
        try {
            token = getToken();
        } catch (Exception e) {
            return null;
        }

        // 获取session中该token对应的用户
        return sessionManagerApi.getSession(token);

    }

    @Override
    public boolean getSuperAdminFlag() {
        LoginUser loginUser = getLoginUser();
        return loginUser.getSuperAdmin();
    }

    @Override
    public boolean hasLogin() {

        // 获取用户的token
        String token = null;
        try {
            token = getToken();
        } catch (Exception e) {
            return false;
        }

        // 获取是否在会话中有
        return sessionManagerApi.haveSession(token);
    }

    @Override
    public boolean haveButton(String buttonCode) {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getButtonCodes() == null) {
            return false;
        } else {
            return loginUser.getButtonCodes().contains(buttonCode);
        }
    }

}
