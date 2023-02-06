package com.xxw.platform.plugin.security.starter;

import com.xxw.platform.plugin.security.api.expander.SecurityConfigExpander;
import com.xxw.platform.plugin.security.sdk.xss.XssFilter;
import com.xxw.platform.plugin.security.sdk.xss.XssJacksonDeserializer;
import com.xxw.platform.plugin.security.sdk.xss.prop.XssProperties;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * XSS安全过滤器相关配置
 *
 * @author liaoxiting
 * @date 2021/1/13 23:05
 */
@Configuration
public class XssAutoConfiguration {

    /**
     * XSS Filter过滤器，用来过滤param之类的传参
     *
     * @author liaoxiting
     * @date 2021/1/13 23:09
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean() {
        XssProperties properties = createProperties();

        FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        xssFilterFilterRegistrationBean.setFilter(new XssFilter(properties));
        xssFilterFilterRegistrationBean.addUrlPatterns(properties.getUrlPatterns());
        xssFilterFilterRegistrationBean.setName(XssFilter.FILTER_NAME);
        xssFilterFilterRegistrationBean.setOrder(HIGHEST_PRECEDENCE);
        return xssFilterFilterRegistrationBean;
    }

    /**
     * XSS的json反序列化器，针对json的传参
     *
     * @author liaoxiting
     * @date 2021/1/13 23:09
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer xssJackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.deserializerByType(String.class, new XssJacksonDeserializer(createProperties()));
    }

    /**
     * 组装xss的配置
     *
     * @author liaoxiting
     * @date 2021/1/13 23:13
     */
    private XssProperties createProperties() {
        XssProperties xssProperties = new XssProperties();
        xssProperties.setUrlPatterns(SecurityConfigExpander.getUrlPatterns());
        xssProperties.setUrlExclusion(SecurityConfigExpander.getUrlExclusion());
        return xssProperties;
    }

}
