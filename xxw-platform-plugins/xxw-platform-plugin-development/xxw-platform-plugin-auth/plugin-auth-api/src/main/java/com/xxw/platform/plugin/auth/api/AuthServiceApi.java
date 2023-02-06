package com.xxw.platform.plugin.auth.api;

import com.xxw.platform.plugin.auth.api.exception.AuthException;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginRequest;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginResponse;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginWithTokenRequest;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.jwt.api.pojo.payload.DefaultJwtPayload;

/**
 * 认证服务的接口，包括基本的登录退出操作和校验token等操作
 *
 * @author liaoxiting
 * @date 2020/10/26 14:41
 */
public interface AuthServiceApi {

    /**
     * 常规登录操作
     *
     * @param loginRequest 登录的请求
     * @return token 一般为jwt token
     * @author liaoxiting
     * @date 2020/10/26 14:41
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 登录（直接用账号登录），一般用在第三方登录
     *
     * @param username 账号
     * @author liaoxiting
     * @date 2020/10/26 14:40
     */
    LoginResponse loginWithUserName(String username);

    /**
     * 登录（通过账号和sso后的token），一般用在单点登录
     *
     * @param username 账号
     * @param caToken  sso登录成功后的会话
     * @author liaoxiting
     * @date 2021/5/25 22:44
     */
    LoginResponse loginWithUserNameAndCaToken(String username, String caToken);

    /**
     * 通过token进行登录，一般用在单点登录服务
     *
     * @param loginWithTokenRequest 请求
     * @author liaoxiting
     * @date 2021/5/25 22:44
     */
    LoginResponse LoginWithToken(LoginWithTokenRequest loginWithTokenRequest);

    /**
     * 当前登录人退出登录
     *
     * @author liaoxiting
     * @date 2020/10/19 14:16
     */
    void logout();

    /**
     * 移除某个token，也就是退出某个用户
     *
     * @param token 某个用户的登录token
     * @author liaoxiting
     * @date 2020/10/19 14:16
     */
    void logoutWithToken(String token);

    /**
     * 校验jwt token的正确性，调用jwt工具类相关方法校验
     * <p>
     * 结果有三种，第一是jwt过期了，第二是用户随便写的错误token，第三种是token正确，token正确不会抛出异常
     *
     * @param token 某个用户的登录token
     * @return token解析出的用户基本信息
     * @throws AuthException 认证异常，如果token错误或过期，会有相关的异常抛出
     * @author liaoxiting
     * @date 2020/10/19 14:16
     */
    DefaultJwtPayload validateToken(String token) throws AuthException;

    /**
     * 校验用户是否认证通过，认证是校验token的过程，校验失败会抛出异常
     *
     * @param token      用户登陆的token
     * @param requestUrl 被校验的url
     * @author liaoxiting
     * @date 2020/10/22 16:03
     */
    void checkAuth(String token, String requestUrl);

    /**
     * 取消冻结帐号
     *
     * @author liaoxiting
     * @date 2022/1/22 16:37
     */
    void cancelFreeze(LoginRequest loginRequest);

    /**
     * 为指定token创建新的登录信息
     *
     * @param token             用户旧的token
     * @param defaultJwtPayload jwt的payload信息
     * @return 新的当前登录用户
     * @author liaoxiting
     * @date 2022/10/17 0:04
     */
    LoginUser createNewLoginInfo(String token, DefaultJwtPayload defaultJwtPayload);

}
