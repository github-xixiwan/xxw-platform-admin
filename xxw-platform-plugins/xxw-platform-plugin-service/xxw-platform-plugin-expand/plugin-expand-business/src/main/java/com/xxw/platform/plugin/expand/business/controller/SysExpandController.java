package com.xxw.platform.plugin.expand.business.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.api.ExpandApi;
import com.xxw.platform.plugin.expand.api.pojo.ExpandFieldInfo;
import com.xxw.platform.plugin.expand.business.entity.SysExpand;
import com.xxw.platform.plugin.expand.business.entity.SysExpandData;
import com.xxw.platform.plugin.expand.business.request.SysExpandRequest;
import com.xxw.platform.plugin.expand.business.service.SysExpandService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务拓展控制器
 *
 * @author liaoxiting
 * @date 2022/03/29 23:47
 */
@RestController
@ApiResource(name = "业务拓展", resBizType = ResBizTypeEnum.SYSTEM)
public class SysExpandController {

    @Resource
    private SysExpandService sysExpandService;

    @Resource
    private ExpandApi expandApi;

    /**
     * 添加
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @PostResource(name = "添加", path = "/sysExpand/add")
    public ResponseData<SysExpand> add(@RequestBody @Validated(SysExpandRequest.add.class) SysExpandRequest sysExpandRequest) {
        sysExpandService.add(sysExpandRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @PostResource(name = "删除", path = "/sysExpand/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysExpandRequest.delete.class) SysExpandRequest sysExpandRequest) {
        sysExpandService.del(sysExpandRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @PostResource(name = "编辑", path = "/sysExpand/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysExpandRequest.edit.class) SysExpandRequest sysExpandRequest) {
        sysExpandService.edit(sysExpandRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改业务状态
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @PostResource(name = "修改业务状态", path = "/sysExpand/updateStatus")
    public ResponseData<?> updateStatus(@RequestBody @Validated(BaseRequest.updateStatus.class) SysExpandRequest sysExpandRequest) {
        sysExpandService.updateStatus(sysExpandRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @GetResource(name = "查看详情", path = "/sysExpand/detail")
    public ResponseData<SysExpand> detail(@Validated(SysExpandRequest.detail.class) SysExpandRequest sysExpandRequest) {
        return new SuccessResponseData<>(sysExpandService.detail(sysExpandRequest));
    }

    /**
     * 根据业务编码获取业务数据详情
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @GetResource(name = "根据业务编码获取业务数据详情", path = "/sysExpand/getByExpandCode")
    public ResponseData<SysExpandData> getByExpandCode(@Validated(SysExpandRequest.getByExpandCode.class) SysExpandRequest sysExpandRequest) {
        return new SuccessResponseData<>(sysExpandService.getByExpandCode(sysExpandRequest));
    }

    /**
     * 获取列表
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @GetResource(name = "获取列表", path = "/sysExpand/list")
    public ResponseData<List<SysExpand>> list(SysExpandRequest sysExpandRequest) {
        return new SuccessResponseData<>(sysExpandService.findList(sysExpandRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @GetResource(name = "分页查询", path = "/sysExpand/page")
    public ResponseData<PageResult<SysExpand>> page(SysExpandRequest sysExpandRequest) {
        return new SuccessResponseData<>(sysExpandService.findPage(sysExpandRequest));
    }

    /**
     * 获取某个业务，需要列表展示的拓展字段
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    @GetResource(name = "获取某个业务，需要列表展示的拓展字段", path = "/sysExpand/getListFields")
    public ResponseData<List<ExpandFieldInfo>> getListFields(@Validated(SysExpandRequest.getByExpandCode.class) SysExpandRequest sysExpandRequest) {
        return new SuccessResponseData<>(expandApi.getPageListExpandFieldList(sysExpandRequest.getExpandCode()));
    }

}
