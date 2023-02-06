package com.xxw.platform.plugin.system.business.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import com.xxw.platform.plugin.system.api.pojo.resource.LayuiApiResourceTreeNode;
import com.xxw.platform.plugin.system.api.pojo.resource.ResourceRequest;
import com.xxw.platform.plugin.system.business.resource.entity.SysResource;
import com.xxw.platform.plugin.system.business.resource.pojo.ResourceTreeNode;

import java.util.List;

/**
 * 资源服务类
 *
 * @author liaoxiting
 * @date 2020/11/24 19:56
 */
public interface SysResourceService extends IService<SysResource> {

    /**
     * 获取资源分页列表
     *
     * @param resourceRequest 请求参数
     * @return 返回结果
     * @author liaoxiting
     * @date 2020/11/24 20:45
     */
    PageResult<SysResource> findPage(ResourceRequest resourceRequest);

    /**
     * 通过应用code获取获取资源下拉列表
     * <p>
     * 只获取菜单资源
     *
     * @param resourceRequest 请求参数
     * @return 响应下拉结果
     * @author liaoxiting
     * @date 2020/11/24 20:45
     */
    List<SysResource> findList(ResourceRequest resourceRequest);

    /**
     * 获取角色绑定的资源树列表，用于分配接口权限
     *
     * @param roleId          角色id
     * @param treeBuildFlag   true-带树形结构，false-不组装树形结构的
     * @param resourceBizType 资源的类型，1-业务类，2-系统类
     * @author liaoxiting
     * @date 2022/9/28 23:46
     */
    List<ResourceTreeNode> getRoleResourceTree(Long roleId, Boolean treeBuildFlag, Integer resourceBizType);

    /**
     * 获取资源绑定列表（业务通用）
     *
     * @param resourceCodes   业务已经绑定的资源的编码集合
     * @param treeBuildFlag   是否要构建成树
     * @param resourceBizType 资源的类型，1-业务类，2-系统类
     * @author liaoxiting
     * @date 2021/8/8 22:24
     */
    List<ResourceTreeNode> getResourceList(List<String> resourceCodes, Boolean treeBuildFlag, Integer resourceBizType);

    /**
     * 获取资源树列表，用于生成api接口
     *
     * @return 资源树列表
     * @author liaoxiting
     * @date 2020/12/18 15:06
     */
    List<LayuiApiResourceTreeNode> getApiResourceTree(ResourceRequest resourceRequest);

    /**
     * 获取资源的详情
     *
     * @param resourceRequest 请求参数
     * @return 资源详情
     * @author liaoxiting
     * @date 2020/12/18 16:04
     */
    ResourceDefinition getApiResourceDetail(ResourceRequest resourceRequest);

    /**
     * 删除某个项目的所有资源
     *
     * @param projectCode 项目编码，一般为spring application name
     * @author liaoxiting
     * @date 2020/11/24 20:46
     */
    void deleteResourceByProjectCode(String projectCode);

    /**
     * 更新资源编码前缀，将guns$前缀改为新的
     *
     * @author liaoxiting
     * @date 2022/11/17 0:17
     */
    void updateResourceAppCode(String newAppCode);

}
