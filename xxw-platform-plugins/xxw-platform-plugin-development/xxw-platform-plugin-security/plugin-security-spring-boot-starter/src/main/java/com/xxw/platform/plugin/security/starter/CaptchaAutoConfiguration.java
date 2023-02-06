package com.xxw.platform.plugin.security.starter;

import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.security.api.DragCaptchaApi;
import com.xxw.platform.plugin.security.api.ImageCaptchaApi;
import com.xxw.platform.plugin.security.sdk.captcha.DragCaptchaService;
import com.xxw.platform.plugin.security.sdk.captcha.ImageCaptchaService;
import com.xxw.platform.plugin.security.starter.cache.SecurityMemoryCacheAutoConfiguration;
import com.xxw.platform.plugin.security.starter.cache.SecurityRedisCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 图形验证码自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 21:44
 */
@Configuration
@AutoConfigureAfter({SecurityMemoryCacheAutoConfiguration.class, SecurityRedisCacheAutoConfiguration.class})
public class CaptchaAutoConfiguration {

    @Resource(name = "captchaCache")
    private CacheOperatorApi<String> captchaCache;

    /**
     * 图形验证码
     *
     * @author liaoxiting
     * @date 2021/1/15 11:25
     */
    @Bean
    @ConditionalOnMissingBean(ImageCaptchaApi.class)
    public ImageCaptchaApi captchaApi() {
        // 验证码过期时间 120秒
        return new ImageCaptchaService(captchaCache);
    }

    /**
     * 拖拽验证码工具
     *
     * @author liaoxiting
     * @date 2021/7/5 11:57
     */
    @Bean
    @ConditionalOnMissingBean(DragCaptchaApi.class)
    public DragCaptchaApi dragCaptchaService() {
        // 验证码过期时间 120秒
        return new DragCaptchaService(captchaCache);
    }

}
