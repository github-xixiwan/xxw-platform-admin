package com.xxw.platform.plugin.system.business.notice.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.notice.SysNoticeRequest;
import com.xxw.platform.plugin.system.business.notice.entity.SysNotice;
import com.xxw.platform.plugin.system.business.notice.service.SysNoticeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知管理控制器
 *
 * @author liaoxiting
 * @date 2021/1/8 19:47
 */
@RestController
@ApiResource(name = "通知管理")
public class SysNoticeController {

    @Resource
    private SysNoticeService sysNoticeService;

    /**
     * 添加通知管理
     *
     * @author liaoxiting
     * @date 2021/1/9 14:44
     */
    @PostResource(name = "添加通知管理", path = "/sysNotice/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(SysNoticeRequest.add.class) SysNoticeRequest sysNoticeParam) {
        sysNoticeService.add(sysNoticeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑通知管理
     *
     * @author liaoxiting
     * @date 2021/1/9 14:54
     */
    @PostResource(name = "编辑通知管理", path = "/sysNotice/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(SysNoticeRequest.edit.class) SysNoticeRequest sysNoticeParam) {
        sysNoticeService.edit(sysNoticeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除通知管理
     *
     * @author liaoxiting
     * @date 2021/1/9 14:54
     */
    @PostResource(name = "删除通知管理", path = "/sysNotice/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(SysNoticeRequest.delete.class) SysNoticeRequest sysNoticeParam) {
        sysNoticeService.del(sysNoticeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查看通知管理
     *
     * @author liaoxiting
     * @date 2021/1/9 9:49
     */
    @GetResource(name = "查看通知管理", path = "/sysNotice/detail")
    public ResponseData<SysNotice> detail(@Validated(SysNoticeRequest.detail.class) SysNoticeRequest sysNoticeParam) {
        return new SuccessResponseData<>(sysNoticeService.detail(sysNoticeParam));
    }

    /**
     * 查询通知管理
     *
     * @author liaoxiting
     * @date 2021/1/9 21:23
     */
    @GetResource(name = "查询通知管理", path = "/sysNotice/page")
    public ResponseData<PageResult<SysNotice>> page(SysNoticeRequest sysNoticeParam) {
        return new SuccessResponseData<>(sysNoticeService.findPage(sysNoticeParam));
    }

    /**
     * 通知管理列表
     *
     * @author liaoxiting
     * @date 2021/1/9 14:55
     */
    @GetResource(name = "通知管理列表", path = "/sysNotice/list")
    public ResponseData<List<SysNotice>> list(SysNoticeRequest sysNoticeParam) {
        return new SuccessResponseData<>(sysNoticeService.findList(sysNoticeParam));
    }

}
