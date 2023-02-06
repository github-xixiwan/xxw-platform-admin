package com.xxw.platform.plugin.system.business.role.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.MenuServiceApi;
import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.role.service.SysRoleResourceService;
import com.xxw.platform.plugin.system.business.role.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色控制器
 *
 * @author liaoxiting
 * @date 2020/11/5 上午10:19
 */
@RestController
@ApiResource(name = "系统角色管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysRoleAuthController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private MenuServiceApi menuServiceApi;

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    /**
     * 获取角色分配菜单界面，绑定情况列表
     *
     * @author liaoxiting
     * @date 2022/9/28 16:04
     */
    @GetResource(name = "获取角色分配菜单界面，绑定情况列表", path = "/sysMenu/roleBindMenuList")
    public ResponseData<List<MenuAndButtonTreeResponse>> roleBindMenuList(@Validated(SysRoleRequest.roleBindMenuList.class) SysRoleRequest sysRoleRequest) {
        List<MenuAndButtonTreeResponse> treeResponseList = menuServiceApi.getRoleBindMenuList(sysRoleRequest);
        return new SuccessResponseData<>(treeResponseList);
    }

    /**
     * 获取角色分配操作权限，绑定情况列表
     *
     * @author liaoxiting
     * @date 2022/9/28 17:23
     */
    @GetResource(name = "获取角色分配操作权限，绑定情况列表", path = "/sysMenu/roleBindOperateList")
    public ResponseData<List<MenuAndButtonTreeResponse>> roleBindOperateList(@Validated(SysRoleRequest.roleBindMenuList.class) SysRoleRequest sysRoleRequest) {
        List<MenuAndButtonTreeResponse> treeResponseList = menuServiceApi.getRoleBindOperateList(sysRoleRequest);
        return new SuccessResponseData<>(treeResponseList);
    }

    /**
     * 角色权限界面：角色绑定菜单权限
     *
     * @author liaoxiting
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "角色权限界面：角色绑定菜单权限", path = "/sysRole/grantRoleMenus")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantRoleMenus(@RequestBody @Validated(SysRoleRequest.grantRoleMenus.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantRoleMenus(sysRoleRequest));
    }

    /**
     * 角色权限界面，角色绑定操作权限
     *
     * @author liaoxiting
     * @date 2022/9/29 11:10
     */
    @PostResource(name = "角色权限界面，角色绑定操作权限", path = "/sysRole/grantButton")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantButton(@RequestBody @Validated(SysRoleRequest.grantButton.class) SysRoleRequest sysRoleRequest) {
        List<MenuAndButtonTreeResponse> menuAndButtonTreeResponses = sysRoleService.grantButton(sysRoleRequest);
        return new SuccessResponseData<>(menuAndButtonTreeResponses);
    }

    /**
     * 角色权限界面：角色绑定菜单权限（全选操作）
     *
     * @author liaoxiting
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "角色权限界面：角色绑定菜单权限（全选操作）", path = "/sysRole/grantRoleMenus/grantAll")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantRoleMenusGrantAll(@RequestBody @Validated(SysRoleRequest.grantAll.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantRoleMenusGrantAll(sysRoleRequest));
    }

    /**
     * 角色权限界面：角色绑定操作权限（全选操作）
     *
     * @author liaoxiting
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "角色权限界面：角色绑定操作权限（全选操作）", path = "/sysRole/grantButton/grantAll")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantButtonGrantAll(@RequestBody @Validated(SysRoleRequest.grantAll.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantButtonGrantAll(sysRoleRequest));
    }

    /**
     * 角色绑定接口数据V2
     *
     * @author liaoxiting
     * @date 2021/8/10 18:23
     */
    @PostResource(name = "角色绑定接口数据V2", path = "/sysRole/grantResourceV2")
    public ResponseData<?> grantResourceV2(@RequestBody @Validated(SysRoleRequest.grantResourceV2.class) SysRoleRequest sysRoleRequest) {
        sysRoleResourceService.grantResourceV2(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 角色绑定所有接口数据
     *
     * @author liaoxiting
     * @date 2021/8/10 18:23
     */
    @PostResource(name = "角色绑定所有接口数据", path = "/sysRole/grantResourceV2/grantAll")
    public ResponseData<?> grantResourceV2GrantAll(@RequestBody @Validated(SysRoleRequest.grantAll.class) SysRoleRequest sysRoleRequest) {
        this.sysRoleService.grantResourceV2GrantAll(sysRoleRequest);
        return new SuccessResponseData<>();
    }

}
