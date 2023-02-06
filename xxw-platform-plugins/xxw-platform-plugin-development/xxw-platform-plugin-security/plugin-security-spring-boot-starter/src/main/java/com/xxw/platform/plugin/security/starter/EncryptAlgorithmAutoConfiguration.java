package com.xxw.platform.plugin.security.starter;

import com.xxw.platform.plugin.security.sdk.df.algorithm.EncryptAlgorithmApi;
import com.xxw.platform.plugin.security.sdk.df.algorithm.impl.AesEncryptAlgorithmApiImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加密算法自动配置
 *
 * @author liaoxiting
 * @date 2021/7/5 10:06
 */
@Configuration
public class EncryptAlgorithmAutoConfiguration {

    /**
     * 数据库加密算法
     *
     * @return {@link import com.xxw.platform.plugin.security.sdk.df.algorithm..EncryptAlgorithmApi}
     * @author liaoxiting
     * @date 2021/7/5 10:16
     **/
    @Bean
    @ConditionalOnMissingBean(EncryptAlgorithmApi.class)
    public EncryptAlgorithmApi encryptAlgorithmApi() {
        return new AesEncryptAlgorithmApiImpl();
    }

}
