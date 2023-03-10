package com.xxw.platform.plugin.system.business.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleDataScopeRequest;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.role.entity.SysRoleDataScope;

import java.util.List;

/**
 * 系统角色数据范围service接口
 *
 * @author liaoxiting
 * @date 2020/11/5 上午11:21
 */
public interface SysRoleDataScopeService extends IService<SysRoleDataScope> {

    /**
     * 授权数据
     *
     * @param sysRoleRequest 授权参数
     * @author liaoxiting
     * @date 2020/11/5 上午11:20
     */
    void grantDataScope(SysRoleRequest sysRoleRequest);

    /**
     * 根据角色id获取角色数据范围集合
     *
     * @param roleIdList 角色id集合
     * @return 数据范围id集合
     * @author liaoxiting
     * @date 2020/11/5 上午11:21
     */
    List<Long> getRoleDataScopeIdList(List<Long> roleIdList);

    /**
     * 新增
     *
     * @param sysRoleDataScopeRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void add(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 删除
     *
     * @param sysRoleDataScopeRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void del(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 根据角色id 删除角色数据范围
     *
     * @param roleId 角色id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void delByRoleId(Long roleId);
    /**
     * 修改
     *
     * @param sysRoleDataScopeRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void edit(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 查询-详情
     *
     * @param sysRoleDataScopeRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    SysRoleDataScope detail(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 查询-列表
     *
     * @param sysRoleDataScopeRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    List<SysRoleDataScope> findList(SysRoleDataScopeRequest sysRoleDataScopeRequest);
}
