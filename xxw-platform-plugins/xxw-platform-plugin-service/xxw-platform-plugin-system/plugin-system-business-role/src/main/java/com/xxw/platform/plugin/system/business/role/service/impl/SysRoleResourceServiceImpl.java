package com.xxw.platform.plugin.system.business.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.enums.DbTypeEnum;
import com.xxw.platform.frame.common.util.ResourceCodeUtil;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.config.api.InitConfigApi;
import com.xxw.platform.plugin.db.api.context.DbOperatorContext;
import com.xxw.platform.plugin.system.api.ResourceServiceApi;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleResourceDTO;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.business.role.entity.SysRoleResource;
import com.xxw.platform.plugin.system.business.role.mapper.SysRoleResourceMapper;
import com.xxw.platform.plugin.system.business.role.service.SysRoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色菜单service接口实现类
 *
 * @author liaoxiting
 * @date 2020/11/5 上午11:32
 */
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements SysRoleResourceService {

    @Resource(name = "roleResourceCacheApi")
    private CacheOperatorApi<List<String>> roleResourceCacheApi;

    @Resource
    private ResourceServiceApi resourceServiceApi;

    @Resource
    private InitConfigApi initConfigApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantResource(SysRoleRequest sysRoleRequest) {

        Long roleId = sysRoleRequest.getRoleId();

        // 删除所拥有角色关联的资源
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleResource::getRoleId, roleId);
        this.remove(queryWrapper);

        // 清除角色缓存资源
        roleResourceCacheApi.remove(String.valueOf(roleId));

        // 授权资源
        List<String> codeList = sysRoleRequest.getGrantResourceList();
        List<SysRoleResourceDTO> list = new ArrayList<>();
        for (String resCode : codeList) {
            SysRoleResourceDTO sysRoleResourceDTO = new SysRoleResourceDTO();
            sysRoleResourceDTO.setResourceCode(resCode);
            list.add(sysRoleResourceDTO);
        }
        this.batchSaveResCodes(roleId, list);
    }

    @Override
    public void grantResourceV2(SysRoleRequest sysRoleRequest) {
        // 先将该业务下，模块下的所有资源删除掉
        List<String> modularTotalResource = sysRoleRequest.getModularTotalResource();
        if (ObjectUtil.isNotEmpty(modularTotalResource)) {
            LambdaUpdateWrapper<SysRoleResource> wrapper = new LambdaUpdateWrapper<>();
            wrapper.in(SysRoleResource::getResourceCode, modularTotalResource);
            wrapper.eq(SysRoleResource::getRoleId, sysRoleRequest.getRoleId());
            this.remove(wrapper);
        }

        // 再将该业务下，需要绑定的资源添加上
        List<String> selectedResource = sysRoleRequest.getSelectedResource();
        if (ObjectUtil.isNotEmpty(selectedResource)) {
            ArrayList<SysRoleResource> sysRoleResources = new ArrayList<>();
            for (String resourceCode : selectedResource) {
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setRoleId(sysRoleRequest.getRoleId());
                sysRoleResource.setResourceCode(resourceCode);
                sysRoleResource.setResourceBizType(resourceServiceApi.getResourceBizTypeByCode(resourceCode));
                sysRoleResources.add(sysRoleResource);
            }
            this.saveBatch(sysRoleResources, sysRoleResources.size());
        }

        // 清除角色绑定的资源缓存
        roleResourceCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleResourceListByResourceIds(List<Long> resourceIds) {

        // 查询资源包含的角色
        LambdaQueryWrapper<SysRoleResource> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.select(SysRoleResource::getRoleId);
        wrapper1.in(SysRoleResource::getResourceCode, resourceIds);
        wrapper1.groupBy(SysRoleResource::getRoleId);
        List<SysRoleResource> toGetRoles = this.list(wrapper1);
        List<Long> roleIds = toGetRoles.stream().map(SysRoleResource::getRoleId).collect(Collectors.toList());
        for (Long roleId : roleIds) {
            // 清除角色绑定的资源缓存
            roleResourceCacheApi.remove(String.valueOf(roleId));
        }

        // 清除资源
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleResource::getResourceCode, resourceIds);
        this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleResourceListByRoleId(Long roleId, Integer resourceBizType) {
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleResource::getRoleId, roleId);
        queryWrapper.eq(ObjectUtil.isNotEmpty(resourceBizType), SysRoleResource::getResourceBizType, resourceBizType);
        this.remove(queryWrapper);

        // 清除角色绑定的资源缓存
        roleResourceCacheApi.remove(String.valueOf(roleId));

    }

    @Override
    public void quickSaveAll(List<SysRoleResource> sysRoleResourceList) {
        DbTypeEnum currentDbType = DbOperatorContext.me().getCurrentDbType();
        if (DbTypeEnum.MYSQL.equals(currentDbType)) {
            // 分批插入数据
            List<List<SysRoleResource>> split = ListUtil.split(sysRoleResourceList, RuleConstants.DEFAULT_BATCH_INSERT_SIZE);
            for (List<SysRoleResource> sysRoleResources : split) {
                this.getBaseMapper().insertBatchSomeColumn(sysRoleResources);
            }
        } else {
            this.saveBatch(sysRoleResourceList, sysRoleResourceList.size());
        }
    }

    /**
     * 批量保存角色和资源的绑定
     *
     * @author liaoxiting
     * @date 2022/9/29 14:34
     */
    @Override
    public void batchSaveResCodes(Long roleId, List<SysRoleResourceDTO> totalResource) {
        ArrayList<SysRoleResource> sysRoleResourceList = new ArrayList<>();

        if (ObjectUtil.isNotEmpty(totalResource)) {
            for (SysRoleResourceDTO resCode : totalResource) {
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setRoleId(roleId);
                sysRoleResource.setResourceCode(resCode.getResourceCode());
                sysRoleResource.setResourceBizType(resCode.getResourceBizType());
                sysRoleResourceList.add(sysRoleResource);
            }

            DbTypeEnum currentDbType = DbOperatorContext.me().getCurrentDbType();
            if (DbTypeEnum.MYSQL.equals(currentDbType)) {
                List<List<SysRoleResource>> split = ListUtil.split(sysRoleResourceList, RuleConstants.DEFAULT_BATCH_INSERT_SIZE);
                for (List<SysRoleResource> sysRoleResources : split) {
                    this.getBaseMapper().insertBatchSomeColumn(sysRoleResources);
                }
            } else {
                this.saveBatch(sysRoleResourceList);
            }
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

        // 获取所有角色资源表信息
        List<SysRoleResource> list = this.list();

        // 批量更新资源编码
        for (SysRoleResource sysRoleResource : list) {
            String newResourceCode = ResourceCodeUtil.replace(sysRoleResource.getResourceCode(), newAppCode);
            sysRoleResource.setResourceCode(newResourceCode);
        }

        this.updateBatchById(list);

    }

}
