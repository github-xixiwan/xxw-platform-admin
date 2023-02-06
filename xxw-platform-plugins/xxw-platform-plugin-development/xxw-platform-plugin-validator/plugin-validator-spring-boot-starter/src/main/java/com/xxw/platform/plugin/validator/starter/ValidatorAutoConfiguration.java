package com.xxw.platform.plugin.validator.starter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 校验器的自动配置
 *
 * @author liaoxiting
 * @date 2021/3/18 16:03
 */
@Configuration
@AutoConfigureBefore(ValidationAutoConfiguration.class)
public class ValidatorAutoConfiguration {

    /**
     * 自定义的spring参数校验器，重写主要为了保存一些在自定义validator中读不到的属性
     *
     * @author liaoxiting
     * @date 2020/8/12 20:18
     */
    @Bean
    public XxwPlatformValidator gunsValidator() {
        return new XxwPlatformValidator();
    }

}
