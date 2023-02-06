package com.xxw.platform.frame.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
/**
 * application 启动后状态的监听器
 *
 * @author liaoxiting
 * @date 2021/5/14 20:28
 */
@Slf4j
public abstract class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        // 如果是配置中心的上下文略过，spring cloud环境environment会读取不到
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext instanceof AnnotationConfigApplicationContext) {
            return;
        }

        // 执行具体业务
        this.eventCallback(event);
    }

    /**
     * 监听器具体的业务逻辑
     *
     * @author liaoxiting
     * @date 2021/5/14 20:17
     */
    public abstract void eventCallback(ApplicationStartedEvent event);

}
