package com.xxw.platform.plugin.system.business.menu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.util.ResourceCodeUtil;
import com.xxw.platform.plugin.config.api.InitConfigApi;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuResourceRequest;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuResource;
import com.xxw.platform.plugin.system.business.menu.mapper.SysMenuResourceMapper;
import com.xxw.platform.plugin.system.business.menu.service.SysMenuResourceService;
import com.xxw.platform.plugin.system.business.resource.pojo.ResourceTreeNode;
import com.xxw.platform.plugin.system.business.resource.service.SysResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统资源信息关联
 *
 * @author liaoxiting
 * @date 2021/8/8 21:38
 */
@Service
public class SysMenuResourceServiceImpl extends ServiceImpl<SysMenuResourceMapper, SysMenuResource> implements SysMenuResourceService {

    @Resource
    private SysResourceService sysResourceService;

    @Resource
    private InitConfigApi initConfigApi;

    @Override
    public List<ResourceTreeNode> getMenuResourceTree(Long businessId) {
        LambdaQueryWrapper<SysMenuResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenuResource::getBusinessId, businessId);
        List<SysMenuResource> list = this.list(wrapper);

        List<String> resourceCodes = list.stream().map(SysMenuResource::getResourceCode).collect(Collectors.toList());
        return sysResourceService.getResourceList(resourceCodes, true, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMenuResourceBind(SysMenuResourceRequest sysMenuResourceRequest) {
        // 先将该业务下，模块下的所有资源删除掉
        List<String> modularTotalResource = sysMenuResourceRequest.getModularTotalResource();
        if (ObjectUtil.isNotEmpty(modularTotalResource)) {
            LambdaUpdateWrapper<SysMenuResource> wrapper = new LambdaUpdateWrapper<>();
            wrapper.in(SysMenuResource::getResourceCode, modularTotalResource);
            wrapper.eq(SysMenuResource::getBusinessId, sysMenuResourceRequest.getBusinessId());
            this.remove(wrapper);
        }

        // 再将该业务下，需要绑定的资源添加上
        List<String> selectedResource = sysMenuResourceRequest.getSelectedResource();
        if (ObjectUtil.isNotEmpty(selectedResource)) {
            ArrayList<SysMenuResource> menuResources = new ArrayList<>();
            for (String resourceCode : selectedResource) {
                SysMenuResource sysMenuResource = new SysMenuResource();
                sysMenuResource.setBusinessType(sysMenuResourceRequest.getBusinessType());
                sysMenuResource.setBusinessId(sysMenuResourceRequest.getBusinessId());
                sysMenuResource.setResourceCode(resourceCode);
                menuResources.add(sysMenuResource);
            }
            this.saveBatch(menuResources, menuResources.size());
        }
    }

    @Override
    public void updateNewAppCode(Boolean decisionFirstStart, String newAppCode) {

        // 判断是否是第一次启动项目
        if (decisionFirstStart) {
            Boolean initConfigFlag = initConfigApi.getInitConfigFlag();
            if (initConfigFlag) {
                return;
            }
        }

        // 获取所有菜单资源表信息
        List<SysMenuResource> list = this.list();

        // 批量更新资源编码
        for (SysMenuResource sysMenuResource : list) {
            String newResourceCode = ResourceCodeUtil.replace(sysMenuResource.getResourceCode(), newAppCode);
            sysMenuResource.setResourceCode(newResourceCode);
        }

        this.updateBatchById(list);

    }

}
