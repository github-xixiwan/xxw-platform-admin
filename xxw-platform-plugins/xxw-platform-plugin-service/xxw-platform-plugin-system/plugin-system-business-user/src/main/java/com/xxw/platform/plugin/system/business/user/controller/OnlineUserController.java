package com.xxw.platform.plugin.system.business.user.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.user.OnlineUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.OnlineUserRequest;
import com.xxw.platform.plugin.system.business.user.service.SysUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 在线用户管理
 *
 * @author liaoxiting
 * @date 2021/1/11 22:52
 */
@RestController
@ApiResource(name = "在线用户管理", resBizType = ResBizTypeEnum.SYSTEM)
public class OnlineUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SessionManagerApi sessionManagerApi;

    /**
     * 当前在线用户列表
     *
     * @author liaoxiting
     * @date 2021/1/11 22:53
     */
    @GetResource(name = "当前在线用户列表", path = "/sysUser/onlineUserList")
    public ResponseData<List<OnlineUserDTO>> onlineUserList(OnlineUserRequest onlineUserRequest) {
        return new SuccessResponseData<>(sysUserService.onlineUserList(onlineUserRequest));
    }

    /**
     * 踢掉在线用户
     *
     * @author liaoxiting
     * @date 2021/1/11 22:53
     */
    @PostResource(name = "踢掉在线用户", path = "/sysUser/removeSession")
    public ResponseData<?> removeSession(@Valid @RequestBody OnlineUserRequest onlineUserRequest) {
        sessionManagerApi.removeSession(onlineUserRequest.getToken());
        return new SuccessResponseData<>();
    }

}
