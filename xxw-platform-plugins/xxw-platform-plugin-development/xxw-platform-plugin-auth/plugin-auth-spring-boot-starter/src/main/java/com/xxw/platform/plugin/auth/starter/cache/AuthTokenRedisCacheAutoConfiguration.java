package com.xxw.platform.plugin.auth.starter.cache;

import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.auth.sdk.cache.LoginErrorCountRedisCache;
import com.xxw.platform.plugin.auth.sdk.session.cache.catoken.RedisCaClientTokenCache;
import com.xxw.platform.plugin.auth.sdk.session.cache.logintoken.RedisLoginTokenCache;
import com.xxw.platform.plugin.auth.sdk.session.cache.loginuser.RedisLoginUserCache;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.cache.sdk.redis.util.CreateRedisTemplateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
/**
 * 认证和鉴权模块的自动配置
 *
 * @author liaoxiting
 * @date 2020/11/30 22:16
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
public class AuthTokenRedisCacheAutoConfiguration {

    /**
     * 登录用户的缓存，默认使用内存方式
     * <p>
     * 如需redis，可在项目创建一个名为 loginUserCache 的bean替代即可
     *
     * @author liaoxiting
     * @date 2021/1/31 21:04
     */
    @Bean
    public CacheOperatorApi<LoginUser> loginUserCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, LoginUser> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RedisLoginUserCache(redisTemplate);
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
    public CacheOperatorApi<Set<String>> allPlaceLoginTokenCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Set<String>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RedisLoginTokenCache(redisTemplate);
    }

    /**
     * 登录错误次数的缓存
     *
     * @author liaoxiting
     * @date 2022/3/15 17:25
     */
    @Bean
    public CacheOperatorApi<Integer> loginErrorCountCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new LoginErrorCountRedisCache(redisTemplate);
    }

    /**
     * CaClient单点登录token的缓存
     *
     * @author liaoxiting
     * @date 2022/5/20 11:52
     */
    @Bean
    public CacheOperatorApi<String> caClientTokenCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = CreateRedisTemplateUtil.createString(redisConnectionFactory);
        return new RedisCaClientTokenCache(redisTemplate);
    }

}
