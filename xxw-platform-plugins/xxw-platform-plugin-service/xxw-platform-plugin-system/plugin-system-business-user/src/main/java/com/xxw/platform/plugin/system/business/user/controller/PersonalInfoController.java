package com.xxw.platform.plugin.system.business.user.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.business.user.service.SysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 个人信息控制器
 *
 * @author liaoxiting
 * @date 2021/3/17 22:05
 */
@RestController
@ApiResource(name = "个人信息", resBizType = ResBizTypeEnum.SYSTEM)
public class PersonalInfoController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 更新用户个人信息
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "个人信息_更新个人信息", path = "/sysUser/updateInfo", requiredPermission = false)
    @BusinessLog
    public ResponseData<?> updateInfo(@RequestBody @Validated(SysUserRequest.updateInfo.class) SysUserRequest sysUserRequest) {
        sysUserService.editInfo(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改密码
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "个人信息_修改密码", path = "/sysUser/updatePassword", requiredPermission = false)
    @BusinessLog
    public ResponseData<?> updatePwd(@RequestBody @Validated(SysUserRequest.updatePwd.class) SysUserRequest sysUserRequest) {
        sysUserService.editPassword(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改头像
     *
     * @author liaoxiting
     * @date 2020/11/6 13:48
     */
    @PostResource(name = "个人信息_修改头像", path = "/sysUser/updateAvatar", requiredPermission = false)
    @BusinessLog
    public ResponseData<?> updateAvatar(@RequestBody @Validated(SysUserRequest.updateAvatar.class) SysUserRequest sysUserRequest) {
        sysUserService.editAvatar(sysUserRequest);
        return new SuccessResponseData<>();
    }

}
