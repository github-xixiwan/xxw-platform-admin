package com.xxw.platform.plugin.system.starter.listener;

import com.xxw.platform.frame.common.listener.ApplicationReadyListener;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.starter.init.InitAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动后初始化超级管理员
 *
 * @author liaoxiting
 * @date 2020/12/17 21:44
 */
@Component
@Slf4j
public class SuperAdminInitListener extends ApplicationReadyListener implements Ordered {

    @Resource
    private InitAdminService initAdminService;

    @Override
    public void eventCallback(ApplicationReadyEvent event) {
        long startTime = System.currentTimeMillis();
        initAdminService.initSuperAdmin();
        log.info("初始化超级管理员权限完成，耗时：{}ms", (System.currentTimeMillis() - startTime));
    }

    @Override
    public int getOrder() {
        return SystemConstants.SUPER_ADMIN_INIT_LISTENER_SORT;
    }

}
