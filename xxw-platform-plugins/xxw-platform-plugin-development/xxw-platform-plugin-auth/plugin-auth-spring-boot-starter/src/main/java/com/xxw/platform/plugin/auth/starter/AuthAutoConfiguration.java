package com.xxw.platform.plugin.auth.starter;

import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.cookie.SessionCookieCreator;
import com.xxw.platform.plugin.auth.api.expander.AuthConfigExpander;
import com.xxw.platform.plugin.auth.api.password.PasswordStoredEncryptApi;
import com.xxw.platform.plugin.auth.api.password.PasswordTransferEncryptApi;
import com.xxw.platform.plugin.auth.api.pojo.auth.PwdRsaSecretProperties;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.auth.sdk.password.BcryptPasswordStoredEncrypt;
import com.xxw.platform.plugin.auth.sdk.password.RsaPasswordTransferEncrypt;
import com.xxw.platform.plugin.auth.sdk.session.DefaultSessionManager;
import com.xxw.platform.plugin.auth.sdk.session.cookie.DefaultSessionCookieCreator;
import com.xxw.platform.plugin.auth.sdk.session.timer.ClearInvalidLoginUserCacheTimer;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.jwt.api.JwtApi;
import com.xxw.platform.plugin.jwt.api.pojo.config.JwtConfig;
import com.xxw.platform.plugin.jwt.sdk.JwtTokenOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Set;
/**
 * 认证和鉴权模块的自动配置
 *
 * @author liaoxiting
 * @date 2020/11/30 22:16
 */
@Configuration
public class AuthAutoConfiguration {

    @Resource
    private PwdRsaSecretProperties pwdRsaSecretProperties;

    /**
     * jwt操作工具类的配置
     *
     * @author liaoxiting
     * @date 2020/12/1 14:40
     */
    @Bean
    @ConditionalOnMissingBean(JwtApi.class)
    public JwtApi jwtApi() {

        JwtConfig jwtConfig = new JwtConfig();

        // 从系统配置表中读取配置
        jwtConfig.setJwtSecret(AuthConfigExpander.getAuthJwtSecret());
        jwtConfig.setExpiredSeconds(AuthConfigExpander.getAuthJwtTimeoutSeconds());

        return new JwtTokenOperator(jwtConfig);
    }

    /**
     * Bcrypt方式的密码加密
     *
     * @author liaoxiting
     * @date 2020/12/21 17:45
     */
    @Bean
    @ConditionalOnMissingBean(PasswordStoredEncryptApi.class)
    public PasswordStoredEncryptApi passwordStoredEncryptApi() {
        return new BcryptPasswordStoredEncrypt();
    }

    /**
     * RSA方式密码加密传输
     *
     * @author liaoxiting
     * @date 2020/12/21 17:45
     */
    @Bean
    @ConditionalOnMissingBean(PasswordTransferEncryptApi.class)
    public PasswordTransferEncryptApi passwordTransferEncryptApi() {
        return new RsaPasswordTransferEncrypt(pwdRsaSecretProperties.getPublicKey(), pwdRsaSecretProperties.getPrivateKey());
    }

    /**
     * session cookie的创建
     *
     * @author liaoxiting
     * @date 2020/12/27 15:48
     */
    @Bean
    @ConditionalOnMissingBean(SessionCookieCreator.class)
    public SessionCookieCreator sessionCookieCreator() {
        return new DefaultSessionCookieCreator();
    }

    /**
     * 默认的session缓存为内存缓存，方便启动
     * <p>
     * 如需替换请在项目中增加一个SessionManagerApi Bean即可
     *
     * @author liaoxiting
     * @date 2020/11/30 22:17
     */
    @Bean
    @ConditionalOnMissingBean(SessionManagerApi.class)
    public SessionManagerApi sessionManagerApi(CacheOperatorApi<LoginUser> loginUserCache, CacheOperatorApi<Set<String>> allPlaceLoginTokenCache) {
        Long sessionExpiredSeconds = AuthConfigExpander.getSessionExpiredSeconds();
        return new DefaultSessionManager(loginUserCache, allPlaceLoginTokenCache, sessionExpiredSeconds, sessionCookieCreator());
    }

    /**
     * 清空无用登录用户缓存的定时任务
     *
     * @author liaoxiting
     * @date 2021/3/30 11:32
     */
    @Bean
    @ConditionalOnMissingBean(ClearInvalidLoginUserCacheTimer.class)
    public ClearInvalidLoginUserCacheTimer clearInvalidLoginUserCacheTimer(CacheOperatorApi<LoginUser> loginUserCache, CacheOperatorApi<Set<String>> allPlaceLoginTokenCache) {
        return new ClearInvalidLoginUserCacheTimer(loginUserCache, allPlaceLoginTokenCache);
    }

}
