package com.xxw.platform.plugin.wrapper.starter;

import com.xxw.platform.plugin.wrapper.sdk.WrapperAop;
import com.xxw.platform.plugin.wrapper.sdk.field.mvc.CustomEnumGenericConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wrapper的自动配置
 *
 * @author liaoxiting
 * @date 2021/1/19 22:42
 */
@Configuration
public class WrapperAutoConfiguration {

    /**
     * Wrapper的自动配置
     *
     * @author liaoxiting
     * @date 2021/1/19 22:42
     */
    @Bean
    @ConditionalOnMissingBean(WrapperAop.class)
    public WrapperAop wrapperAop() {
        return new WrapperAop();
    }

    /**
     * 自定义spring mvc的参数转化器，将请求参数转化为枚举
     *
     * @author liaoxiting
     * @date 2022/9/24 18:32
     */
    @Bean
    public CustomEnumGenericConverter customEnumGenericConverter() {
        return new CustomEnumGenericConverter();
    }

}
