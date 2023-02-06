package com.xxw.platform.plugin.jwt.starter;

import com.xxw.platform.plugin.jwt.api.JwtApi;
import com.xxw.platform.plugin.jwt.api.expander.JwtConfigExpander;
import com.xxw.platform.plugin.jwt.api.pojo.config.JwtConfig;
import com.xxw.platform.plugin.jwt.sdk.JwtTokenOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jwt的自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 14:34
 */
@Configuration
public class JwtAutoConfiguration {

    /**
     * jwt操作工具类的配置
     *
     * @author liaoxiting
     * @date 2020/12/1 14:40
     */
    @Bean
    @ConditionalOnMissingBean(JwtApi.class)
    public JwtApi jwtApi() {

        JwtConfig jwtConfig = new JwtConfig();

        // 从系统配置表中读取配置
        jwtConfig.setJwtSecret(JwtConfigExpander.getJwtSecret());
        jwtConfig.setExpiredSeconds(JwtConfigExpander.getJwtTimeoutSeconds());

        return new JwtTokenOperator(jwtConfig);
    }

}
