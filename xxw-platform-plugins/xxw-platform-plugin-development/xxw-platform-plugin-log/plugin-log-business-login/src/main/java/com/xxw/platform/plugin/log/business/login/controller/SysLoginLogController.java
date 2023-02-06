package com.xxw.platform.plugin.log.business.login.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogDto;
import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogRequest;
import com.xxw.platform.plugin.log.business.login.entity.SysLoginLog;
import com.xxw.platform.plugin.log.business.login.service.SysLoginLogService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登陆日志控制器
 *
 * @author liaoxiting
 * @date 2021/1/13 17:51
 */
@RestController
@ApiResource(name = "登录日志", resBizType = ResBizTypeEnum.SYSTEM)
public class SysLoginLogController {

    @Resource
    private SysLoginLogService sysLoginLogService;

    /**
     * 清空登录日志
     *
     * @author liaoxiting
     * @date 2021/1/13 17:51
     */
    @GetResource(name = "清空登录日志", path = "/loginLog/deleteAll")
    @BusinessLog
    public ResponseData<?> deleteAll() {
        sysLoginLogService.delAll();
        return new SuccessResponseData<>();
    }

    /**
     * 查询登录日志详情
     *
     * @author liaoxiting
     * @date 2021/1/13 17:51
     */
    @GetResource(name = "查看详情登录日志", path = "/loginLog/detail")
    public ResponseData<SysLoginLog> detail(@Validated(SysLoginLogRequest.detail.class) SysLoginLogRequest sysLoginLogRequest) {
        return new SuccessResponseData<>(sysLoginLogService.detail(sysLoginLogRequest));
    }

    /**
     * 分页查询登录日志
     *
     * @author liaoxiting
     * @date 2021/1/13 17:51
     */
    @GetResource(name = "分页查询登录日志", path = "/loginLog/page")
    public ResponseData<PageResult<SysLoginLogDto>> page(SysLoginLogRequest sysLoginLogRequest) {
        return new SuccessResponseData<>(sysLoginLogService.findPage(sysLoginLogRequest));
    }

}
