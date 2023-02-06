package com.xxw.platform.plugin.system.business.role.controller;

import cn.hutool.core.collection.ListUtil;
import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleDTO;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;
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
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    /**
     * 添加系统角色
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:38
     */
    @PostResource(name = "添加角色", path = "/sysRole/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(SysRoleRequest.add.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.add(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统角色
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:48
     */
    @PostResource(name = "角色删除", path = "/sysRole/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(SysRoleRequest.delete.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.del(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统角色
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:49
     */
    @PostResource(name = "角色编辑", path = "/sysRole/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(SysRoleRequest.edit.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.edit(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看系统角色
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:50
     */
    @GetResource(name = "角色查看", path = "/sysRole/detail")
    public ResponseData<SysRoleDTO> detail(@Validated(SysRoleRequest.detail.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.detail(sysRoleRequest));
    }

    /**
     * 查询系统角色
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:19
     */
    @GetResource(name = "查询角色", path = "/sysRole/page")
    public ResponseData<PageResult<SysRole>> page(SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.findPage(sysRoleRequest));
    }

    /**
     * 角色授权资源
     *
     * @author liaoxiting
     * @date 2020/11/22 19:51
     */
    @PostResource(name = "角色授权资源", path = "/sysRole/grantResource")
    public ResponseData<?> grantResource(@RequestBody @Validated(SysRoleRequest.grantResource.class) SysRoleRequest sysRoleParam) {
        sysRoleResourceService.grantResource(sysRoleParam);
        return new SuccessResponseData<>();
    }

    /**
     * 角色授权菜单和按钮
     *
     * @author liaoxiting
     * @date 2021/1/9 18:04
     */
    @PostResource(name = "角色授权菜单和按钮", path = "/sysRole/grantMenuAndButton")
    public ResponseData<?> grantMenuAndButton(@RequestBody @Validated(SysRoleRequest.grantMenuButton.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.grantMenuAndButton(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 角色授权菜单，新版界面用
     *
     * @author liaoxiting
     * @date 2021/8/11 9:58
     */
    @PostResource(name = "角色授权菜单", path = "/sysRole/grantMenu")
    public ResponseData<?> grantMenu(@RequestBody @Validated(SysRoleRequest.grantMenu.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.grantMenu(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 角色绑定或取消绑定菜单和按钮
     *
     * @author liaoxiting
     * @date 2021/8/11 9:58
     */
    @PostResource(name = "角色绑定或取消绑定菜单和按钮", path = "/sysRole/grantMenusAndButtons")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantMenusAndButtons(@RequestBody @Validated(SysRoleRequest.grantMenusAndButtons.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantMenusAndButtonsAndGetResult(sysRoleRequest));
    }

    /**
     * 设置角色绑定的数据范围类型和数据范围
     *
     * @author liaoxiting
     * @date 2020/3/28 16:05
     */
    @PostResource(name = "设置角色绑定的数据范围类型和数据范围", path = "/sysRole/grantDataScope")
    public ResponseData<?> grantData(@RequestBody @Validated(SysRoleRequest.grantDataScope.class) SysRoleRequest sysRoleParam) {
        sysRoleService.grantDataScope(sysRoleParam);
        return new SuccessResponseData<>();
    }

    /**
     * 系统角色下拉（用于用户授权角色时选择）
     *
     * @author liaoxiting
     * @date 2020/11/6 13:49
     */
    @GetResource(name = "角色下拉", path = "/sysRole/dropDown")
    public ResponseData<List<SimpleDict>> dropDown() {
        return new SuccessResponseData<>(sysRoleService.dropDown());
    }

    /**
     * 拥有菜单
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:58
     */
    @GetResource(name = "角色拥有菜单", path = "/sysRole/getRoleMenus")
    public ResponseData<List<Long>> getRoleMenus(@Validated(SysRoleRequest.detail.class) SysRoleRequest sysRoleRequest) {
        Long roleId = sysRoleRequest.getRoleId();
        return new SuccessResponseData<>(sysRoleService.getMenuIdsByRoleIds(ListUtil.toList(roleId)));
    }

    /**
     * 拥有数据
     *
     * @author liaoxiting
     * @date 2020/11/5 上午10:59
     */
    @GetResource(name = "角色拥有数据", path = "/sysRole/getRoleDataScope")
    public ResponseData<List<Long>> getRoleDataScope(@Validated(SysRoleRequest.detail.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.getRoleDataScope(sysRoleRequest));
    }

    /**
     * 获取角色下拉列表
     *
     * @author liaoxiting
     * @date 2022/6/8 14:57
     */
    @GetResource(name = "获取角色下拉列表", path = "/sysRole/getRoleSelectList")
    public ResponseData<List<SysRoleDTO>> getRoleSelectList(SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.getRoleSelectList(sysRoleRequest));
    }

    /**
     * 获取角色信息集合
     *
     * @author liaoxiting
     * @date 2022/11/2 14:08
     */
    @PostResource(name = "获取角色信息集合", path = "/sysRole/getRoleInfoListByIds")
    public ResponseData<List<SysRoleDTO>> getRoleInfoListByIds(@RequestBody @Validated(SysRoleRequest.batchQuery.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.getRolesByIds(sysRoleRequest.getRoleIdList()));
    }

}
