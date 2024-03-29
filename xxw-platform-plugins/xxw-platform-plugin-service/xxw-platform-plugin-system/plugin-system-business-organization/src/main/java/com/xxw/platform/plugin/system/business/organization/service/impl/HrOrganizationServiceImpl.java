package com.xxw.platform.plugin.system.business.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.constants.SymbolConstant;
import com.xxw.platform.frame.common.constants.TreeConstants;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.tree.factory.DefaultTreeBuildFactory;
import com.xxw.platform.frame.common.tree.ztree.ZTreeNode;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.enums.DataScopeTypeEnum;
import com.xxw.platform.plugin.db.api.DbOperatorApi;
import com.xxw.platform.plugin.db.api.context.DbOperatorContext;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.api.ExpandApi;
import com.xxw.platform.plugin.system.api.RoleDataScopeServiceApi;
import com.xxw.platform.plugin.system.api.RoleServiceApi;
import com.xxw.platform.plugin.system.api.UserOrgServiceApi;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import com.xxw.platform.plugin.system.api.enums.DetectModeEnum;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.organization.OrganizationExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationRequest;
import com.xxw.platform.plugin.system.api.pojo.organization.OrganizationTreeNode;
import com.xxw.platform.plugin.system.api.util.DataScopeUtil;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrganization;
import com.xxw.platform.plugin.system.business.organization.factory.OrganizationFactory;
import com.xxw.platform.plugin.system.business.organization.mapper.HrOrganizationMapper;
import com.xxw.platform.plugin.system.business.organization.service.HrOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 组织架构管理
 *
 * @author liaoxiting
 * @date 2020/11/04 11:05
 */
@Service
public class HrOrganizationServiceImpl extends ServiceImpl<HrOrganizationMapper, HrOrganization> implements HrOrganizationService {

    @Resource
    private UserOrgServiceApi userOrgServiceApi;

    @Resource
    private RoleServiceApi roleServiceApi;

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private RoleDataScopeServiceApi roleDataScopeServiceApi;

