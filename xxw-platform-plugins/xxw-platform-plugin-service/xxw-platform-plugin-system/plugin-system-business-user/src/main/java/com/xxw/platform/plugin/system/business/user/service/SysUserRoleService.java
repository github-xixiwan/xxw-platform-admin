package com.xxw.platform.plugin.system.business.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.UserRoleRequest;
import com.xxw.platform.plugin.system.business.user.entity.SysUserRole;

import java.util.List;

/**
 * 系统用户角色service接口
 *
 * @author liaoxiting
 * @date 2021/2/3 15:23
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 新增
     *
     * @param userRoleRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void add(UserRoleRequest userRoleRequest);

    /**
     * 删除
     *
     * @param userRoleRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void del(UserRoleRequest userRoleRequest);

    /**
     * 根据用户id删除角色
     *
     * @param userId 用户id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void delByUserId(Long userId);

    /**
     * 修改
     *
     * @param userRoleRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void edit(UserRoleRequest userRoleRequest);

    /**
     * 查询-详情
     *
     * @param userRoleRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    SysUserRole detail(UserRoleRequest userRoleRequest);

    /**
     * 查询-列表
     *
     * @param userRoleRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    List<SysUserRole> findList(UserRoleRequest userRoleRequest);

    /**
     * 根据userId查询列表
     *
     * @param userId 用户id
     * @author liaoxiting
     * @date 2021/2/3 15:06
     */
    List<SysUserRole> findListByUserId(Long userId);

    /**
     * 根据userId查询角色集合
     *
     * @param userId 用户id
     * @return 用户角色集合
     * @author liaoxiting
     * @date 2021/2/3 15:09
     */
    List<Long> findRoleIdsByUserId(Long userId);

    /**
     * 角色分配
     *
     * @param sysUserRequest 请求参数
     * @author liaoxiting
     * @date 2021/2/3 15:16
     */
    void assignRoles(SysUserRequest sysUserRequest);

}
