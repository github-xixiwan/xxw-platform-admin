package com.xxw.platform.plugin.system.business.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.RoleServiceApi;
import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleDTO;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;

import java.util.List;

/**
 * 系统角色service接口
 *
 * @author liaoxiting
 * @date 2020/11/5 上午11:12
 */
public interface SysRoleService extends IService<SysRole>, RoleServiceApi {

    /**
     * 添加系统角色
     *
     * @param sysRoleRequest 添加参数
     * @author liaoxiting
     * @date 2020/11/5 上午11:13
     */
    void add(SysRoleRequest sysRoleRequest);

    /**
     * 编辑系统角色
     *
     * @param sysRoleRequest 编辑参数
     * @author liaoxiting
     * @date 2020/11/5 上午11:14
     */
    void edit(SysRoleRequest sysRoleRequest);

    /**
     * 查看系统角色
     *
     * @param sysRoleRequest 查看参数
     * @return 系统角色
     * @author liaoxiting
     * @date 2020/11/5 上午11:14
     */
    SysRoleDTO detail(SysRoleRequest sysRoleRequest);

    /**
     * 查询系统角色
     *
     * @param sysRoleRequest 查询参数
     * @return 查询分页结果
     * @author liaoxiting
     * @date 2020/11/5 上午11:13
     */
    PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest);

    /**
     * 根据角色名模糊搜索系统角色列表
     *
     * @param sysRoleRequest 查询参数
     * @return 增强版hashMap，格式：[{"id":456, "name":"总经理(zjl)"}]
     * @author liaoxiting
     * @date 2020/11/5 上午11:13
     */
    List<SimpleDict> findList(SysRoleRequest sysRoleRequest);

    /**
     * 授权菜单和按钮
     *
     * @author liaoxiting
     * @date 2021/1/9 18:13
     */
    void grantMenuAndButton(SysRoleRequest sysRoleMenuButtonRequest);

    /**
     * 角色绑定菜单，新界面用
     *
     * @author liaoxiting
     * @date 2021/8/11 10:02
     */
    void grantMenu(SysRoleRequest sysRoleMenuButtonRequest);

    /**
     * 角色绑定按钮，新界面用
     *
     * @author liaoxiting
     * @date 2021/8/11 10:02
     */
    List<MenuAndButtonTreeResponse> grantButton(SysRoleRequest sysRoleMenuButtonRequest);

    /**
     * 授权数据范围（组织机构）
     *
     * @param sysRoleRequest 授权参数
     * @author liaoxiting
     * @date 2020/11/5 上午11:14
     */
    void grantDataScope(SysRoleRequest sysRoleRequest);

    /**
     * 系统角色下拉（用于授权角色时选择）
     *
     * @return 增强版hashMap，格式：[{"id":456, "code":"zjl", "name":"总经理"}]
     * @author liaoxiting
     * @date 2020/11/5 上午11:13
     */
    List<SimpleDict> dropDown();

    /***
     * 查询角色拥有数据
     *
     * @param sysRoleRequest 查询参数
     * @return 数据范围id集合
     * @author liaoxiting
     * @date 2020/11/5 上午11:15
     */
    List<Long> getRoleDataScope(SysRoleRequest sysRoleRequest);

    /**
     * 授权菜单和按钮集合
     *
     * @author liaoxiting
     * @date 2022/6/21 9:55
     */
    void grantMenusAndButtons(SysRoleRequest sysRoleRequest);

    /**
     * 授权菜单和按钮集合，并获取结果
     *
     * @author liaoxiting
     * @date 2022/6/27 17:08
     */
    List<MenuAndButtonTreeResponse> grantMenusAndButtonsAndGetResult(SysRoleRequest sysRoleRequest);

    /**
     * 角色权限界面，绑定角色的菜单权限
     *
     * @author liaoxiting
     * @date 2022/9/28 20:29
     */
    List<MenuAndButtonTreeResponse> grantRoleMenus(SysRoleRequest sysRoleRequest);

    /**
     * 角色权限界面，绑定角色的菜单权限，全选操作
     *
     * @author liaoxiting
     * @date 2022/9/29 9:43
     */
    List<MenuAndButtonTreeResponse> grantRoleMenusGrantAll(SysRoleRequest sysRoleRequest);

}
