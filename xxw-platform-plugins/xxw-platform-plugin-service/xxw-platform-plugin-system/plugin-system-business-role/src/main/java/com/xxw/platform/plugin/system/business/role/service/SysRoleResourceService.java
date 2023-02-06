package com.xxw.platform.plugin.system.business.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleResourceDTO;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.role.entity.SysRoleResource;

import java.util.List;

/**
 * 系统角色菜单service接口
 *
 * @author liaoxiting
 * @date 2020/11/5 上午11:17
 */
public interface SysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 授权资源
     *
     * @param sysRoleRequest 授权参数
     * @author liaoxiting
     * @date 2020/11/5 上午11:17
     */
    void grantResource(SysRoleRequest sysRoleRequest);

    /**
     * 角色授权接口资源
     *
     * @author liaoxiting
     * @date 2021/8/10 18:28
     */
    void grantResourceV2(SysRoleRequest sysRoleRequest);

    /**
     * 根据资源id集合删除角色关联的资源
     *
     * @param resourceIds 资源id集合
     * @author liaoxiting
     * @date 2020/11/5 上午11:17
     */
    void deleteRoleResourceListByResourceIds(List<Long> resourceIds);

    /**
     * 根据角色id删除对应的角色资源信息
     *
     * @param roleId          角色id
     * @param resourceBizType 指定的资源类型，如果为空，则删除所有类型的
     * @author liaoxiting
     * @date 2020/11/5 上午11:18
     */
    void deleteRoleResourceListByRoleId(Long roleId, Integer resourceBizType);

    /**
     * 保存所有的角色资源
     *
     * @author liaoxiting
     * @date 2022/9/17 14:33
     */
    void quickSaveAll(List<SysRoleResource> sysRoleResourceList);

    /**
     * 批量保存角色和资源的绑定
     *
     * @author liaoxiting
     * @date 2022/9/29 14:34
     */
    void batchSaveResCodes(Long roleId, List<SysRoleResourceDTO> totalResourceCode);

    /**
     * 更新本表的资源编码，替换为新的app编码
     *
     * @param decisionFirstStart 判断是否是第一次启动，参数传true，则判断必须是第一次启动才执行update操作
     * @param newAppCode         新应用编码
     * @author liaoxiting
     * @date 2022/11/16 23:37
     */
    void updateNewAppCode(Boolean decisionFirstStart, String newAppCode);

}
