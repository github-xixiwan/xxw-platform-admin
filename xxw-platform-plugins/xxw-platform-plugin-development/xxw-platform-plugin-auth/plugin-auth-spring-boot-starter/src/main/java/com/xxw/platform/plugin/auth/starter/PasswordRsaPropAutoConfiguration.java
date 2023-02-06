package com.xxw.platform.plugin.auth.starter;

import com.xxw.platform.plugin.auth.api.pojo.auth.PwdRsaSecretProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 密码认证配置
 *
 * @author liaoxiting
 * @date 2022/10/16 15:33
 */
@Configuration
public class PasswordRsaPropAutoConfiguration {

    /**
     * 密码加密传输的配置，RSA加密密钥对
     *
     * @author liaoxiting
     * @date 2022/10/16 15:34
     */
    @Bean
    @ConfigurationProperties(prefix = "xxw.password.rsa")
    public PwdRsaSecretProperties pwdRsaSecretProperties() {
        return new PwdRsaSecretProperties();
    }

}
