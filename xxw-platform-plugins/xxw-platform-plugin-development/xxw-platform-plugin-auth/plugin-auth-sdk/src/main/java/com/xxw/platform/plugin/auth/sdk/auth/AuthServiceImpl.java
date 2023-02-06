package com.xxw.platform.plugin.auth.sdk.auth;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.tenant.RequestTenantCodeHolder;
import com.xxw.platform.frame.common.util.HttpServletUtil;
import com.xxw.platform.plugin.auth.api.AuthServiceApi;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.SsoServerApi;
import com.xxw.platform.plugin.auth.api.TempSecretApi;
import com.xxw.platform.plugin.auth.api.constants.AuthConstants;
import com.xxw.platform.plugin.auth.api.constants.LoginCacheConstants;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.enums.SsoClientTypeEnum;
import com.xxw.platform.plugin.auth.api.exception.AuthException;
import com.xxw.platform.plugin.auth.api.exception.enums.AuthExceptionEnum;
import com.xxw.platform.plugin.auth.api.expander.AuthConfigExpander;
import com.xxw.platform.plugin.auth.api.password.PasswordStoredEncryptApi;
import com.xxw.platform.plugin.auth.api.password.PasswordTransferEncryptApi;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginRequest;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginResponse;
import com.xxw.platform.plugin.auth.api.pojo.auth.LoginWithTokenRequest;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.auth.api.pojo.sso.SsoLoginCodeRequest;
import com.xxw.platform.plugin.auth.api.pojo.sso.SsoProperties;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.ds.api.constants.DatasourceContainerConstants;
import com.xxw.platform.plugin.ds.api.context.CurrentDataSourceContext;
import com.xxw.platform.plugin.jwt.api.context.JwtContext;
import com.xxw.platform.plugin.jwt.api.exception.JwtException;
import com.xxw.platform.plugin.jwt.api.exception.enums.JwtExceptionEnum;
import com.xxw.platform.plugin.jwt.api.pojo.config.JwtConfig;
import com.xxw.platform.plugin.jwt.api.pojo.payload.DefaultJwtPayload;
import com.xxw.platform.plugin.jwt.sdk.JwtTokenOperator;
import com.xxw.platform.plugin.log.api.LoginLogServiceApi;
import com.xxw.platform.plugin.message.api.expander.WebSocketConfigExpander;
import com.xxw.platform.plugin.scanner.api.exception.ScannerException;
import com.xxw.platform.plugin.scanner.api.exception.enums.ScannerExceptionEnum;
import com.xxw.platform.plugin.scanner.api.holder.InitScanFlagHolder;
import com.xxw.platform.plugin.security.api.DragCaptchaApi;
import com.xxw.platform.plugin.security.api.ImageCaptchaApi;
import com.xxw.platform.plugin.security.api.expander.SecurityConfigExpander;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import com.xxw.platform.plugin.system.api.enums.UserStatusEnum;
import com.xxw.platform.plugin.system.api.pojo.user.UserLoginInfoDTO;
import com.xxw.platform.plugin.validator.api.exception.enums.ValidatorExceptionEnum;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 认证服务的实现
 *
 * @author liaoxiting
 * @date 2020/10/20 10:25
 */
@Service
public class AuthServiceImpl implements AuthServiceApi {

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private PasswordStoredEncryptApi passwordStoredEncryptApi;

    @Resource
    private PasswordTransferEncryptApi passwordTransferEncryptApi;

    @Resource
    private LoginLogServiceApi loginLogServiceApi;

    @Resource
    private ImageCaptchaApi captchaApi;

    @Resource
    private DragCaptchaApi dragCaptchaApi;

    @Resource
    private SsoProperties ssoProperties;

    @Resource(name = "loginErrorCountCacheApi")
    private CacheOperatorApi<Integer> loginErrorCountCacheApi;

