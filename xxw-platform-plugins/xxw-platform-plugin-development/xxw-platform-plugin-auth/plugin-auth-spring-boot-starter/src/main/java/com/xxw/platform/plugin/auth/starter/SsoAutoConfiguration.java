package com.xxw.platform.plugin.auth.starter;

import com.xxw.platform.plugin.auth.api.pojo.sso.SsoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 单点配置
 *
 * @author liaoxiting
 * @date 2021/5/25 22:29
 */
@Configuration
public class SsoAutoConfiguration {

    /**
     * 单点的开关配置
     *
     * @author liaoxiting
     * @date 2021/5/25 22:29
     */
    @Bean
    @ConfigurationProperties(prefix = "sso")
    public SsoProperties ssoProperties() {
        return new SsoProperties();
    }

}
