package com.xxw.platform.plugin.auth.starter.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.auth.api.constants.LoginCacheConstants;
import com.xxw.platform.plugin.auth.api.expander.AuthConfigExpander;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.auth.sdk.cache.LoginErrorCountMemoryCache;
import com.xxw.platform.plugin.auth.sdk.session.cache.catoken.MemoryCaClientTokenCache;
import com.xxw.platform.plugin.auth.sdk.session.cache.logintoken.MemoryLoginTokenCache;
import com.xxw.platform.plugin.auth.sdk.session.cache.loginuser.MemoryLoginUserCache;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
/**
 * 认证和鉴权模块的自动配置
 *
 * @author liaoxiting
 * @date 2020/11/30 22:16
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.connection.RedisConnectionFactory")
public class AuthTokenMemoryCacheAutoConfiguration {

    /**
     * 登录用户的缓存，默认使用内存方式
     * <p>
     * 如需redis，可在项目创建一个名为 loginUserCache 的bean替代即可
     *
     * @author liaoxiting
     * @date 2021/1/31 21:04
     */
    @Bean
    public CacheOperatorApi<LoginUser> loginUserCache() {
        Long sessionExpiredSeconds = AuthConfigExpander.getSessionExpiredSeconds();
        TimedCache<String, LoginUser> loginUsers = CacheUtil.newTimedCache(1000L * sessionExpiredSeconds);
        return new MemoryLoginUserCache(loginUsers);
    }

    /**
     * 登录用户token的缓存，默认使用内存方式
     * <p>
     * 如需redis，可在项目创建一个名为 allPlaceLoginTokenCache 的bean替代即可
     *
     * @author liaoxiting
     * @date 2021/1/31 21:04
     */
    @Bean
    public CacheOperatorApi<Set<String>> allPlaceLoginTokenCache() {
        TimedCache<String, Set<String>> loginTokens = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new MemoryLoginTokenCache(loginTokens);
    }

    /**
     * 登录错误次数的缓存
     *
     * @author liaoxiting
     * @date 2022/3/15 17:25
     */
    @Bean
    public CacheOperatorApi<Integer> loginErrorCountCacheApi() {
        TimedCache<String, Integer> loginTimeCache = CacheUtil.newTimedCache(LoginCacheConstants.LOGIN_CACHE_TIMEOUT_SECONDS * 1000);
        return new LoginErrorCountMemoryCache(loginTimeCache);
    }

    /**
     * CaClient单点登录token的缓存
     *
     * @author liaoxiting
     * @date 2022/5/20 11:52
     */
    @Bean
    public CacheOperatorApi<String> caClientTokenCacheApi() {
        TimedCache<String, String> loginTimeCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new MemoryCaClientTokenCache(loginTimeCache);
    }

}
