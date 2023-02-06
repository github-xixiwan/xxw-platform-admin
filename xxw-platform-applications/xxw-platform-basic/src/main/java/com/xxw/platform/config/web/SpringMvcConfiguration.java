package com.xxw.platform.config.web;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xxw.platform.core.security.AuthJwtTokenSecurityInterceptor;
import com.xxw.platform.core.security.PermissionSecurityInterceptor;
import com.xxw.platform.frame.web.core.error.CustomErrorAttributes;
import com.xxw.platform.plugin.security.sdk.cors.CorsFilterConfiguration;
import com.xxw.platform.plugin.wrapper.sdk.field.jackson.CustomJacksonIntrospector;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * spring mvc的配置
 *
 * @author liaoxiting
 * @since 2020/4/11 10:23
 */
@Configuration
@Import({cn.hutool.extra.spring.SpringUtil.class, CorsFilterConfiguration.class})
public class SpringMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private AuthJwtTokenSecurityInterceptor authJwtTokenSecurityInterceptor;

    @Resource
    private PermissionSecurityInterceptor permissionSecurityInterceptor;

    /**
     * 重写系统的默认错误提示
     *
     * @author liaoxiting
     * @since 2020/12/16 15:36
     */
    @Bean
    public CustomErrorAttributes errorAttributes() {
        return new CustomErrorAttributes();
    }

    /**
     * json自定义序列化工具,long转string
     *
     * @author liaoxiting
     * @since 2020/12/13 17:16
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance).serializerByType(Long.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.annotationIntrospector(new CustomJacksonIntrospector());
        };
    }

    /**
     * 配置项目拦截器
     *
     * @author liaoxiting
     * @since 2020/12/18 9:43
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authJwtTokenSecurityInterceptor);
        registry.addInterceptor(permissionSecurityInterceptor);
    }

    /**
     * 静态资源映射
     *
     * @author liaoxiting
     * @since 2021/1/16 21:45
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
        registry.addResourceHandler("/xxw-devops/**").addResourceLocations("classpath:/xxw-devops/");
    }

}
