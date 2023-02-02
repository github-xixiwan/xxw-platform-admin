package com.xxw.platform.plugin.system.business.user.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.business.user.entity.SysUserGroup;
import com.xxw.platform.plugin.system.business.user.request.SysUserGroupRequest;
import com.xxw.platform.plugin.system.business.user.service.SysUserGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组控制器
 *
 * @author fengshuonan
 * @date 2022/09/26 10:12
 */
@RestController
@ApiResource(name = "用户组", resBizType = ResBizTypeEnum.SYSTEM)
public class SysUserGroupController {

    @Resource
    private SysUserGroupService sysUserGroupService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    @PostResource(name = "添加", path = "/sysUserGroup/add")
    public ResponseData<SysUserGroup> add(@RequestBody @Validated(SysUserGroupRequest.add.class) SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup result = sysUserGroupService.add(sysUserGroupRequest);
        return new SuccessResponseData<>(result);
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    @GetResource(name = "查看详情", path = "/sysUserGroup/detail")
    public ResponseData<SysUserGroup> detail(@Validated(SysUserGroupRequest.detail.class) SysUserGroupRequest sysUserGroupRequest) {
        return new SuccessResponseData<>(sysUserGroupService.detail(sysUserGroupRequest));
    }

    /**
     * 获取用户组-选择关系列表
     *
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    @GetResource(name = "获取用户组-选择关系列表", path = "/sysUserGroup/getSelectRelationList")
    public ResponseData<List<SimpleDict>> getSelectRelationList() {
        return new SuccessResponseData<>(sysUserGroupService.getSelectRelationList());
    }

}
