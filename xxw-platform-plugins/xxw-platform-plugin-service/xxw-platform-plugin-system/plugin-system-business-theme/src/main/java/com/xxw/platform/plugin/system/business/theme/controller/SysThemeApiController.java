package com.xxw.platform.plugin.system.business.theme.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeRequest;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 主题开放接口的API
 *
 * @author liaoxiting
 * @date 2022/1/10 18:27
 */
@RestController
@ApiResource(name = "主题开放接口的API", resBizType = ResBizTypeEnum.SYSTEM)
public class SysThemeApiController {

    @Resource
    private SysThemeService sysThemeService;

    /**
     * 获取当前Guns管理系统的主题数据
     *
     * @author liaoxiting
     * @date 2022/1/10 18:29
     */
    @GetResource(name = "获取当前Guns管理系统的主题数据", path = "/theme/currentThemeInfo", requiredPermission = false, requiredLogin = false)
    public ResponseData<DefaultTheme> currentThemeInfo(SysThemeRequest sysThemeParam) {
        DefaultTheme defaultTheme = sysThemeService.currentThemeInfo(sysThemeParam);
        return new SuccessResponseData<>(defaultTheme);
    }
}
