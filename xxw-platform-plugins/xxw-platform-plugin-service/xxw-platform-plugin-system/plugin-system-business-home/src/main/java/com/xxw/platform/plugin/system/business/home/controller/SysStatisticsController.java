package com.xxw.platform.plugin.system.business.home.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.business.home.entity.SysStatisticsCount;
import com.xxw.platform.plugin.system.business.home.pojo.request.SysStatisticsCountRequest;
import com.xxw.platform.plugin.system.business.home.service.SysStatisticsCountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 常用功能的统计次数控制器
 *
 * @author fengshuonan
 * @date 2022/02/10 21:17
 */
@RestController
@ApiResource(name = "常用功能的统计次数", resBizType = ResBizTypeEnum.SYSTEM)
public class SysStatisticsController {

    @Resource
    private SysStatisticsCountService sysStatisticsCountService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    @PostResource(name = "添加", path = "/sysStatisticsCount/add")
    public ResponseData<?> add(@RequestBody @Validated(SysStatisticsCountRequest.add.class) SysStatisticsCountRequest sysStatisticsCountRequest) {
        sysStatisticsCountService.add(sysStatisticsCountRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    @PostResource(name = "删除", path = "/sysStatisticsCount/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysStatisticsCountRequest.delete.class) SysStatisticsCountRequest sysStatisticsCountRequest) {
        sysStatisticsCountService.del(sysStatisticsCountRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    @PostResource(name = "编辑", path = "/sysStatisticsCount/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysStatisticsCountRequest.edit.class) SysStatisticsCountRequest sysStatisticsCountRequest) {
        sysStatisticsCountService.edit(sysStatisticsCountRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    @GetResource(name = "查看详情", path = "/sysStatisticsCount/detail")
    public ResponseData<SysStatisticsCount> detail(@Validated(SysStatisticsCountRequest.detail.class) SysStatisticsCountRequest sysStatisticsCountRequest) {
        return new SuccessResponseData<>(sysStatisticsCountService.detail(sysStatisticsCountRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    @GetResource(name = "获取列表", path = "/sysStatisticsCount/list")
    public ResponseData<List<SysStatisticsCount>> list(SysStatisticsCountRequest sysStatisticsCountRequest) {
        return new SuccessResponseData<>(sysStatisticsCountService.findList(sysStatisticsCountRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    @GetResource(name = "分页查询", path = "/sysStatisticsCount/page")
    public ResponseData<PageResult<SysStatisticsCount>> page(SysStatisticsCountRequest sysStatisticsCountRequest) {
        return new SuccessResponseData<>(sysStatisticsCountService.findPage(sysStatisticsCountRequest));
    }

}
