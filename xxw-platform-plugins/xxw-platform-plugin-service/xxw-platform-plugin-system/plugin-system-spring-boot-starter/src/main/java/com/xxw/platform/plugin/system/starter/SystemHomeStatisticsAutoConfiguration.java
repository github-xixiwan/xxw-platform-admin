package com.xxw.platform.plugin.system.starter;

import com.xxw.platform.plugin.system.business.home.aop.InterfaceStatisticsAop;
import com.xxw.platform.plugin.system.business.home.timer.InterfaceStatisticsTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 首页统计数据的装载
 *
 * @author liaoxiting
 * @date 2022/2/9 17:57
 */
@Configuration
public class SystemHomeStatisticsAutoConfiguration {

    /**
     * 接口统计的AOP
     *
     * @author liaoxiting
     * @date 2022/2/9 14:00
     */
    @Bean
    public InterfaceStatisticsAop interfaceStatisticsAspect() {
        return new InterfaceStatisticsAop();
    }

    /**
     * 定时将统计数据存入数据库
     *
     * @author liaoxiting
     * @date 2022/2/9 17:58
     */
    @Bean
    public InterfaceStatisticsTimer interfaceStatisticsHolder() {
        return new InterfaceStatisticsTimer();
    }

}
