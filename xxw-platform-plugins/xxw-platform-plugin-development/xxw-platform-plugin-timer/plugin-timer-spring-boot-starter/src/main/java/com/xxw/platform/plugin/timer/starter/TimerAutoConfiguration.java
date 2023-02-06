package com.xxw.platform.plugin.timer.starter;

import com.xxw.platform.plugin.timer.api.TimerExeService;
import com.xxw.platform.plugin.timer.sdk.hutool.TimerExeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务的自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 21:34
 */
@Configuration
public class TimerAutoConfiguration {

    /**
     * hutool 新的定时任务，增加判断spring cloud双上下文
     *
     * @author liaoxiting
     * @date 2022/11/15 14:52
     */
    @Bean
    @ConditionalOnMissingBean(TimerExeService.class)
    public TimerExeService timerExeService() {
        return new TimerExeServiceImpl();
    }

}
