package com.xxw.platform.plugin.system.business.home.timer;

import com.xxw.platform.plugin.system.business.home.service.HomePageService;
import com.xxw.platform.plugin.timer.api.TimerAction;

import javax.annotation.Resource;

/**
 * 定时刷新接口访问次数统计
 *
 * @author liaoxiting
 * @date 2022/2/9 16:08
 */
public class InterfaceStatisticsTimer implements TimerAction {

    @Resource
    private HomePageService homePageService;

    @Override
    public void action(String params) {
        homePageService.saveStatisticsCacheToDb();
    }

}
