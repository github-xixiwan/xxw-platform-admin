package com.xxw.platform.plugin.i18n.starter;

import com.xxw.platform.plugin.i18n.api.TranslationApi;
import com.xxw.platform.plugin.i18n.sdk.TranslationContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多语言翻译的自动配置
 *
 * @author liaoxiting
 * @date 2021/1/24 16:42
 */
@Configuration
public class TranslationAutoConfiguration {

    /**
     * 多语言翻译条目存放容器
     *
     * @author liaoxiting
     * @date 2021/1/24 19:42
     */
    @Bean
    public TranslationApi translationApi() {
        return new TranslationContainer();
    }

}