    @Resource(name = "caClientTokenCacheApi")
    private CacheOperatorApi<String> caClientTokenCacheApi;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return loginAction(loginRequest, true, null);
    }

    @Override
    public LoginResponse loginWithUserName(String username) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccount(username);
        return loginAction(loginRequest, false, null);
    }

    @Override
    public LoginResponse loginWithUserNameAndCaToken(String username, String caToken) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccount(username);
        return loginAction(loginRequest, false, caToken);
    }

    @Override
    public LoginResponse LoginWithToken(LoginWithTokenRequest loginWithTokenRequest) {

        // 解析jwt token中的账号
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setJwtSecret(AuthConfigExpander.getSsoJwtSecret());
        jwtConfig.setExpiredSeconds(0L);

        // jwt工具类初始化
        JwtTokenOperator jwtTokenOperator = new JwtTokenOperator(jwtConfig);

        // 解析token中的用户信息
        Claims payload = null;
        try {
            payload = jwtTokenOperator.getJwtPayloadClaims(loginWithTokenRequest.getToken());
        } catch (Exception exception) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_PARSE_ERROR, exception.getMessage());
        }

        // 获取到用户信息
        Object userInfoEncryptString = payload.get("userInfo");
        if (ObjectUtil.isEmpty(userInfoEncryptString)) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_GET_USER_ERROR);
        }

        // 解密出用户账号和caToken（caToken用于校验用户是否在单点中心）
        String account = null;
        String caToken = null;
        try {
            AES aesUtil = SecureUtil.aes(Base64.decode(AuthConfigExpander.getSsoDataDecryptSecret()));
            String loginUserJson = aesUtil.decryptStr(userInfoEncryptString.toString(), CharsetUtil.CHARSET_UTF_8);
            JSONObject userInfoJsonObject = JSON.parseObject(loginUserJson);
            account = userInfoJsonObject.getString("account");
            caToken = userInfoJsonObject.getString("caToken");
        } catch (Exception exception) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_DECRYPT_USER_ERROR, exception.getMessage());
        }

        // 账号为空，抛出异常
        if (account == null) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_DECRYPT_USER_ERROR);
        }

        LoginResponse loginResponse = loginWithUserNameAndCaToken(account, caToken);

        // 存储单点token和生成的本地token的映射关系
        caClientTokenCacheApi.put(loginWithTokenRequest.getToken(), loginResponse.getToken());

        return loginResponse;
    }

    @Override
    public void logout() {
        String token = LoginContext.me().getToken();

        if (StrUtil.isNotEmpty(token)) {
            loginLogServiceApi.loginOutSuccess(LoginContext.me().getLoginUser().getUserId());
        }

        logoutWithToken(token);
        sessionManagerApi.destroySessionCookie();
    }

    @Override
    public void logoutWithToken(String token) {
        // 清除token缓存的用户信息
        sessionManagerApi.removeSession(token);
    }

    @Override
    public DefaultJwtPayload validateToken(String token) throws AuthException {
        try {
            // 1. 先校验jwt token本身是否有问题
            JwtContext.me().validateTokenWithException(token);

            // 2. 获取jwt的payload
            DefaultJwtPayload defaultPayload = JwtContext.me().getDefaultPayload(token);

            // 3. 如果是7天免登陆，则不校验session过期
            if (defaultPayload.getRememberMe()) {
                return defaultPayload;
            }

            // 4. 判断session里是否有这个token
            LoginUser session = sessionManagerApi.getSession(token);
            if (session == null) {
                throw new AuthException(AuthExceptionEnum.AUTH_EXPIRED_ERROR);
            }

            return defaultPayload;
        } catch (JwtException jwtException) {
            // jwt token本身过期的话，返回 AUTH_EXPIRED_ERROR
            if (JwtExceptionEnum.JWT_EXPIRED_ERROR.getErrorCode().equals(jwtException.getErrorCode())) {
                throw new AuthException(AuthExceptionEnum.AUTH_EXPIRED_ERROR);
            } else {
                // 其他情况为返回jwt解析错误
                throw new AuthException(AuthExceptionEnum.TOKEN_PARSE_ERROR);
            }
        } catch (io.jsonwebtoken.JwtException jwtSelfException) {
            // 其他jwt解析错误
            throw new AuthException(AuthExceptionEnum.TOKEN_PARSE_ERROR);
        }
    }

    @Override
    public void checkAuth(String token, String requestUrl) {

        // 1. 校验token是否传参
        if (StrUtil.isEmpty(token)) {
            throw new AuthException(AuthExceptionEnum.TOKEN_GET_ERROR);
        }

        // 2. 校验用户token是否正确，校验失败会抛出异常
        this.validateToken(token);

    }

    @Override
    public void cancelFreeze(LoginRequest loginRequest) {
        loginErrorCountCacheApi.remove(loginRequest.getAccount());
    }

    @Override
    public LoginUser createNewLoginInfo(String token, DefaultJwtPayload defaultJwtPayload) {

        // 获取用户的信息
        String account = defaultJwtPayload.getAccount();

        // 获取用户租户信息
        String tenantCode = defaultJwtPayload.getTenantCode();

        UserLoginInfoDTO userLoginInfo;
        try {
            // 如果有特定租户则进行切换操作
            if (StrUtil.isNotEmpty(tenantCode) && !DatasourceContainerConstants.MASTER_DATASOURCE_NAME.equals(tenantCode)) {
                CurrentDataSourceContext.setDataSourceName(RuleConstants.TENANT_DB_PREFIX + tenantCode);
            }

            // 获取用户信息
            userLoginInfo = userServiceApi.getUserLoginInfo(account);
            LoginUser loginUser = userLoginInfo.getLoginUser();
            loginUser.setTenantCode(tenantCode);

            // 创建用户会话信息
            sessionManagerApi.updateSession(token, loginUser);
        } finally {
            // 清除租户信息
            CurrentDataSourceContext.clearDataSourceName();
        }

        return userLoginInfo.getLoginUser();
    }

    /**
     * 登录的真正业务逻辑
     *
     * @param loginRequest     登录参数
     * @param validatePassword 是否校验密码，true-校验密码，false-不会校验密码
     * @param caToken          单点登录后服务端的token，一般为32位uuid
     * @author liaoxiting
     * @date 2020/10/21 16:59
     */
    private LoginResponse loginAction(LoginRequest loginRequest, Boolean validatePassword, String caToken) {
        // 1.参数为空校验
        if (validatePassword) {
            if (loginRequest == null || StrUtil.hasBlank(loginRequest.getAccount(), loginRequest.getPassword())) {
                throw new AuthException(AuthExceptionEnum.PARAM_EMPTY);
            }
        } else {
            if (loginRequest == null || StrUtil.hasBlank(loginRequest.getAccount())) {
                throw new AuthException(AuthExceptionEnum.ACCOUNT_IS_BLANK);
            }
        }

        // 1.2 判断账号是否密码重试次数过多被冻结
        Integer loginErrorCount = loginErrorCountCacheApi.get(loginRequest.getAccount());
        if (loginErrorCount != null && loginErrorCount >= LoginCacheConstants.MAX_ERROR_LOGIN_COUNT) {
            throw new AuthException(AuthExceptionEnum.LOGIN_LOCKED);
        }

        // 1.3 暂存多租户编码（v7.3.2增加，方便缓存调用过程中获取多租户的前缀）
        RequestTenantCodeHolder.setTenantCode(loginRequest.getTenantCode());

        // 2. 如果开启了验证码校验，则验证当前请求的验证码是否正确
        if (SecurityConfigExpander.getCaptchaOpen()) {
            String verKey = loginRequest.getVerKey();
            String verCode = loginRequest.getVerCode();

            if (StrUtil.isEmpty(verKey) || StrUtil.isEmpty(verCode)) {
                throw new AuthException(ValidatorExceptionEnum.CAPTCHA_EMPTY);
            }
            if (!captchaApi.validateCaptcha(verKey, verCode)) {
                throw new AuthException(ValidatorExceptionEnum.CAPTCHA_ERROR);
            }
        }

        // 2.1 验证拖拽验证码
        if (SecurityConfigExpander.getDragCaptchaOpen()) {
            String verKey = loginRequest.getVerKey();
            String verXLocationValue = loginRequest.getVerCode();

            if (StrUtil.isEmpty(verKey) || StrUtil.isEmpty(verXLocationValue)) {
                throw new AuthException(ValidatorExceptionEnum.CAPTCHA_EMPTY);
            }
            if (!dragCaptchaApi.validateCaptcha(verKey, Convert.toInt(verXLocationValue))) {
                throw new AuthException(ValidatorExceptionEnum.DRAG_CAPTCHA_ERROR);
            }
        }

        // 2.2 校验当前系统是否初始化资源完成，如果资源还没有初始化，提示用户请等一下再登录
        if (!InitScanFlagHolder.getFlag()) {
            throw new ScannerException(ScannerExceptionEnum.SYSTEM_RESOURCE_URL_NOT_INIT);
        }

        // 3. 解密密码的密文，需要sys_config相关配置打开
        if (loginRequest.getPassword() != null && AuthConfigExpander.getPasswordRsaValidateFlag()) {
            String decryptPassword = passwordTransferEncryptApi.decrypt(loginRequest.getPassword());
            loginRequest.setPassword(decryptPassword);
        }

        // 4. 如果开启了单点登录，并且CaToken没有值，走单点登录，获取loginCode
        if (ssoProperties.getOpenFlag() && StrUtil.isEmpty(caToken)) {
            if (SsoClientTypeEnum.client.name().equals(ssoProperties.getSsoClientType())) {
                // 调用单点的接口获取loginCode，远程接口校验用户级密码正确性。
                String remoteLoginCode = getRemoteLoginCode(loginRequest);
                return new LoginResponse(remoteLoginCode);
            } else {
                // 如果当前系统是单点服务端
                SsoServerApi ssoServerApi = SpringUtil.getBean(SsoServerApi.class);
                SsoLoginCodeRequest ssoLoginCodeRequest = new SsoLoginCodeRequest();
                ssoLoginCodeRequest.setAccount(loginRequest.getAccount());
                ssoLoginCodeRequest.setPassword(loginRequest.getPassword());
                String remoteLoginCode = ssoServerApi.createSsoLoginCode(ssoLoginCodeRequest);
                return new LoginResponse(remoteLoginCode);
            }
        }

        // 5. 获取用户密码的加密值和用户的状态
        UserLoginInfoDTO userValidateInfo = userServiceApi.getUserLoginInfo(loginRequest.getAccount());

        // 6. 校验用户密码是否正确
        validateUserPassword(validatePassword, loginErrorCount, loginRequest, userValidateInfo);

        // 7. 校验用户是否异常（不是正常状态）
        if (!UserStatusEnum.ENABLE.getCode().equals(userValidateInfo.getUserStatus())) {
            throw new AuthException(AuthExceptionEnum.USER_STATUS_ERROR, UserStatusEnum.getCodeMessage(userValidateInfo.getUserStatus()));
        }

        // 8. 获取LoginUser，用于用户的缓存
        LoginUser loginUser = userValidateInfo.getLoginUser();

        // 9. 生成用户的token
        DefaultJwtPayload defaultJwtPayload = new DefaultJwtPayload(loginUser.getUserId(), loginUser.getAccount(), loginRequest.getRememberMe(), caToken, loginRequest.getTenantCode());
        String jwtToken = JwtContext.me().generateTokenDefaultPayload(defaultJwtPayload);
        loginUser.setToken(jwtToken);

        // 如果包含租户编码，则放到loginUser中
        loginUser.setTenantCode(loginRequest.getTenantCode());

        synchronized (loginUser.getAccount().intern()) {

            // 9.1 获取ws-url 保存到用户信息中
            loginUser.setWsUrl(WebSocketConfigExpander.getWebSocketWsUrl());

            // 10. 缓存用户信息，创建会话
            sessionManagerApi.createSession(jwtToken, loginUser, loginRequest.getCreateCookie());

            // 11. 如果开启了单账号单端在线，则踢掉已经上线的该用户
            if (AuthConfigExpander.getSingleAccountLoginFlag()) {
                sessionManagerApi.removeSessionExcludeToken(jwtToken);
            }
        }

        // 12. 更新用户登录时间和ip
        String ip = HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest());
        userServiceApi.updateUserLoginInfo(loginUser.getUserId(), new Date(), ip);

        // 13.登录成功日志
        loginLogServiceApi.loginSuccess(loginUser.getUserId());

        // 13.1 登录成功，清空用户的错误登录次数
        this.cancelFreeze(loginRequest);

        // 14. 组装返回结果
        return new LoginResponse(loginUser, jwtToken, defaultJwtPayload.getExpirationDate());
    }

    /**
     * 调用远程接口获取loginCode
     *
     * @author liaoxiting
     * @date 2021/2/26 15:15
     */
    private String getRemoteLoginCode(LoginRequest loginRequest) {

        // 获取sso的地址
        String ssoUrl = AuthConfigExpander.getSsoUrl();

        // 请求sso服务获取loginCode
        HttpRequest httpRequest = HttpRequest.post(ssoUrl + AuthConstants.SYS_AUTH_SSO_GET_LOGIN_CODE);
        httpRequest.body(JSON.toJSONString(loginRequest));
        HttpResponse httpResponse = httpRequest.execute();

        // 获取返回结果的message
        String body = httpResponse.body();
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotBlank(body)) {
            jsonObject = JSON.parseObject(body);
        }

        // 如果返回结果是失败的
        if (httpResponse.getStatus() != 200) {
            String message = jsonObject.getString("message");
            throw new AuthException(AuthExceptionEnum.SSO_LOGIN_CODE_GET_ERROR, message);
        }

        // 从body中获取loginCode
        String loginCode = jsonObject.getString("data");

        // loginCode为空
        if (loginCode == null) {
            throw new AuthException(AuthExceptionEnum.SSO_LOGIN_CODE_GET_ERROR, "loginCode为空");
        }

        return loginCode;
    }

    /**
     * 用户密码校验，校验失败会报异常
     *
     * @author liaoxiting
     * @date 2022/3/26 14:16
     */
    private void validateUserPassword(Boolean validatePassword, Integer loginErrorCount, LoginRequest loginRequest, UserLoginInfoDTO userValidateInfo) {

        // 如果本次登录需要校验密码
        if (validatePassword) {
            Boolean checkResult = passwordStoredEncryptApi.checkPassword(loginRequest.getPassword(), userValidateInfo.getUserPasswordHexed());

            // 校验用户表密码是否正确，如果正确则直接返回
            if (checkResult) {
                return;
            }

            // 如果密码不正确则校验用户是否有临时秘钥
            TempSecretApi tempSecretApi = null;
            try {
                tempSecretApi = SpringUtil.getBean(TempSecretApi.class);
                if (tempSecretApi != null) {
                    String userTempSecretKey = tempSecretApi.getUserTempSecretKey(userValidateInfo.getLoginUser().getUserId());
                    // 如果用户有临时秘钥，则校验秘钥是否正确
                    if (StrUtil.isNotBlank(userTempSecretKey)) {
                        Boolean checkTempKeyResult = passwordStoredEncryptApi.checkPassword(loginRequest.getPassword(), userTempSecretKey);
                        if (checkTempKeyResult) {
                            return;
                        }
                    }
                }
            } catch (Exception ignored) {
            }

            // 临时秘钥校验完成，返回前端用户密码错误
            if (loginErrorCount == null) {
                loginErrorCount = 0;
            }

            loginErrorCountCacheApi.put(loginRequest.getAccount(), loginErrorCount + 1);

            throw new AuthException(AuthExceptionEnum.USERNAME_PASSWORD_ERROR);
        }
    }

}
