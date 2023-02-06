package com.xxw.platform.plugin.system.business.user.controller;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.auth.api.AuthServiceApi;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginRequest;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginResponse;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginWithTokenRequest;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.login.CurrentUserInfoResponse;
import com.xxw.platform.plugin.system.api.pojo.login.ValidateTokenRequest;
import com.xxw.platform.plugin.system.api.pojo.login.v3.IndexUserInfoV3;
import com.xxw.platform.plugin.system.business.user.factory.UserLoginInfoFactory;
import com.xxw.platform.plugin.system.business.user.service.IndexUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 登录登出控制器
 *
 * @author liaoxiting
 * @date 2021/3/17 17:23
 */
@RestController
@Slf4j
@ApiResource(name = "登陆登出管理", resBizType = ResBizTypeEnum.SYSTEM)
public class LoginController {

    @Resource
    private AuthServiceApi authServiceApi;

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private IndexUserInfoService indexUserInfoService;

    @Resource(name = "caClientTokenCacheApi")
    private CacheOperatorApi<String> caClientTokenCacheApi;

    /**
     * 用户登陆
     *
     * @author liaoxiting
     * @date 2021/3/17 17:23
     */
    @PostResource(name = "登陆", path = "/login", requiredLogin = false, requiredPermission = false)
    public ResponseData<String> login(@RequestBody @Validated LoginRequest loginRequest) {
        loginRequest.setCreateCookie(true);
        LoginResponse loginResponse = authServiceApi.login(loginRequest);
        return new SuccessResponseData<>(loginResponse.getToken());
    }

    /**
     * 用户登陆(提供给分离版用的接口，不会写cookie)
     *
     * @author liaoxiting
     * @date 2021/3/17 17:23
     */
    @PostResource(name = "登陆（分离版）", path = "/loginApi", requiredLogin = false, requiredPermission = false)
    public ResponseData<LoginResponse> loginApi(@RequestBody @Validated LoginRequest loginRequest) {
        loginRequest.setCreateCookie(false);
        LoginResponse loginResponse = authServiceApi.login(loginRequest);
        return new SuccessResponseData<>(loginResponse);
    }

    /**
     * 基于token登录，适用于单点登录，将caToken请求过来，进行解析，并创建本系统可以识别的token
     *
     * @author liaoxiting
     * @date 2021/5/25 22:36
     */
    @PostResource(name = "适用于单点登录", path = "/loginWithToken", requiredLogin = false, requiredPermission = false)
    public ResponseData<String> loginWithToken(@RequestBody @Validated LoginWithTokenRequest loginWithTokenRequest) {
        LoginResponse loginResponse = authServiceApi.LoginWithToken(loginWithTokenRequest);
        return new SuccessResponseData<>(loginResponse.getToken());
    }

    /**
     * 单点退出，基于CaClientToken的单点退出
     *
     * @param caClientToken token是单点登录回调本系统时候的token
     * @author liaoxiting
     * @date 2021/3/17 17:24
     */
    @ApiResource(name = "单点退出", path = "/logoutByCaClientToken", resBizType = ResBizTypeEnum.SYSTEM,
            requiredLogin = false, requiredPermission = false, method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData<?> ssoLogout(@RequestParam("caClientToken") String caClientToken) {

        // 获取CaClientToken对应的本地用户
        String currentSystemToken = caClientTokenCacheApi.get(caClientToken);

        if (StrUtil.isNotBlank(currentSystemToken)) {
            // 移除本系统中token
            authServiceApi.logoutWithToken(currentSystemToken);
            caClientTokenCacheApi.remove(caClientToken);
        }

        return new SuccessResponseData<>();
    }

    /**
     * 用户登出
     *
     * @author liaoxiting
     * @date 2021/3/17 17:24
     */
    @ApiResource(name = "登出", path = "/logoutAction", resBizType = ResBizTypeEnum.SYSTEM,
            requiredPermission = false, method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData<?> logoutAction() {
        authServiceApi.logout();
        return new SuccessResponseData<>();
    }

    /**
     * 获取当前用户的用户信息
     *
     * @author liaoxiting
     * @date 2021/3/17 17:37
     */
    @GetResource(name = "获取当前用户的用户信息", path = "/getCurrentLoginUserInfo", requiredPermission = false)
    public ResponseData<CurrentUserInfoResponse> getCurrentLoginUserInfo() {
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 转化返回结果
        CurrentUserInfoResponse currentUserInfoResponse = UserLoginInfoFactory.parseUserInfo(loginUser);

        return new SuccessResponseData<>(currentUserInfoResponse);
    }

    /**
     * 校验token是否正确
     *
     * @author liaoxiting
     * @date 2021/6/18 15:26
     */
    @PostResource(name = "校验token是否正确", path = "/validateToken", requiredPermission = false, requiredLogin = false)
    public ResponseData<Boolean> validateToken(@RequestBody @Valid ValidateTokenRequest validateTokenRequest) {
        boolean haveSessionFlag = sessionManagerApi.haveSession(validateTokenRequest.getToken());
        return new SuccessResponseData<>(haveSessionFlag);
    }

    /**
     * 取消帐号冻结
     *
     * @author liaoxiting
     * @date 2022/1/22 16:40
     */
    @PostResource(name = "取消帐号冻结", path = "/cancelFreeze")
    public ResponseData<?> cancelFreeze(@RequestBody @Validated(LoginRequest.cancelFreeze.class) LoginRequest loginRequest) {
        authServiceApi.cancelFreeze(loginRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 新版Antdv3版本的用户信息获取
     *
     * @author liaoxiting
     * @date 2022/4/8 15:31
     */
    @GetResource(name = "新版Antdv3版本的用户信息获取", path = "/v3/userInfo", requiredPermission = false)
    public ResponseData<IndexUserInfoV3> userInfoV3(@RequestParam(value = "menuFrontType", required = false) Integer menuFrontType,
                                                    @RequestParam(value = "devopsFlag", required = false) Boolean devopsFlag) {
        return new SuccessResponseData<>(indexUserInfoService.userInfoV3(menuFrontType, devopsFlag));
    }

}
