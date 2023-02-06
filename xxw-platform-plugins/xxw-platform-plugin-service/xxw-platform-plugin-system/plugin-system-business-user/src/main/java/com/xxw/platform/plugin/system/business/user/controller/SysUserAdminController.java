package com.xxw.platform.plugin.system.business.user.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserAdminDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysAdminRequest;
import com.xxw.platform.plugin.system.business.user.service.SysUserAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员相关接口
 * <p>
 * 管理员角色只能维护后台相关菜单
 *
 * @author liaoxiting
 * @date 2022/9/30 10:44
 */
@RestController
@ApiResource(name = "管理员相关接口", resBizType = ResBizTypeEnum.SYSTEM)
public class SysUserAdminController {

    @Resource
    private SysUserAdminService sysUserAdminService;

    /**
     * 获取后台管理员列表
     *
     * @author liaoxiting
     * @date 2022/9/30 10:44
     */
    @GetResource(name = "获取后台管理员列表", path = "/sysUser/backAuth/getAdminList")
    public ResponseData<List<SysUserAdminDTO>> getAdminList() {
        List<SysUserAdminDTO> adminUserList = sysUserAdminService.getAdminUserList();
        return new SuccessResponseData<>(adminUserList);
    }

    /**
     * 添加后台管理员
     *
     * @author liaoxiting
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "添加后台管理员", path = "/sysUser/backAuth/addAdmin")
    public ResponseData<?> addAdmin(@RequestBody @Validated(BaseRequest.add.class) SysAdminRequest sysAdminRequest) {
        this.sysUserAdminService.addAdminUser(sysAdminRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除后台管理员
     *
     * @author liaoxiting
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "删除后台管理员", path = "/sysUser/backAuth/delAdmin")
    public ResponseData<?> delAdmin(@RequestBody @Validated(BaseRequest.delete.class) SysAdminRequest sysAdminRequest) {
        this.sysUserAdminService.deleteAdminUser(sysAdminRequest);
        return new SuccessResponseData<>();
    }

}
