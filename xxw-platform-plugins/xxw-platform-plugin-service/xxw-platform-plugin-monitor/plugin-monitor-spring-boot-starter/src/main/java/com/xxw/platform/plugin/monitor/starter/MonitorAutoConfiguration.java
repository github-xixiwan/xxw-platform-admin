package com.xxw.platform.plugin.monitor.starter;

import com.xxw.platform.plugin.monitor.business.system.holder.SystemHardwareInfoHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 系统监控的自动配置
 *
 * @author liaoxiting
 * @date 2021/1/31 22:37
 */
@Configuration
public class MonitorAutoConfiguration {

    /**
     * 系统信息的holder，从这里获取系统信息
     *
     * @author liaoxiting
     * @date 2021/2/1 20:44
     */
    @Bean
    public SystemHardwareInfoHolder systemHardwareInfoHolder() {
        return new SystemHardwareInfoHolder();
    }

}
