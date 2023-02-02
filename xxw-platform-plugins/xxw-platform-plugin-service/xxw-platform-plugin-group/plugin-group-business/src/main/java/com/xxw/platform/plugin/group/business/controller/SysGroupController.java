package com.xxw.platform.plugin.group.business.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.group.api.GroupApi;
import com.xxw.platform.plugin.group.api.pojo.SysGroupDTO;
import com.xxw.platform.plugin.group.api.pojo.SysGroupRequest;
import com.xxw.platform.plugin.group.business.service.SysGroupService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务分组控制器
 *
 * @author fengshuonan
 * @date 2022/05/11 12:54
 */
@RestController
@ApiResource(name = "业务分组", resBizType = ResBizTypeEnum.SYSTEM)
public class SysGroupController {

    @Resource
    private SysGroupService sysGroupService;

    @Resource
    private GroupApi groupApi;

    /**
     * 获取某个业务的分组列表
     *
     * @author fengshuonan
     * @date 2022/05/11 12:54
     */
    @GetResource(name = "获取列表", path = "/sysGroup/list")
    public ResponseData<List<SysGroupDTO>> list(@Validated(BaseRequest.list.class) SysGroupRequest sysGroupRequest) {
        return new SuccessResponseData<>(groupApi.findGroupList(sysGroupRequest, false));
    }

    /**
     * 添加分组时候的选择列表
     *
     * @author fengshuonan
     * @date 2022/05/11 12:54
     */
    @GetResource(name = "添加分组时候的选择列表", path = "/sysGroup/addSelect")
    public ResponseData<List<SysGroupDTO>> addSelect(@Validated(BaseRequest.list.class) SysGroupRequest sysGroupRequest) {
        return new SuccessResponseData<>(sysGroupService.addSelect(sysGroupRequest));
    }

    /**
     * 将某个业务记录，增加到某个分组中，如果分组没有则创建分组
     *
     * @author fengshuonan
     * @date 2022/05/11 12:54
     */
    @PostResource(name = "添加", path = "/sysGroup/add")
    public ResponseData<?> add(@RequestBody @Validated(SysGroupRequest.add.class) SysGroupRequest sysGroupRequest) {
        sysGroupService.add(sysGroupRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除分组
     *
     * @author fengshuonan
     * @date 2022/05/11 12:54
     */
    @PostResource(name = "删除", path = "/sysGroup/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysGroupRequest.delete.class) SysGroupRequest sysGroupRequest) {
        sysGroupService.del(sysGroupRequest);
        return new SuccessResponseData<>();
    }

}
