package com.xxw.platform.plugin.system.starter.config;

import com.xxw.platform.plugin.system.starter.ErrorStaticJsonView;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 错误界面自动配置，一般用于404响应
 *
 * @author liaoxiting
 * @date 2021/5/17 11:16
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class RestErrorViewAutoConfiguration {

    /**
     * 默认错误页面，返回json
     *
     * @author liaoxiting
     * @date 2020/12/16 15:47
     */
    @Bean("error")
    @ConditionalOnMissingBean(name = "error")
    public ErrorStaticJsonView error() {
        return new ErrorStaticJsonView();
    }

}
