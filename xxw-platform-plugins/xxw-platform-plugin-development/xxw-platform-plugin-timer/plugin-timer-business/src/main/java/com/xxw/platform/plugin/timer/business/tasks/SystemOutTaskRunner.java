package com.xxw.platform.plugin.timer.business.tasks;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.plugin.timer.api.TimerAction;
import org.springframework.stereotype.Component;

/**
 * 这是一个定时任务的示例程序
 *
 * @author liaoxiting
 * @date 2020/6/30 22:09
 */
@Component
public class SystemOutTaskRunner implements TimerAction {

    @Override
    public void action(String params) {
        System.out.println(StrUtil.format("这是一个定时任务测试的程序，一直输出这行内容！这个是参数: {}", params));
    }

}
