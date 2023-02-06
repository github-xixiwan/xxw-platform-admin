package com.xxw.platform.plugin.system.business.theme.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateRelRequest;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeTemplateRelService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统主题模板属性关系控制器
 *
 * @author liaoxiting
 * @date 2021/12/24 10:55
 */
@RestController
@ApiResource(name = "系统主题模板属性关系管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysThemeTemplateRelController {

    @Resource
    private SysThemeTemplateRelService sysThemeTemplateRelService;

    /**
     * 增加系统主题模板属性关系
     *
     * @author liaoxiting
     * @date 2021/12/24 11:17
     */
    @PostResource(name = "增加系统主题模板属性关系", path = "/sysThemeTemplateRel/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(SysThemeTemplateRelRequest.add.class) SysThemeTemplateRelRequest sysThemeTemplateParam) {
        sysThemeTemplateRelService.add(sysThemeTemplateParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统主题模板属性关系
     *
     * @author liaoxiting
     * @date 2021/12/24 11:23
     */
    @PostResource(name = "删除系统主题模板属性关系", path = "/sysThemeTemplateRel/del")
    @BusinessLog
    public ResponseData<?> del(@RequestBody SysThemeTemplateRelRequest sysThemeTemplateRelParam) {
        sysThemeTemplateRelService.del(sysThemeTemplateRelParam);
        return new SuccessResponseData<>();
    }
}
