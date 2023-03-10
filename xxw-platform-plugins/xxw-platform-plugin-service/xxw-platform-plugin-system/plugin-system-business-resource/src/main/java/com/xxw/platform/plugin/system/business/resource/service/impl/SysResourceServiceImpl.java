package com.xxw.platform.plugin.system.business.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.constants.TreeConstants;
import com.xxw.platform.frame.common.enums.DbTypeEnum;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.tree.factory.DefaultTreeBuildFactory;
import com.xxw.platform.frame.common.util.ResourceCodeUtil;
import com.xxw.platform.plugin.auth.api.LoginUserApi;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.basic.SimpleRoleInfo;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.db.api.context.DbOperatorContext;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.ResourceReportApi;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ReportResourceParam;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceUrlParam;
import com.xxw.platform.plugin.scanner.api.pojo.resource.SysResourcePersistencePojo;
import com.xxw.platform.plugin.system.api.ResourceServiceApi;
import com.xxw.platform.plugin.system.api.RoleServiceApi;
import com.xxw.platform.plugin.system.api.pojo.resource.LayuiApiResourceTreeNode;
import com.xxw.platform.plugin.system.api.pojo.resource.ResourceRequest;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleResourceDTO;
import com.xxw.platform.plugin.system.business.resource.entity.SysResource;
import com.xxw.platform.plugin.system.business.resource.factory.ResourceFactory;
import com.xxw.platform.plugin.system.business.resource.mapper.SysResourceMapper;
import com.xxw.platform.plugin.system.business.resource.pojo.ResourceTreeNode;
import com.xxw.platform.plugin.system.business.resource.service.SysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ????????? ???????????????
 *
 * @author liaoxiting
 * @date 2020/11/23 22:45
 */
