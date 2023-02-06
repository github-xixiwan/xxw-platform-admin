package com.xxw.platform.plugin.monitor.business.system.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.monitor.business.system.SystemHardwareCalculator;
import com.xxw.platform.plugin.monitor.business.system.holder.SystemHardwareInfoHolder;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统监控控制器
 *
 * @author liaoxiting
 * @date 2021/4/14 18:44
 */
@RestController
@ApiResource(name = "监控的控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class MonitorStatusController {

    @Resource
    private SystemHardwareInfoHolder systemHardwareInfoHolder;

    /**
     * 将应用设为默认应用，用户进入系统会默认进这个应用的菜单
     *
     * @author liaoxiting
     * @date 2020/6/29 16:49
     */
    @GetResource(name = "获取系统信息", path = "/getSystemInfo")
    public ResponseData<SystemHardwareCalculator> getSystemInfo() {
        SystemHardwareCalculator systemHardwareInfo = systemHardwareInfoHolder.getSystemHardwareInfo();
        return new SuccessResponseData<>(systemHardwareInfo);
    }

}
