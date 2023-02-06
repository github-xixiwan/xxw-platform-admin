package com.xxw.platform.plugin.security.starter;

import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.security.api.BlackListApi;
import com.xxw.platform.plugin.security.api.CountValidatorApi;
import com.xxw.platform.plugin.security.api.WhiteListApi;
import com.xxw.platform.plugin.security.sdk.bw.BlackListService;
import com.xxw.platform.plugin.security.sdk.bw.WhiteListService;
import com.xxw.platform.plugin.security.sdk.count.DefaultCountValidator;
import com.xxw.platform.plugin.security.starter.cache.SecurityMemoryCacheAutoConfiguration;
import com.xxw.platform.plugin.security.starter.cache.SecurityRedisCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
/**
 * 计数器和黑白名单自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 21:44
 */
@Configuration
@AutoConfigureAfter({SecurityMemoryCacheAutoConfiguration.class, SecurityRedisCacheAutoConfiguration.class})
public class CounterAutoConfiguration {

    @Resource(name = "blackListCache")
    private CacheOperatorApi<String> blackListCache;

    @Resource(name = "whiteListCache")
    private CacheOperatorApi<String> whiteListCache;

    @Resource(name = "countValidateCache")
    private CacheOperatorApi<Long> countValidateCache;

    /**
     * 黑名单校验
     *
     * @author liaoxiting
     * @date 2020/12/1 21:18
     */
    @Bean
    public BlackListApi blackListApi() {
        return new BlackListService(blackListCache);
    }

    /**
     * 白名单校验
     *
     * @author liaoxiting
     * @date 2020/12/1 21:18
     */
    @Bean
    public WhiteListApi whiteListApi() {
        return new WhiteListService(whiteListCache);
    }

    /**
     * 计数校验器
     *
     * @author liaoxiting
     * @date 2020/12/1 21:18
     */
    @Bean
    public CountValidatorApi countValidatorApi() {
        return new DefaultCountValidator(countValidateCache);
    }

}
