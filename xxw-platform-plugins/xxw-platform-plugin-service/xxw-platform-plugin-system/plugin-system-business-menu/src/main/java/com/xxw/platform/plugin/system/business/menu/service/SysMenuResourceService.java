package com.xxw.platform.plugin.system.business.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuResourceRequest;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuResource;
import com.xxw.platform.plugin.system.business.resource.pojo.ResourceTreeNode;

import java.util.List;

/**
 * 菜单资源信息
 *
 * @author liaoxiting
 * @date 2021/8/8 21:38
 */
public interface SysMenuResourceService extends IService<SysMenuResource> {

    /**
     * 获取菜单或菜单按钮绑定资源的树
     *
     * @param businessId 业务id，菜单或按钮id
     * @return 资源树列表
     * @author liaoxiting
     * @date 2021/8/8 21:56
     */
    List<ResourceTreeNode> getMenuResourceTree(Long businessId);

    /**
     * 添加菜单和资源的绑定
     *
     * @author liaoxiting
     * @date 2021/8/10 13:58
     */
    void addMenuResourceBind(SysMenuResourceRequest sysMenuResourceRequest);

    /**
     * 更新本表的所有资源编码，改为新的应用code前缀
     *
     * @param decisionFirstStart 判断是否是第一次启动，参数传true，则判断必须是第一次启动才执行update操作
     * @param newAppCode         新应用编码
     * @author liaoxiting
     * @date 2022/11/16 23:13
     */
    void updateNewAppCode(Boolean decisionFirstStart, String newAppCode);

}
