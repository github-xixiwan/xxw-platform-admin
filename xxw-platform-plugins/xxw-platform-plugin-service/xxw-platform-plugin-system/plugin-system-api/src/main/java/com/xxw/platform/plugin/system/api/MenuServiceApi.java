package com.xxw.platform.plugin.system.api;

import com.xxw.platform.plugin.system.api.enums.AntdvFrontTypeEnum;
import com.xxw.platform.plugin.system.api.pojo.login.v3.IndexMenuInfo;
import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuButtonDTO;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;

import java.util.List;
import java.util.Set;

/**
 * 菜单api
 *
 * @author liaoxiting
 * @date 2020/11/24 21:37
 */
public interface MenuServiceApi {

    /**
     * 根据应用编码判断该机构下是否有状态为正常的菜单
     *
     * @param appCode 应用编码
     * @return 该应用下是否有正常菜单，true是，false否
     * @author liaoxiting
     * @date 2020/11/24 21:37
     */
    boolean hasMenu(String appCode);

    /**
     * 获取当前用户所拥有菜单对应的appCode列表
     *
     * @author liaoxiting
     * @date 2021/4/21 15:40
     */
    List<String> getUserAppCodeList();

    /**
     * 获取菜单所有的父级菜单ID
     *
     * @param menuIds 菜单列表
     * @return {@link Set< Long>}
     * @author liaoxiting
     * @date 2021/6/22 上午10:11
     **/
    Set<Long> getMenuAllParentMenuId(Set<Long> menuIds);

    /**
     * 通过按钮id获取按钮code
     *
     * @author liaoxiting
     * @date 2021/8/11 10:40
     */
    String getMenuButtonCodeByButtonId(Long buttonId);

    /**
     * 通过菜单或按钮id的集合，获取拥有资源编码的集合
     *
     * @author liaoxiting
     * @date 2021/8/11 14:25
     */
    List<String> getResourceCodesByBusinessId(List<Long> businessIds);

    /**
     * 构建Antdv3版本的菜单和权限信息
     *
     * @param menuFrontType 菜单的前后台类型，如果没传递，默认查前台菜单
     * @author liaoxiting
     * @date 2022/4/8 15:59
     */
    List<IndexMenuInfo> buildAuthorities(Integer menuFrontType, Boolean devopsFlag);

    /**
     * 获取角色绑定菜单和按钮权限的树
     *
     * @author liaoxiting
     * @date 2021/8/10 22:23
     */
    List<MenuAndButtonTreeResponse> getRoleMenuAndButtons(SysRoleRequest sysRoleRequest);

    /**
     * 获取角色绑定的菜单列表
     *
     * @author liaoxiting
     * @date 2022/9/28 16:06
     */
    List<MenuAndButtonTreeResponse> getRoleBindMenuList(SysRoleRequest sysRoleRequest);

    /**
     * 获取角色绑定的操作权限列表
     *
     * @author liaoxiting
     * @date 2022/9/28 17:26
     */
    List<MenuAndButtonTreeResponse> getRoleBindOperateList(SysRoleRequest sysRoleRequest);

    /**
     * 获取所有菜单id集合
     *
     * @param antdvFrontTypeEnum 前台还是后台菜单
     * @author liaoxiting
     * @date 2022/9/29 9:56
     */
    List<Long> getTotalMenuIdList(AntdvFrontTypeEnum antdvFrontTypeEnum);

    /**
     * 获取所有菜单按钮id集合
     *
     * @param antdvFrontTypeEnum 前台还是后台菜单
     * @author liaoxiting
     * @date 2022/9/29 9:56
     */
    List<SysMenuButtonDTO> getTotalMenuButtonIdList(AntdvFrontTypeEnum antdvFrontTypeEnum);

    /**
     * 获取当前用户拥有的菜单类型
     * <p>
     * 判断是拥有前台菜单，还是后台菜单，还是都有
     *
     * @param menuIds 用户所拥有的的所有菜单集合
     * @author liaoxiting
     * @date 2022/10/13 21:38
     */
    AntdvFrontTypeEnum getUserMenuType(List<Long> menuIds);

}