    @Resource
    private ExpandApi expandApi;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(HrOrganizationRequest hrOrganizationRequest) {

        // 获取父id
        Long pid = hrOrganizationRequest.getOrgParentId();

        // 校验数据范围
        DataScopeUtil.quickValidateDataScope(pid);

        HrOrganization hrOrganization = new HrOrganization();
        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充parentIds
        this.fillParentIds(hrOrganization);

        // 设置状态为启用，未删除状态
        hrOrganization.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(hrOrganization);

        // 处理动态表单数据
        if (hrOrganizationRequest.getExpandDataInfo() != null) {
            hrOrganizationRequest.getExpandDataInfo().setPrimaryFieldValue(hrOrganization.getOrgId());
            expandApi.saveOrUpdateExpandData(hrOrganizationRequest.getExpandDataInfo());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(HrOrganizationRequest hrOrganizationRequest) {

        HrOrganization hrOrganization = this.queryOrganization(hrOrganizationRequest);
        Long organizationId = hrOrganization.getOrgId();

        // 校验数据范围
        DataScopeUtil.quickValidateDataScope(organizationId);

        // 该机构下有员工，则不能删
        Boolean userOrgFlag = userOrgServiceApi.getUserOrgFlag(organizationId, null);
        if (userOrgFlag) {
            throw new SystemModularException(OrganizationExceptionEnum.DELETE_ORGANIZATION_ERROR);
        }

        // 级联删除子节点，逻辑删除
        Set<Long> childIdList = DbOperatorContext.me().findSubListByParentId("hr_organization", "org_pids", "org_id", organizationId);
        childIdList.add(organizationId);

        LambdaUpdateWrapper<HrOrganization> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(HrOrganization::getOrgId, childIdList).set(HrOrganization::getDelFlag, YesOrNotEnum.Y.getCode());
        this.update(updateWrapper);

        // 删除角色对应的组织架构数据范围
        roleDataScopeServiceApi.delByOrgIds(childIdList);

        // 删除用户对应的组织架构数据范围
        userServiceApi.deleteUserDataScopeListByOrgIdList(childIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(HrOrganizationRequest hrOrganizationRequest) {

        HrOrganization hrOrganization = this.queryOrganization(hrOrganizationRequest);
        Long id = hrOrganization.getOrgId();

        // 校验数据范围
        DataScopeUtil.quickValidateDataScope(id);

        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充parentIds
        this.fillParentIds(hrOrganization);

        // 不能修改状态，用修改状态接口修改状态
        hrOrganization.setStatusFlag(null);

        // 更新这条记录
        this.updateById(hrOrganization);

        // 处理动态表单数据
        if (hrOrganizationRequest.getExpandDataInfo() != null) {
            hrOrganizationRequest.getExpandDataInfo().setPrimaryFieldValue(hrOrganization.getOrgId());
            expandApi.saveOrUpdateExpandData(hrOrganizationRequest.getExpandDataInfo());
        }
    }

    @Override
    public void updateStatus(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.queryOrganization(hrOrganizationRequest);
        hrOrganization.setStatusFlag(hrOrganizationRequest.getStatusFlag());
        this.updateById(hrOrganization);
    }

    @Override
    public HrOrganization detail(HrOrganizationRequest hrOrganizationRequest) {
        return this.getOne(this.createWrapper(hrOrganizationRequest), false);
    }

    @Override
    public PageResult<HrOrganization> findPage(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = this.createWrapper(hrOrganizationRequest);
        Page<HrOrganization> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public List<HrOrganization> findList(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = this.createWrapper(hrOrganizationRequest);
        return this.list(wrapper);
    }

    @Override
    public List<OrganizationTreeNode> organizationTree(HrOrganizationRequest hrOrganizationRequest) {

        // 定义返回结果
        List<OrganizationTreeNode> treeNodeList = CollectionUtil.newArrayList();

        // 组装节点
        List<HrOrganization> hrOrganizationList = this.findListByDataScope(hrOrganizationRequest);
        for (HrOrganization hrOrganization : hrOrganizationList) {
            OrganizationTreeNode treeNode = OrganizationFactory.parseOrganizationTreeNode(hrOrganization);
            treeNodeList.add(treeNode);
        }

        // 设置树节点上，用户绑定的组织机构数据范围
        if (hrOrganizationRequest.getUserId() != null) {
            List<Long> orgIds = userServiceApi.getUserBindDataScope(hrOrganizationRequest.getUserId());
            if (ObjectUtil.isNotEmpty(orgIds)) {
                for (OrganizationTreeNode organizationTreeNode : treeNodeList) {
                    for (Long orgId : orgIds) {
                        if (organizationTreeNode.getId().equals(orgId)) {
                            organizationTreeNode.setSelected(true);
                        }
                    }
                }
            }
        }

        if (ObjectUtil.isNotEmpty(hrOrganizationRequest.getOrgName()) || ObjectUtil.isNotEmpty(hrOrganizationRequest.getOrgCode())) {
            return treeNodeList;
        } else {
            return new DefaultTreeBuildFactory<OrganizationTreeNode>().doTreeBuild(treeNodeList);
        }
    }

    @Override
    public List<ZTreeNode> orgZTree(HrOrganizationRequest hrOrganizationRequest, boolean buildTree) {

        // 获取角色id
        Long roleId = hrOrganizationRequest.getRoleId();

        // 获取所有组织机构列表
        LambdaQueryWrapper<HrOrganization> wrapper = createWrapper(hrOrganizationRequest);
        List<HrOrganization> list = this.list(wrapper);
        List<ZTreeNode> zTreeNodes = OrganizationFactory.parseZTree(list);

        // 获取角色目前绑定的组织机构范围
        List<Long> roleDataScopes = roleServiceApi.getRoleDataScopes(ListUtil.toList(roleId));

        // 设置绑定的组织机构范围为已选则状态
        for (ZTreeNode zTreeNode : zTreeNodes) {
            if (roleDataScopes.contains(zTreeNode.getId())) {
                zTreeNode.setChecked(true);
            }
        }

        if (buildTree) {
            return new DefaultTreeBuildFactory<ZTreeNode>().doTreeBuild(zTreeNodes);
        } else {
            return zTreeNodes;
        }
    }

    @Override
    public Set<Long> findAllLevelParentIdsByOrganizations(Set<Long> organizationIds) {

        // 定义返回结果
        Set<Long> allLevelParentIds = new HashSet<>(organizationIds);

        // 查询出这些节点的pids字段
        LambdaQueryWrapper<HrOrganization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(HrOrganization::getOrgId, organizationIds);
        queryWrapper.select(HrOrganization::getOrgPids);

        List<HrOrganization> organizationList = this.list(queryWrapper);
        if (organizationList == null || organizationList.isEmpty()) {
            return allLevelParentIds;
        }

        // 把所有的pids分割，并放入到set中
        for (HrOrganization hrOrganization : organizationList) {

            // 获取pids值
            String pids = hrOrganization.getOrgPids();

            // 去掉所有的左中括号
            pids = StrUtil.removeAll(pids, SymbolConstant.LEFT_SQUARE_BRACKETS);

            // 去掉所有的右中括号
            pids = StrUtil.removeAll(pids, SymbolConstant.RIGHT_SQUARE_BRACKETS);

            // 按逗号分割这个字符串，得到pid的数组
            String[] finalPidArray = pids.split(StrUtil.COMMA);

            // 遍历这些值，放入到最终的set
            for (String pid : finalPidArray) {
                allLevelParentIds.add(Convert.toLong(pid));
            }
        }
        return allLevelParentIds;
    }

    @Override
    public List<HrOrganizationDTO> orgList() {

        LambdaQueryWrapper<HrOrganization> queryWrapper = new LambdaQueryWrapper<>();

        // 如果是超级管理员 或 数据范围是所有，则不过滤数据范围
        boolean needToDataScope = true;
        if (LoginContext.me().getSuperAdminFlag()) {
            Set<DataScopeTypeEnum> dataScopeTypes = LoginContext.me().getLoginUser().getDataScopeTypeEnums();
            if (dataScopeTypes != null && dataScopeTypes.contains(DataScopeTypeEnum.ALL)) {
                needToDataScope = false;
            }
        }

        // 如果需要数据范围过滤，则获取用户的数据范围，拼接查询条件
        if (needToDataScope) {
            Set<Long> dataScope = LoginContext.me().getLoginUser().getDataScopeOrganizationIds();

            // 数据范围没有，直接返回空
            if (ObjectUtil.isEmpty(dataScope)) {
                return new ArrayList<>();
            }

            // 根据组织机构数据范围的上级组织，用于展示完整的树形结构
            Set<Long> allLevelParentIdsByOrganizations = this.findAllLevelParentIdsByOrganizations(dataScope);

            // 拼接查询条件
            queryWrapper.in(HrOrganization::getOrgId, allLevelParentIdsByOrganizations);
        }

        // 只查询未删除的
        queryWrapper.eq(HrOrganization::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(HrOrganization::getOrgSort);

        // 先实例化出Function接口
        Function<Object, HrOrganizationDTO> mapper = e -> {
            HrOrganizationDTO org = new HrOrganizationDTO();
            HrOrganization hrOrg = (HrOrganization) e;
            BeanUtil.copyProperties(hrOrg, org);
            return org;
        };

        // 返回数据
        List<HrOrganization> list = this.list(queryWrapper);
        return list.stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    @Override
    public HrOrganizationDTO getOrgDetail(Long orgId) {
        HrOrganizationDTO hrOrganizationDTO = new HrOrganizationDTO();
        HrOrganizationRequest request = new HrOrganizationRequest();
        request.setOrgId(orgId);
        HrOrganization detail = this.detail(request);
        if (ObjectUtil.isNotNull(detail)) {
            BeanUtil.copyProperties(detail, hrOrganizationDTO, CopyOptions.create().ignoreError());
        }
        return hrOrganizationDTO;
    }

    @Override
    public List<HrOrganizationDTO> getOrgDetailList(List<Long> orgIdList) {
        ArrayList<HrOrganizationDTO> organizationDTOS = new ArrayList<>();
        for (Long orgId : orgIdList) {
            HrOrganizationDTO orgDetail = this.getOrgDetail(orgId);
            organizationDTOS.add(orgDetail);
        }
        return organizationDTOS;
    }

    @Override
    public List<OrganizationTreeNode> getOrgTreeList(HrOrganizationRequest hrOrganizationRequest) {

        List<OrganizationTreeNode> treeNodeList = CollectionUtil.newArrayList();

        // 获取所有组织机构
        LambdaQueryWrapper<HrOrganization> wrapper = createWrapper(hrOrganizationRequest);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);

        // 组装节点
        for (HrOrganization hrOrganization : hrOrganizationList) {
            OrganizationTreeNode treeNode = OrganizationFactory.parseOrganizationTreeNode(hrOrganization);
            treeNodeList.add(treeNode);
        }

        // 节点组装成树
        return new DefaultTreeBuildFactory<OrganizationTreeNode>().doTreeBuild(treeNodeList);
    }

    @Override
    public List<OrganizationTreeNode> getDeptOrgTree(Long orgId) {

        // 获取该公司下所有的子部门id集合
        Set<Long> deptIds = dbOperatorApi.findSubListByParentId("hr_organization", "org_pids", "org_id", orgId);
        deptIds.add(orgId);

        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrOrganization::getOrgId, deptIds);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);

        // 组装部门的树形节点
        List<OrganizationTreeNode> treeNodeList = CollectionUtil.newArrayList();
        for (HrOrganization hrOrganization : hrOrganizationList) {
            OrganizationTreeNode treeNode = OrganizationFactory.parseOrganizationTreeNode(hrOrganization);
            treeNodeList.add(treeNode);
        }
        return new DefaultTreeBuildFactory<OrganizationTreeNode>().doTreeBuild(treeNodeList);
    }

    @Override
    public Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum) {
        if (DetectModeEnum.TO_TOP.equals(detectModeEnum)) {
            return calcParentOrgId(orgId, parentLevelNum, true);
        } else {
            return calcParentOrgId(orgId, parentLevelNum, false);
        }
    }

    /**
     * 创建组织架构的通用条件查询wrapper
     *
     * @author liaoxiting
     * @date 2020/11/6 10:16
     */
    private LambdaQueryWrapper<HrOrganization> createWrapper(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> queryWrapper = new LambdaQueryWrapper<>();

        // 查询未删除状态的
        queryWrapper.eq(HrOrganization::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(HrOrganization::getOrgSort);

        if (ObjectUtil.isEmpty(hrOrganizationRequest)) {
            return queryWrapper;
        }

        String orgName = hrOrganizationRequest.getOrgName();
        String orgCode = hrOrganizationRequest.getOrgCode();
        Long orgParentId = hrOrganizationRequest.getOrgParentId();
        Long orgId = hrOrganizationRequest.getOrgId();
        Integer orgType = hrOrganizationRequest.getOrgType();
        String taxNo = hrOrganizationRequest.getTaxNo();

        // 拼接组织机构名称条件
        queryWrapper.like(ObjectUtil.isNotEmpty(orgName), HrOrganization::getOrgName, orgName);

        // 拼接组织机构编码条件
        queryWrapper.like(ObjectUtil.isNotEmpty(orgCode), HrOrganization::getOrgCode, orgCode);

        // 组织机构类型拼接
        queryWrapper.eq(ObjectUtil.isNotEmpty(orgType), HrOrganization::getOrgType, orgType);

        // 税号查询条件
        queryWrapper.eq(StrUtil.isNotEmpty(taxNo), HrOrganization::getTaxNo, taxNo);

        // 拼接父机构id查询条件
        if (ObjectUtil.isNotEmpty(orgParentId)) {
            queryWrapper.and(qw -> {
                qw.eq(HrOrganization::getOrgId, orgParentId).or().like(HrOrganization::getOrgPids, orgParentId);
            });
        }

        // 拼接机构id查询条件
        queryWrapper.eq(ObjectUtil.isNotEmpty(orgId), HrOrganization::getOrgId, orgId);

        // 拼接限制查询范围列表
        List<Long> orgIdLimit = hrOrganizationRequest.getOrgIdLimit();
        if (ObjectUtil.isNotEmpty(orgIdLimit)) {
            queryWrapper.nested(qw -> {
                for (Long itemOrgId : orgIdLimit) {
                    qw.or().like(HrOrganization::getOrgPids, itemOrgId);
                }
            });
        }

        return queryWrapper;
    }
    /**
     * 获取系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    private HrOrganization queryOrganization(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.getById(hrOrganizationRequest.getOrgId());
        if (ObjectUtil.isEmpty(hrOrganization)) {
            throw new SystemModularException(OrganizationExceptionEnum.CANT_FIND_ORG, hrOrganizationRequest.getOrgId());
        }
        return hrOrganization;
    }

    /**
     * 填充该节点的pIds
     * <p>
     * 如果pid是顶级节点，pids就是 [-1],
     * <p>
     * 如果pid不是顶级节点，pids就是父节点的pids + [pid] + ,
     *
     * @author liaoxiting
     * @date 2020/11/5 13:45
     */
    private void fillParentIds(HrOrganization hrOrganization) {
        if (TreeConstants.DEFAULT_PARENT_ID.equals(hrOrganization.getOrgParentId())) {
            hrOrganization.setOrgPids(SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA);
        } else {
            // 获取父组织机构
            HrOrganizationRequest hrOrganizationRequest = new HrOrganizationRequest();
            hrOrganizationRequest.setOrgId(hrOrganization.getOrgParentId());
            HrOrganization parentOrganization = this.queryOrganization(hrOrganizationRequest);

            // 设置本节点的父ids为 (上一个节点的pids + (上级节点的id) )
            hrOrganization.setOrgPids(parentOrganization.getOrgPids() + SymbolConstant.LEFT_SQUARE_BRACKETS + parentOrganization.getOrgId() + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA);
        }
    }

    /**
     * 根据数据范围获取组织机构列表
     *
     * @author liaoxiting
     * @date 2021/2/8 20:22
     */
    private List<HrOrganization> findListByDataScope(HrOrganizationRequest hrOrganizationRequest) {

        LambdaQueryWrapper<HrOrganization> queryWrapper = this.createWrapper(hrOrganizationRequest);

        // 数据范围过滤
        // 如果是超级管理员，或者数据范围权限是所有，则不过滤数据范围
        boolean needToDataScope = true;
        Set<DataScopeTypeEnum> dataScopeTypes = LoginContext.me().getLoginUser().getDataScopeTypeEnums();
        if (LoginContext.me().getSuperAdminFlag() || (dataScopeTypes != null && dataScopeTypes.contains(DataScopeTypeEnum.ALL))) {
            needToDataScope = false;
        }

        // 过滤数据范围的SQL拼接
        if (needToDataScope) {
            // 获取用户数据范围信息
            Set<Long> dataScope = LoginContext.me().getLoginUser().getDataScopeOrganizationIds();

            // 如果数据范围为空，则返回空数组
            if (ObjectUtil.isEmpty(dataScope)) {
                return new ArrayList<>();
            }

            // 根据组织机构数据范围的上级组织，用于展示完整的树形结构
            Set<Long> allLevelParentIdsByOrganizations = this.findAllLevelParentIdsByOrganizations(dataScope);
            // 拼接查询条件
            queryWrapper.in(HrOrganization::getOrgId, allLevelParentIdsByOrganizations);
        }

        return this.list(queryWrapper);
    }

    /**
     * 计算获取上级组织机构id
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表不计算直接返回本身
     * @param reverse        是否反转，true-代表自下而上计算，false-代表自上而下计算
     * @author liaoxiting
     * @date 2022/10/1 11:45
     */
    private Long calcParentOrgId(Long orgId, Integer parentLevelNum, boolean reverse) {

        if (ObjectUtil.isEmpty(orgId) || ObjectUtil.isEmpty(parentLevelNum)) {
            return null;
        }

        // 如果上级层数为0，则直接返回参数的orgId，代表同级别组织机构
        if (parentLevelNum == 0) {
            return orgId;
        }

        // 获取当前部门的所有父级id
        HrOrganization hrOrganization = this.getById(orgId);
        if (hrOrganization == null || StrUtil.isEmpty(hrOrganization.getOrgPids())) {
            return null;
        }
        String orgParentIdListStr = hrOrganization.getOrgPids();

        // 去掉中括号符号
        orgParentIdListStr = orgParentIdListStr.replaceAll("\\[", "");
        orgParentIdListStr = orgParentIdListStr.replaceAll("]", "");

        // 获取所有上级id列表
        String[] orgParentIdList = orgParentIdListStr.split(",");
        if (reverse) {
            orgParentIdList = ArrayUtil.reverse(orgParentIdList);
        }

        // 先删掉id为-1的机构，因为-1是不存在的
        ArrayList<String> parentOrgIdList = new ArrayList<>();
        for (String orgIdItem : orgParentIdList) {
            if (!TreeConstants.DEFAULT_PARENT_ID.toString().equals(orgIdItem)) {
                parentOrgIdList.add(orgIdItem);
            }
        }

        // 根据请求参数，需要从parentOrgIdList获取的下标
        int needGetArrayIndex = parentLevelNum - 1;

        // parentOrgIdList最大能提供的下标
        int maxCanGetIndex = parentOrgIdList.size() - 1;

        // 如果没有最顶级的上级，则他本身就是最顶级上级
        if (maxCanGetIndex < 0) {
            return orgId;
        }

        // 根据参数传参，进行获取上级的操作
        String orgIdString;
        if (needGetArrayIndex <= (maxCanGetIndex)) {
            orgIdString = parentOrgIdList.get(needGetArrayIndex);
        } else {
            // 如果需要获取的下标，大于了最大下标
            if (reverse) {
                orgIdString = parentOrgIdList.get(maxCanGetIndex);
            } else {
                return orgId;
            }
        }
        return Long.valueOf(orgIdString);
    }

}
