package com.xxw.platform.plugin.system.business.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuButtonRequest;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuButton;

/**
 * 系统菜单按钮service接口
 *
 * @author liaoxiting
 * @date 2021/1/9 11:04
 */
public interface SysMenuButtonService extends IService<SysMenuButton> {

    /**
     * 添加系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮请求参数
     * @author liaoxiting
     * @date 2021/1/9 11:28
     */
    void add(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 添加系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮请求参数
     * @author liaoxiting
     * @date 2021/1/9 11:28
     */
    void addDefaultButtons(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 删除单个系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮id
     * @author liaoxiting
     * @date 2021/1/9 12:14
     */
    void del(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 批量删除多个系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮id集合
     * @author liaoxiting
     * @date 2021/1/9 12:27
     */
    void delBatch(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 编辑系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮请求参数
     * @author liaoxiting
     * @date 2021/1/9 12:00
     */
    void edit(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 获取菜单按钮详情
     *
     * @param sysMenuButtonRequest 菜单按钮id
     * @return 菜单按钮详情
     * @author liaoxiting
     * @date 2021/1/9 11:53
     */
    SysMenuButton detail(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 分页获取菜单按钮列表
     *
     * @param sysMenuButtonRequest 菜单id
     * @return 菜单按钮列表
     * @author liaoxiting
     * @date 2021/1/9 12:53
     */
    PageResult<SysMenuButton> findPage(SysMenuButtonRequest sysMenuButtonRequest);

    /**
     * 根据菜单id删除该菜单下的所有按钮
     *
     * @param menuId 菜单id
     * @author liaoxiting
     * @date 2021/1/9 14:45
     */
    void deleteMenuButtonByMenuId(Long menuId);

}
