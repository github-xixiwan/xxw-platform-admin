package com.xxw.platform.plugin.system.api;

import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.role.dto.*;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;

import java.util.List;
import java.util.Set;

/**
 * 角色服务对外部模块的接口
 *
 * @author liaoxiting
 * @date 2020/11/5 19:17
 */
public interface RoleServiceApi {

    /**
     * 获取角色，通过传递角色id列表
     *
     * @param roleIds 角色id列表
     * @return 角色信息列表
     * @author liaoxiting
     * @date 2020/11/21 9:17
     */
    List<SysRoleDTO> getRolesByIds(List<Long> roleIds);

    /**
     * 获取角色对应的组织机构范围集合
     *
     * @param roleIds 角色id集合
     * @return 组织机构id集合
     * @author liaoxiting
     * @date 2020/11/21 9:56
     */
    List<Long> getRoleDataScopes(List<Long> roleIds);

    /**
     * 获取某些角色对应的菜单id集合
     *
     * @param roleIds 角色id集合
     * @return 菜单id集合
     * @author liaoxiting
     * @date 2020/11/22 23:00
     */
    List<Long> getMenuIdsByRoleIds(List<Long> roleIds);

    /**
     * 获取角色的资源code集合
     *
     * @param roleIdList 角色id集合
     * @return 资源code集合
     * @author liaoxiting
     * @date 2020/11/5 上午11:17
     */
    Set<String> getRoleResourceCodeList(List<Long> roleIdList);

    /**
     * 获取角色的资源code集合
     *
     * @param roleIdList 角色id集合
     * @return 资源code集合
     * @author liaoxiting
     * @date 2020/11/5 上午11:17
     */
    List<SysRoleResourceDTO> getRoleResourceList(List<Long> roleIdList);

    /**
     * 获取角色对应的按钮编码集合
     *
     * @param roleIdList 角色id集合
     * @return 角色拥有的按钮编码集合
     * @author liaoxiting
     * @date 2021/1/9 11:08
     */
    Set<String> getRoleButtonCodes(List<Long> roleIdList);

    /**
     * 获取角色拥有的菜单
     *
     * @param roleIdList 角色集合
     * @author liaoxiting
     * @date 2021/1/9 17:33
     */
    List<SysRoleMenuDTO> getRoleMenuList(List<Long> roleIdList);

    /**
     * 获取角色拥有的菜单按钮
     *
     * @param roleIdList 角色集合
     * @author liaoxiting
     * @date 2021/1/9 17:33
     */
    List<SysRoleMenuButtonDTO> getRoleMenuButtonList(List<Long> roleIdList);

    /**
     * 获取所有角色列表
     *
     * @author liaoxiting
     * @date 2022/6/8 14:58
     */
    List<SysRoleDTO> getRoleSelectList(SysRoleRequest sysRoleRequest);

    /**
     * 添加管理员角色
     *
     * @author liaoxiting
     * @date 2022/6/8 14:58
     */
    void addAdminRole(SysRoleRequest sysRoleRequest);

    /**
     * 角色权限界面，绑定角色的操作权限，全选操作
     *
     * @author liaoxiting
     * @date 2022/9/29 10:46
     */
    List<MenuAndButtonTreeResponse> grantButtonGrantAll(SysRoleRequest sysRoleRequest);

    /**
     * 删除系统角色
     *
     * @param sysRoleRequest 删除参数
     * @author liaoxiting
     * @date 2020/11/5 上午11:14
     */
    void del(SysRoleRequest sysRoleRequest);

    /**
     * 角色绑定所有资源
     *
     * @author liaoxiting
     * @date 2022/9/29 14:05
     */
    void grantResourceV2GrantAll(SysRoleRequest sysRoleRequest);

    /**
     * 通过角色编码获取角色
     *
     * @author liaoxiting
     * @date 2022/10/21 0:18
     */
    SysRoleDTO getRoleByCode(String roleCode);

    /**
     * 获取角色的菜单、按钮和资源信息
     *
     * @param roleIdList 角色id集合
     * @author liaoxiting
     * @date 2022/10/25 15:53
     */
    RoleAuthorizeInfo getRoleAuthorizeInfo(List<Long> roleIdList);

}
