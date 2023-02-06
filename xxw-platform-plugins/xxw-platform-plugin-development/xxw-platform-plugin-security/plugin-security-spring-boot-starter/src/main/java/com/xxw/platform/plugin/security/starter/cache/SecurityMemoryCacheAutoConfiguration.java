package com.xxw.platform.plugin.security.starter.cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.cache.api.constants.CacheConstants;
import com.xxw.platform.plugin.security.sdk.bw.cache.BlackListMemoryCache;
import com.xxw.platform.plugin.security.sdk.bw.cache.WhiteListMemoryCache;
import com.xxw.platform.plugin.security.sdk.captcha.cache.CaptchaMemoryCache;
import com.xxw.platform.plugin.security.sdk.count.cache.CountValidateMemoryCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 安全模块，缓存的依赖
 *
 * @author liaoxiting
 * @date 2022/11/8 9:57
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.connection.RedisConnectionFactory")
public class SecurityMemoryCacheAutoConfiguration {

    /**
     * 验证码相关的缓存，内存缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 20:44
     */
    @Bean("captchaCache")
    public CacheOperatorApi<String> captchaMemoryCache() {
        // 验证码过期时间 120秒
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000 * 120);
        return new CaptchaMemoryCache(timedCache);
    }

    /**
     * 黑名单的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 21:24
     */
    @Bean("blackListCache")
    public CacheOperatorApi<String> blackListMemoryCache() {
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new BlackListMemoryCache(timedCache);
    }

    /**
     * 白名单的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 21:24
     */
    @Bean("whiteListCache")
    public CacheOperatorApi<String> whiteListMemoryCache() {
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new WhiteListMemoryCache(timedCache);
    }

    /**
     * 计数缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 21:24
     */
    @Bean("countValidateCache")
    public CacheOperatorApi<Long> countValidateMemoryCache() {
        TimedCache<String, Long> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new CountValidateMemoryCache(timedCache);
    }

}
