package com.xxw.platform.plugin.pinyin.starter;

import com.xxw.platform.plugin.pinyin.api.PinYinApi;
import com.xxw.platform.plugin.pinyin.sdk.pinyin4j.PinyinServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 拼音的自动配置
 *
 * @author liaoxiting
 * @date 2020/12/4 15:28
 */
@Configuration
public class PinyinAutoConfiguration {

    /**
     * 拼音工具接口的封装
     *
     * @author liaoxiting
     * @date 2020/12/4 15:29
     */
    @Bean
    @ConditionalOnMissingBean(PinYinApi.class)
    public PinYinApi pinYinApi() {
        return new PinyinServiceImpl();
    }

}
