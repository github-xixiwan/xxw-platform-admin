package com.xxw.platform.plugin.security.starter;

import com.xxw.platform.plugin.security.api.constants.SecurityConstants;
import com.xxw.platform.plugin.security.sdk.ct.ClearThreadLocalFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 安全模块自动配置
 *
 * @author liaoxiting
 * @date 2021/2/19 9:05
 */
@Configuration
public class SecurityAutoConfiguration {

    /**
     * ThreadLocal清除器
     *
     * @author liaoxiting
     * @date 2021/10/29 11:29
     */
    @Bean
    public FilterRegistrationBean<ClearThreadLocalFilter> clearThreadLocalFilterFilterRegistrationBean() {
        FilterRegistrationBean<ClearThreadLocalFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ClearThreadLocalFilter());
        bean.addUrlPatterns(SecurityConstants.DEFAULT_XSS_PATTERN);
        bean.setName(ClearThreadLocalFilter.NAME);
        bean.setOrder(HIGHEST_PRECEDENCE + 1);
        return bean;
    }

}