@Service
@Slf4j
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService, ResourceReportApi, ResourceServiceApi {

    @Resource
    private SysResourceMapper resourceMapper;

    @Resource
    private RoleServiceApi roleServiceApi;

    @Resource(name = "resourceCache")
    private CacheOperatorApi<ResourceDefinition> resourceCache;

    @Override
    public PageResult<SysResource> findPage(ResourceRequest resourceRequest) {
        LambdaQueryWrapper<SysResource> wrapper = createWrapper(resourceRequest);
        Page<SysResource> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public List<SysResource> findList(ResourceRequest resourceRequest) {

        LambdaQueryWrapper<SysResource> wrapper = createWrapper(resourceRequest);

        // ?????????code???name
        wrapper.select(SysResource::getResourceCode, SysResource::getResourceName);

        List<SysResource> menuResourceList = this.list(wrapper);

        // ?????????????????????????????????
        SysResource sysResource = new SysResource();
        sysResource.setResourceCode("");
        sysResource.setResourceName("????????????(???)");
        menuResourceList.add(0, sysResource);

        return menuResourceList;
    }

    @Override
    public List<ResourceTreeNode> getRoleResourceTree(Long roleId, Boolean treeBuildFlag, Integer resourceBizType) {

        // ?????????????????????????????????
        List<SysRoleResourceDTO> resourceList = roleServiceApi.getRoleResourceList(Collections.singletonList(roleId));

        // ????????????????????????
        List<String> alreadyList = new ArrayList<>();
        for (SysRoleResourceDTO sysRoleResponse : resourceList) {
            alreadyList.add(sysRoleResponse.getResourceCode());
        }

        return this.getResourceList(alreadyList, treeBuildFlag, resourceBizType);
    }

    @Override
    public List<ResourceTreeNode> getResourceList(List<String> resourceCodes, Boolean treeBuildFlag, Integer resourceBizType) {
        List<ResourceTreeNode> res = new ArrayList<>();

        // ?????????????????????
        LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysResourceLambdaQueryWrapper.select(SysResource::getAppCode, SysResource::getModularCode, SysResource::getModularName, SysResource::getResourceCode, SysResource::getUrl,
                SysResource::getResourceName);

        // ??????????????????????????????
        sysResourceLambdaQueryWrapper.eq(SysResource::getRequiredPermissionFlag, YesOrNotEnum.Y.getCode());

        // ???????????????????????????
        sysResourceLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(resourceBizType), SysResource::getResourceBizType, resourceBizType);

        LoginUserApi loginUserApi = LoginContext.me();
        if (!loginUserApi.getSuperAdminFlag()) {
            // ??????????????????
            List<Long> roleIds = loginUserApi.getLoginUser().getSimpleRoleInfoList().parallelStream().map(SimpleRoleInfo::getRoleId).collect(Collectors.toList());
            Set<String> resourceCodeList = roleServiceApi.getRoleResourceCodeList(roleIds);
            if (!resourceCodeList.isEmpty()) {
                sysResourceLambdaQueryWrapper.in(SysResource::getResourceCode, resourceCodeList);
            }
        }

        List<SysResource> allResource = this.list(sysResourceLambdaQueryWrapper);

        // ?????????????????????????????????
        Map<String, List<SysResource>> modularMap = new HashMap<>();
        for (SysResource sysResource : allResource) {
            List<SysResource> sysResources = modularMap.get(sysResource.getModularName());

            // ?????????????????????
            if (ObjectUtil.isEmpty(sysResources)) {
                sysResources = new ArrayList<>();
                modularMap.put(sysResource.getModularName(), sysResources);
            }
            // ?????????????????????
            sysResources.add(sysResource);
        }

        // ??????????????????
        for (Map.Entry<String, List<SysResource>> entry : modularMap.entrySet()) {
            ResourceTreeNode item = new ResourceTreeNode();
            item.setResourceFlag(false);
            String id = IdWorker.get32UUID();
            item.setCode(id);
            item.setParentCode(RuleConstants.TREE_ROOT_ID.toString());
            item.setNodeName(entry.getKey());

            // ???????????????????????????????????????
            int checkedNumber = 0;

            //??????????????????
            for (SysResource resource : entry.getValue()) {
                ResourceTreeNode subItem = new ResourceTreeNode();
                // ????????????????????????
                if (!resourceCodes.contains(resource.getResourceCode())) {
                    subItem.setChecked(false);
                } else {
                    checkedNumber++;

                    // ??????????????????
                    item.setChecked(true);
                    subItem.setChecked(true);
                }
                subItem.setResourceFlag(true);
                subItem.setNodeName(resource.getResourceName());
                subItem.setCode(resource.getResourceCode());
                subItem.setParentCode(id);
                res.add(subItem);
            }

            // ?????????????????????
            if (checkedNumber == 0) {
                item.setChecked(false);
                item.setIndeterminate(false);
            } else if (checkedNumber == entry.getValue().size()) {
                item.setChecked(true);
                item.setIndeterminate(false);
            } else {
                item.setChecked(false);
                item.setIndeterminate(true);
            }

            res.add(item);
        }

        // ??????map???????????????
        if (treeBuildFlag) {
            return new DefaultTreeBuildFactory<ResourceTreeNode>().doTreeBuild(res);
        } else {
            return res;
        }
    }

    @Override
    public List<LayuiApiResourceTreeNode> getApiResourceTree(ResourceRequest resourceRequest) {

        // 1. ?????????????????????
        LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysResourceLambdaQueryWrapper.eq(SysResource::getViewFlag, YesOrNotEnum.N.getCode());
        sysResourceLambdaQueryWrapper.select(SysResource::getAppCode, SysResource::getModularCode, SysResource::getModularName, SysResource::getResourceCode, SysResource::getUrl,
                SysResource::getResourceName);

        // ????????????
        if (ObjectUtil.isNotEmpty(resourceRequest.getResourceName())) {
            sysResourceLambdaQueryWrapper.like(SysResource::getUrl, resourceRequest.getResourceName()).or().like(SysResource::getResourceName, resourceRequest.getResourceName());
        }

        List<SysResource> allResource = this.list(sysResourceLambdaQueryWrapper);

        // 2. ??????????????????????????????map
        Map<String, Map<String, List<LayuiApiResourceTreeNode>>> appModularResources = divideResources(allResource);

        // 3. ????????????code?????????name?????????
        Map<String, String> modularCodeName = createModularCodeName(allResource);

        // 4. ??????map???????????????
        return createResourceTree(appModularResources, modularCodeName);
    }

    @Override
    public ResourceDefinition getApiResourceDetail(ResourceRequest resourceRequest) {
        LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysResourceLambdaQueryWrapper.eq(SysResource::getResourceCode, resourceRequest.getResourceCode());
        SysResource sysResource = this.getOne(sysResourceLambdaQueryWrapper);
        if (sysResource != null) {

            // ???????????????ResourceDefinition
            ResourceDefinition resourceDefinition = ResourceFactory.createResourceDefinition(sysResource);

            // ???????????????????????????
            return ResourceFactory.fillResourceDetail(resourceDefinition);
        } else {
            return null;
        }
    }

    @Override
    public void deleteResourceByProjectCode(String projectCode) {
        LambdaQueryWrapper<SysResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysResource::getProjectCode, projectCode);
        this.remove(wrapper);
    }

    @Override
    public void updateResourceAppCode(String newAppCode) {
        // ???????????????????????????
        List<SysResource> list = this.list();

        // ????????????????????????
        for (SysResource sysResource : list) {
            String newResourceCode = ResourceCodeUtil.replace(sysResource.getResourceCode(), newAppCode);
            sysResource.setResourceCode(newResourceCode);
        }

        this.updateBatchById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportResources(@RequestBody ReportResourceParam reportResourceReq) {
        this.reportResourcesAndGetResult(reportResourceReq);
    }

    @Override
    public List<SysResourcePersistencePojo> reportResourcesAndGetResult(ReportResourceParam reportResourceReq) {
        String projectCode = reportResourceReq.getProjectCode();
        Map<String, Map<String, ResourceDefinition>> resourceDefinitions = reportResourceReq.getResourceDefinitions();

        if (ObjectUtil.isEmpty(projectCode) || resourceDefinitions == null) {
            return new ArrayList<>();
        }

        // ??????project?????????????????????????????????
        this.deleteResourceByProjectCode(projectCode);

        // ?????????????????????????????????
        ArrayList<SysResource> allResources = new ArrayList<>();
        ArrayList<ResourceDefinition> resourceDefinitionArrayList = new ArrayList<>();
        for (Map.Entry<String, Map<String, ResourceDefinition>> appModularResources : resourceDefinitions.entrySet()) {
            Map<String, ResourceDefinition> value = appModularResources.getValue();
            for (Map.Entry<String, ResourceDefinition> modularResources : value.entrySet()) {
                resourceDefinitionArrayList.add(modularResources.getValue());
                SysResource resource = ResourceFactory.createResource(modularResources.getValue());
                allResources.add(resource);
            }
        }

        // ?????????????????????
        DbTypeEnum currentDbType = DbOperatorContext.me().getCurrentDbType();
        if (DbTypeEnum.MYSQL.equals(currentDbType)) {
            // ??????????????????
            List<List<SysResource>> split = ListUtil.split(allResources, RuleConstants.DEFAULT_BATCH_INSERT_SIZE);
            for (List<SysResource> sysResources : split) {
                this.getBaseMapper().insertBatchSomeColumn(sysResources);
            }
        } else {
            this.saveBatch(allResources, allResources.size());
        }

        // ???????????????????????????
        Map<String, ResourceDefinition> resourceDefinitionMap = ResourceFactory.orderedResourceDefinition(resourceDefinitionArrayList);
        for (Map.Entry<String, ResourceDefinition> entry : resourceDefinitionMap.entrySet()) {
            resourceCache.put(entry.getKey(), entry.getValue());
        }

        // ??????????????????
        ArrayList<SysResourcePersistencePojo> finalResult = new ArrayList<>();
        for (SysResource item : allResources) {
            SysResourcePersistencePojo sysResourcePersistencePojo = new SysResourcePersistencePojo();
            BeanUtil.copyProperties(item, sysResourcePersistencePojo);
            finalResult.add(sysResourcePersistencePojo);
        }

        return finalResult;
    }

    @Override
    public ResourceDefinition getResourceByUrl(@RequestBody ResourceUrlParam resourceUrlReq) {
        if (ObjectUtil.isEmpty(resourceUrlReq.getUrl())) {
            return null;
        } else {

            // ?????????????????????
            ResourceDefinition tempCachedResourceDefinition = resourceCache.get(resourceUrlReq.getUrl());
            if (tempCachedResourceDefinition != null) {
                return tempCachedResourceDefinition;
            }

            // ?????????????????????????????????
            List<SysResource> resources = resourceMapper.selectList(new QueryWrapper<SysResource>().eq("url", resourceUrlReq.getUrl()));

            if (resources == null || resources.isEmpty()) {
                return null;
            } else {

                // ????????????????????????
                SysResource resource = resources.get(0);
                ResourceDefinition resourceDefinition = new ResourceDefinition();
                BeanUtils.copyProperties(resource, resourceDefinition);

                // ?????????????????????????????????, ????????????????????????????????????????????????true,?????????false
                String requiredLoginFlag = resource.getRequiredLoginFlag();
                resourceDefinition.setRequiredLoginFlag(YesOrNotEnum.Y.name().equals(requiredLoginFlag));

                // ???????????????????????????????????????????????????????????????????????????true,?????????false
                String requiredPermissionFlag = resource.getRequiredPermissionFlag();
                resourceDefinition.setRequiredPermissionFlag(YesOrNotEnum.Y.name().equals(requiredPermissionFlag));

                // ???????????????????????????
                resourceCache.put(resourceDefinition.getUrl(), resourceDefinition);

                return resourceDefinition;
            }
        }
    }

    @Override
    public Set<String> getResourceUrlsListByCodes(Set<String> resourceCodes) {

        if (resourceCodes == null || resourceCodes.isEmpty()) {
            return new HashSet<>();
        }

        // ??????in??????
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysResource::getResourceCode, resourceCodes);
        queryWrapper.select(SysResource::getUrl);

        // ??????????????????
        List<SysResource> list = this.list(queryWrapper);
        return list.stream().map(SysResource::getUrl).collect(Collectors.toSet());
    }

    @Override
    public Integer getResourceCount() {
        long count = this.count();
        return Convert.toInt(count);
    }

    @Override
    public List<SysRoleResourceDTO> getTotalResourceCode(ResBizTypeEnum resBizTypeEnum) {

        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysResource::getResourceCode, SysResource::getResourceBizType);

        // ????????????????????????
        queryWrapper.eq(ObjectUtil.isNotEmpty(resBizTypeEnum), SysResource::getResourceBizType, resBizTypeEnum.getCode());

        List<SysResource> list = this.list(queryWrapper);

        ArrayList<SysRoleResourceDTO> results = new ArrayList<>();
        for (SysResource sysResource : list) {
            SysRoleResourceDTO sysRoleResourceDTO = new SysRoleResourceDTO();
            sysRoleResourceDTO.setResourceCode(sysResource.getResourceCode());
            sysRoleResourceDTO.setResourceBizType(sysResource.getResourceBizType());
            results.add(sysRoleResourceDTO);
        }

        return results;
    }

    @Override
    public Integer getResourceBizTypeByCode(String resCode) {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysResource::getResourceBizType);
        queryWrapper.eq(SysResource::getResourceCode, resCode);
        SysResource one = this.getOne(queryWrapper, false);
        if (one == null) {
            return ResBizTypeEnum.DEFAULT.getCode();
        } else {
            return one.getResourceBizType();
        }
    }

    /**
     * ??????wrapper
     *
     * @author liaoxiting
     * @date 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysResource> createWrapper(ResourceRequest resourceRequest) {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isEmpty(resourceRequest)) {
            return queryWrapper;
        }

        // ????????????????????????
        queryWrapper.eq(ObjectUtil.isNotEmpty(resourceRequest.getAppCode()), SysResource::getAppCode, resourceRequest.getAppCode());

        // ??????????????????
        queryWrapper.like(ObjectUtil.isNotEmpty(resourceRequest.getResourceName()), SysResource::getResourceName, resourceRequest.getResourceName());

        // ????????????url
        queryWrapper.like(ObjectUtil.isNotEmpty(resourceRequest.getUrl()), SysResource::getUrl, resourceRequest.getUrl());

        return queryWrapper;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @return ?????????key???????????????????????????key??????????????????????????????????????????????????????????????????
     * @author liaoxiting
     * @date 2020/12/18 15:34
     */
    private Map<String, Map<String, List<LayuiApiResourceTreeNode>>> divideResources(List<SysResource> sysResources) {
        HashMap<String, Map<String, List<LayuiApiResourceTreeNode>>> appModularResources = new HashMap<>();
        for (SysResource sysResource : sysResources) {

            // ???????????????????????????
            String appCode = sysResource.getAppCode();
            Map<String, List<LayuiApiResourceTreeNode>> modularResource = appModularResources.get(appCode);

            // ????????????????????????????????????map
            if (modularResource == null) {
                modularResource = new HashMap<>();
            }

            // ??????????????????????????????????????????appModularResources??????
            List<LayuiApiResourceTreeNode> resourceTreeNodes = modularResource.get(sysResource.getModularCode());
            if (resourceTreeNodes == null) {
                resourceTreeNodes = new ArrayList<>();
            }

            // ?????????????????????????????????
            LayuiApiResourceTreeNode resourceTreeNode = new LayuiApiResourceTreeNode();
            resourceTreeNode.setResourceFlag(true);
            resourceTreeNode.setTitle(sysResource.getResourceName());
            resourceTreeNode.setId(sysResource.getResourceCode());
            resourceTreeNode.setParentId(sysResource.getModularCode());
            resourceTreeNode.setUrl(sysResource.getUrl());
            resourceTreeNode.setSpread(false);
            resourceTreeNodes.add(resourceTreeNode);

            modularResource.put(sysResource.getModularCode(), resourceTreeNodes);
            appModularResources.put(appCode, modularResource);
        }
        return appModularResources;
    }

    /**
     * ????????????code???name?????????
     *
     * @author liaoxiting
     * @date 2020/12/21 11:23
     */
    private Map<String, String> createModularCodeName(List<SysResource> resources) {
        HashMap<String, String> modularCodeName = new HashMap<>();
        for (SysResource resource : resources) {
            modularCodeName.put(resource.getModularCode(), resource.getModularName());
        }
        return modularCodeName;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @author liaoxiting
     * @date 2020/12/18 15:45
     */
    private List<LayuiApiResourceTreeNode> createResourceTree(Map<String, Map<String, List<LayuiApiResourceTreeNode>>> appModularResources, Map<String, String> modularCodeName) {

        List<LayuiApiResourceTreeNode> finalTree = new ArrayList<>();

        // ???????????????????????????????????????
        for (String appName : appModularResources.keySet()) {

            // ????????????????????????
            LayuiApiResourceTreeNode appNode = new LayuiApiResourceTreeNode();
            appNode.setId(appName);
            appNode.setTitle(appName);
            appNode.setSpread(true);
            appNode.setResourceFlag(false);
            appNode.setParentId(TreeConstants.DEFAULT_PARENT_ID.toString());

            // ????????????????????????????????????
            Map<String, List<LayuiApiResourceTreeNode>> modularResources = appModularResources.get(appName);

            // ??????????????????
            ArrayList<LayuiApiResourceTreeNode> modularNodes = new ArrayList<>();
            for (String modularCode : modularResources.keySet()) {
                LayuiApiResourceTreeNode modularNode = new LayuiApiResourceTreeNode();
                modularNode.setId(modularCode);
                modularNode.setTitle(modularCodeName.get(modularCode));
                modularNode.setParentId(appName);
                modularNode.setSpread(false);
                modularNode.setResourceFlag(false);
                modularNode.setChildren(modularResources.get(modularCode));
                modularNodes.add(modularNode);
            }

            // ????????????????????????????????????
            appNode.setChildren(modularNodes);

            // ?????????????????????
            finalTree.add(appNode);
        }

        return finalTree;
    }

}
