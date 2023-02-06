package com.xxw.platform.plugin.log.business.manage.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.LogManagerApi;
import com.xxw.platform.plugin.log.api.pojo.manage.LogManagerRequest;
import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;
import com.xxw.platform.plugin.log.sdk.db.service.SysLogService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志管理控制器
 *
 * @author liaoxiting
 * @date 2020/11/3 12:44
 */
@RestController
@ApiResource(name = "日志管理控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class LogManagerController {

    /**
     * 日志管理api
     */
    @Resource
    private LogManagerApi logManagerApi;

    /**
     * 日志管理service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 查询日志列表
     *
     * @author liaoxiting
     * @date 2020/11/3 12:58
     */
    @GetResource(name = "查询日志列表", path = "/logManager/list")
    public ResponseData<List<LogRecordDTO>> list(@RequestBody LogManagerRequest logManagerRequest) {
        return new SuccessResponseData<>(logManagerApi.findList(logManagerRequest));
    }

    /**
     * 查询日志
     *
     * @author tengshuqi
     * @date 2021/1/8 17:36
     */
    @GetResource(name = "查询日志列表", path = "/logManager/page")
    public ResponseData<PageResult<LogRecordDTO>> page(LogManagerRequest logManagerRequest) {
        return new SuccessResponseData<>(logManagerApi.findPage(logManagerRequest));
    }

    /**
     * 删除日志
     *
     * @author liaoxiting
     * @date 2020/11/3 13:47
     */
    @PostResource(name = "删除日志", path = "/logManager/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(LogManagerRequest.delete.class) LogManagerRequest logManagerRequest) {
        sysLogService.delAll(logManagerRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看日志详情
     *
     * @author liaoxiting
     * @date 2021/1/11 17:36
     */
    @GetResource(name = "查看日志详情", path = "/logManager/detail")
    public ResponseData<LogRecordDTO> detail(@Validated(LogManagerRequest.detail.class) LogManagerRequest logManagerRequest) {
        return new SuccessResponseData<>(logManagerApi.detail(logManagerRequest));
    }

}
