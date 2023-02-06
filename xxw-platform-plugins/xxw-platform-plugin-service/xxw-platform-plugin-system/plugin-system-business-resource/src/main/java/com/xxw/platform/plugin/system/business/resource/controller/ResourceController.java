package com.xxw.platform.plugin.system.business.resource.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.system.api.pojo.resource.ResourceRequest;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.resource.entity.SysResource;
import com.xxw.platform.plugin.system.business.resource.pojo.ResourceTreeNode;
import com.xxw.platform.plugin.system.business.resource.service.SysResourceService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资源管理控制器
 *
 * @author liaoxiting
 * @date 2020/11/24 19:47
 */
@RestController
@ApiResource(name = "资源管理", resBizType = ResBizTypeEnum.SYSTEM)
public class ResourceController {

    @Resource
    private SysResourceService sysResourceService;

    /**
     * 获取资源列表
     *
     * @author liaoxiting
     * @date 2020/11/24 19:47
     */
    @GetResource(name = "获取资源列表", path = "/resource/pageList")
    public ResponseData<PageResult<SysResource>> pageList(ResourceRequest resourceRequest) {
        PageResult<SysResource> result = this.sysResourceService.findPage(resourceRequest);
        return new SuccessResponseData<>(result);
    }

    /**
     * 获取资源下拉列表（获取菜单资源）
     *
     * @author liaoxiting
     * @date 2020/11/24 19:51
     */
    @GetResource(name = "获取资源下拉列表", path = "/resource/getMenuResourceList")
    public ResponseData<List<SysResource>> getMenuResourceList(ResourceRequest resourceRequest) {
        List<SysResource> menuResourceList = this.sysResourceService.findList(resourceRequest);
        return new SuccessResponseData<>(menuResourceList);
    }

    /**
     * Layui版本--获取资源树列表，用于角色分配接口权限
     *
     * @author liaoxiting
     * @date 2021/1/9 15:07
     */
    @GetResource(name = "Layui版本--获取资源树列表，用于角色分配接口权限", path = "/resource/getRoleResourceTree")
    public List<ResourceTreeNode> getLateralTree(SysRoleRequest sysRoleRequest) {
        return sysResourceService.getRoleResourceTree(sysRoleRequest.getRoleId(), false, null);
    }

    /**
     * AntdVue版本--获取资源树列表，用于角色分配接口权限
     *
     * @author liaoxiting
     * @date 2021/1/9 15:07
     */
    @GetResource(name = "AntdVue版本--获取资源树列表，用于角色分配接口权限", path = "/resource/getRoleResourceTreeAntdv")
    public ResponseData<List<ResourceTreeNode>> getLateralTreeChildren(SysRoleRequest sysRoleRequest) {
        List<ResourceTreeNode> resourceLateralTree = sysResourceService.getRoleResourceTree(sysRoleRequest.getRoleId(), true, sysRoleRequest.getResourceBizType());
        return new SuccessResponseData<>(resourceLateralTree);
    }
}
