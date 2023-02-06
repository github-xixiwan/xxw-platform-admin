package com.xxw.platform.plugin.system.business.role.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.listener.ApplicationStartedListener;
import com.xxw.platform.plugin.system.business.role.service.SysRoleResourceService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 监听项目启动，进行role_resource表的资源名称前缀修改
 *
 * @author liaoxiting
 * @date 2022/11/16 23:05
 */
public class RoleResourceUpdateListener extends ApplicationStartedListener implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void eventCallback(ApplicationStartedEvent event) {

        SysRoleResourceService roleResourceService = SpringUtil.getBean(SysRoleResourceService.class);

        // 获取environment参数
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        String springApplicationName = environment.getProperty("spring.application.name");

        // 获取所有menu_resource表记录
        roleResourceService.updateNewAppCode(true, springApplicationName);

    }

}
