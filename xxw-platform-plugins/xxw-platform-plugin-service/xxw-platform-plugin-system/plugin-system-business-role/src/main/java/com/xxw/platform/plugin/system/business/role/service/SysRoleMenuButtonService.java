package com.xxw.platform.plugin.system.business.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.business.role.entity.SysRoleMenuButton;

import java.util.List;

/**
 * 角色按钮关联 服务类
 *
 * @author liaoxiting
 * @date 2021/01/09 11:48
 */
public interface SysRoleMenuButtonService extends IService<SysRoleMenuButton> {

    /**
     * 批量保存角色绑定的菜单按钮
     *
     * @param roleMenuButtons 角色按钮关联列表
     * @author liaoxiting
     * @date 2022/10/14 0:56
     */
    void batchSaveRoleMenuButton(List<SysRoleMenuButton> roleMenuButtons);

}
