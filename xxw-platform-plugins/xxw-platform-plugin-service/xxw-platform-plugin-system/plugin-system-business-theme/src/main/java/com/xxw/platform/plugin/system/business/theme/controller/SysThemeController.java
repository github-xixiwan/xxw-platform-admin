package com.xxw.platform.plugin.system.business.theme.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeDTO;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeRequest;
import com.xxw.platform.plugin.system.business.theme.entity.SysTheme;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统主题控制器
 *
 * @author liaoxiting
 * @date 2021/12/17 16:40
 */
@RestController
@ApiResource(name = "系统主题管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysThemeController {

    @Resource
    private SysThemeService sysThemeService;

    /**
     * 增加系统主题
     *
     * @author liaoxiting
     * @date 2021/12/17 16:43
     */
    @PostResource(name = "增加系统主题", path = "/sysTheme/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(SysThemeRequest.add.class) SysThemeRequest sysThemeParam) {
        sysThemeService.add(sysThemeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统主题
     *
     * @author liaoxiting
     * @date 2021/12/17 16:45
     */
    @PostResource(name = "删除系统主题", path = "/sysTheme/del")
    @BusinessLog
    public ResponseData<?> del(@RequestBody @Validated(SysThemeRequest.delete.class) SysThemeRequest sysThemeParam) {
        sysThemeService.del(sysThemeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 修改系统主题
     *
     * @author liaoxiting
     * @date 2021/12/17 16:50
     */
    @PostResource(name = "修改系统主题", path = "/sysTheme/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(SysThemeRequest.edit.class) SysThemeRequest sysThemeParam) {
        sysThemeService.edit(sysThemeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查询系统主题
     *
     * @author liaoxiting
     * @date 2021/12/17 16:58
     */
    @GetResource(name = "查询系统主题", path = "/sysTheme/findPage")
    public ResponseData<PageResult<SysThemeDTO>> findPage(SysThemeRequest sysThemeParam) {
        return new SuccessResponseData<>(sysThemeService.findPage(sysThemeParam));
    }

    /**
     * 查询系统主题详情
     *
     * @author liaoxiting
     * @date 2021/12/17 17:04
     */
    @GetResource(name = "查询系统主题详情", path = "/sysTheme/detail")
    public ResponseData<SysTheme> detail(@Validated(SysThemeRequest.updateStatus.class) SysThemeRequest sysThemeParam) {
        return new SuccessResponseData<>(sysThemeService.detail(sysThemeParam));
    }

    /**
     * 修改系统主题启用状态
     *
     * @author liaoxiting
     * @date 2021/12/17 17:32
     */
    @PostResource(name = "修改系统主题启用状态", path = "/sysTheme/updateStatus")
    @BusinessLog
    public ResponseData<?> updateThemeStatus(@RequestBody @Validated(SysThemeRequest.updateStatus.class) SysThemeRequest sysThemeParam) {
        sysThemeService.updateThemeStatus(sysThemeParam);
        return new SuccessResponseData<>();
    }
}
