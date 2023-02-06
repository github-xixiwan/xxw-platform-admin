package com.xxw.platform.plugin.system.starter.init;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.business.resource.entity.SysResource;
import com.xxw.platform.plugin.system.business.resource.service.SysResourceService;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;
import com.xxw.platform.plugin.system.business.role.entity.SysRoleResource;
import com.xxw.platform.plugin.system.business.role.service.SysRoleResourceService;
import com.xxw.platform.plugin.system.business.role.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化admin管理员的服务
 *
 * @author liaoxiting
 * @date 2020/12/17 21:56
 */
@Service
public class InitAdminService {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysResourceService sysResourceService;

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    /**
     * 初始化超级管理员，超级管理员拥有最高权限
     *
     * @author liaoxiting
     * @date 2020/12/17 21:57
     */
    @Transactional(rollbackFor = Exception.class)
    public void initSuperAdmin() {

        // 找到超级管理员的角色id
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, SystemConstants.SUPER_ADMIN_ROLE_CODE);
        SysRole superAdminRole = sysRoleService.getOne(queryWrapper);

        // 删除这个角色绑定的所有资源
        LambdaUpdateWrapper<SysRoleResource> sysRoleResourceLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysRoleResourceLambdaUpdateWrapper.eq(SysRoleResource::getRoleId, superAdminRole.getRoleId());
        sysRoleResourceService.remove(sysRoleResourceLambdaUpdateWrapper);

        // 找到所有Resource，将所有资源赋给这个角色
        LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysResourceLambdaQueryWrapper.select(SysResource::getResourceCode, SysResource::getResourceBizType);
        List<SysResource> resources = sysResourceService.list(sysResourceLambdaQueryWrapper);

        ArrayList<SysRoleResource> sysRoleResources = new ArrayList<>();
        for (SysResource resource : resources) {
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(superAdminRole.getRoleId());
            sysRoleResource.setResourceCode(resource.getResourceCode());
            sysRoleResource.setResourceBizType(resource.getResourceBizType());
            sysRoleResources.add(sysRoleResource);
        }
        sysRoleResourceService.quickSaveAll(sysRoleResources);
    }

}
