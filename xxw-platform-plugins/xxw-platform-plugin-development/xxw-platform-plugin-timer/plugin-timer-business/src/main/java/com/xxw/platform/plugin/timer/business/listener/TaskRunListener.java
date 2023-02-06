package com.xxw.platform.plugin.timer.business.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.listener.ApplicationStartedListener;
import com.xxw.platform.plugin.timer.api.TimerExeService;
import com.xxw.platform.plugin.timer.api.enums.TimerJobStatusEnum;
import com.xxw.platform.plugin.timer.business.entity.SysTimers;
import com.xxw.platform.plugin.timer.business.param.SysTimersParam;
import com.xxw.platform.plugin.timer.business.service.SysTimersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * 项目启动，将数据库所有定时任务开启
 *
 * @author liaoxiting
 * @date 2021/1/12 20:40
 */
@Slf4j
public class TaskRunListener extends ApplicationStartedListener implements Ordered {

    @Override
    public void eventCallback(ApplicationStartedEvent event) {

        SysTimersService sysTimersService = SpringUtil.getBean(SysTimersService.class);
        TimerExeService timerExeService = SpringUtil.getBean(TimerExeService.class);

        // 开启任务调度
        timerExeService.start();

        // 获取数据库所有开启状态的任务
        SysTimersParam sysTimersParam = new SysTimersParam();
        sysTimersParam.setDelFlag("N");
        sysTimersParam.setJobStatus(TimerJobStatusEnum.RUNNING.getCode());
        List<SysTimers> list = sysTimersService.findList(sysTimersParam);

        // 添加定时任务到调度器
        for (SysTimers sysTimers : list) {
            try {
                timerExeService.startTimer(String.valueOf(sysTimers.getTimerId()), sysTimers.getCron(), sysTimers.getActionClass(), sysTimers.getParams());
            } catch (Exception exception) {
                // 遇到错误直接略过这个定时器（可能多个项目公用库）
                log.error("定时器初始化遇到错误，略过该定时器！", exception);
            }
        }

    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 300;
    }

}
