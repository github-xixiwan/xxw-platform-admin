package com.xxw.platform.plugin.system.business.menu.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuResourceRequest;
import com.xxw.platform.plugin.system.business.menu.service.SysMenuResourceService;
import com.xxw.platform.plugin.system.business.resource.pojo.ResourceTreeNode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单资源控制器
 *
 * @author liaoxiting
 * @date 2021/8/8 22:38
 */
@RestController
@ApiResource(name = "菜单资源控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class SysMenuResourceController {

    @Resource
    private SysMenuResourceService sysMenuResourceService;

    /**
     * 获取菜单的资源分配列表
     *
     * @author liaoxiting
     * @date 2021/8/8 22:38
     */
    @GetResource(name = "获取菜单的资源分配列表", path = "/sysMenuResource/getMenuResourceList")
    public ResponseData<List<ResourceTreeNode>> getMenuResourceList(@Validated(value = SysMenuResourceRequest.list.class) SysMenuResourceRequest sysMenuResourceRequest) {
        List<ResourceTreeNode> menuResourceTree = sysMenuResourceService.getMenuResourceTree(sysMenuResourceRequest.getBusinessId());
        return new SuccessResponseData<>(menuResourceTree);
    }

    /**
     * 设置菜单资源绑定
     *
     * @author liaoxiting
     * @date 2021/8/10 11:55
     */
    @PostResource(name = "设置菜单资源绑定", path = "/sysMenuResource/addMenuResourceBind")
    @BusinessLog
    public ResponseData<?> addMenuResourceBind(@RequestBody @Validated(value = SysMenuResourceRequest.add.class) SysMenuResourceRequest sysMenuResourceRequest) {
        sysMenuResourceService.addMenuResourceBind(sysMenuResourceRequest);
        return new SuccessResponseData<>();
    }

}
