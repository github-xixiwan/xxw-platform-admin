package com.xxw.platform.plugin.monitor.business.system.holder;

import com.xxw.platform.plugin.monitor.business.system.SystemHardwareCalculator;
import com.xxw.platform.plugin.timer.api.TimerAction;

/**
 * 定时刷新服务器状态信息
 *
 * @author liaoxiting
 * @date 2021/1/31 21:52
 */
public class SystemHardwareInfoHolder implements TimerAction {

    private SystemHardwareCalculator systemHardwareCalculator = null;

    @Override
    public void action(String params) {
        SystemHardwareCalculator newInfo = new SystemHardwareCalculator();
        newInfo.calc();
        systemHardwareCalculator = newInfo;
    }

    public SystemHardwareCalculator getSystemHardwareInfo() {
        if (systemHardwareCalculator != null) {
            return systemHardwareCalculator;
        }

        systemHardwareCalculator = new SystemHardwareCalculator();
        systemHardwareCalculator.calc();
        return systemHardwareCalculator;
    }

}
