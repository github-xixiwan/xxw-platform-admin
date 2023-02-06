package com.xxw.platform.plugin.system.business.organization.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.frame.common.tree.ztree.ZTreeNode;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.enums.OrgTypeEnum;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationRequest;
import com.xxw.platform.plugin.system.api.pojo.organization.OrganizationTreeNode;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrganization;
import com.xxw.platform.plugin.system.business.organization.service.HrOrganizationService;
import com.xxw.platform.plugin.system.business.organization.wrapper.OrgExpandWrapper;
import com.xxw.platform.plugin.wrapper.api.annotation.Wrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统组织机构控制器
 *
 * @author liaoxiting
 * @date 2020/11/18 21:55
 */
@RestController
@ApiResource(name = "系统组织机构管理", resBizType = ResBizTypeEnum.SYSTEM)
public class HrOrganizationController {

    @Resource
    private HrOrganizationService hrOrganizationService;

    /**
     * 添加系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @PostResource(name = "添加系统组织机构", path = "/hrOrganization/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(HrOrganizationRequest.add.class) HrOrganizationRequest hrOrganizationRequest) {
        hrOrganizationService.add(hrOrganizationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @PostResource(name = "删除系统组织机构", path = "/hrOrganization/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(HrOrganizationRequest.delete.class) HrOrganizationRequest hrOrganizationRequest) {
        hrOrganizationService.del(hrOrganizationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @PostResource(name = "编辑系统组织机构", path = "/hrOrganization/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(HrOrganizationRequest.edit.class) HrOrganizationRequest hrOrganizationRequest) {
        hrOrganizationService.edit(hrOrganizationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改组织机构状态
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @PostResource(name = "修改组织机构状态", path = "/hrOrganization/updateStatus")
    @BusinessLog
    public ResponseData<?> updateStatus(@RequestBody @Validated(HrOrganizationRequest.updateStatus.class) HrOrganizationRequest hrOrganizationRequest) {
        hrOrganizationService.updateStatus(hrOrganizationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @GetResource(name = "查看详情系统组织机构", path = "/hrOrganization/detail")
    public ResponseData<HrOrganization> detail(@Validated(HrOrganizationRequest.detail.class) HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.detail(hrOrganizationRequest));
    }

    /**
     * 分页查询系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @Wrapper(OrgExpandWrapper.class)
    @GetResource(name = "分页查询系统组织机构", path = "/hrOrganization/page")
    public ResponseData<PageResult<HrOrganization>> page(HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.findPage(hrOrganizationRequest));
    }

    /**
     * 获取全部系统组织机构
     *
     * @author liaoxiting
     * @date 2020/11/04 11:05
     */
    @GetResource(name = "获取全部系统组织机构", path = "/hrOrganization/list")
    public ResponseData<List<HrOrganization>> list(HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.findList(hrOrganizationRequest));
    }

    /**
     * 获取全部系统组织机构树（用于新增，编辑组织机构时选择上级节点，用于获取用户管理界面左侧组织机构树）
     *
     * @author liaoxiting
     * @date 2021/01/05 15:55
     */
    @GetResource(name = "获取全部系统组织机构树", path = "/hrOrganization/tree")
    public ResponseData<List<OrganizationTreeNode>> organizationTree(HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.organizationTree(hrOrganizationRequest));
    }

    /**
     * 获取公司管理组织机构树
     *
     * @author liaoxiting
     * @date 2022/5/21 11:24
     */
    @GetResource(name = "获取公司管理组织机构树", path = "/hrOrganization/companyTree")
    public ResponseData<List<OrganizationTreeNode>> companyTree(HrOrganizationRequest hrOrganizationRequest) {
        hrOrganizationRequest.setOrgType(OrgTypeEnum.COMPANY.getCode());
        return new SuccessResponseData<>(hrOrganizationService.organizationTree(hrOrganizationRequest));
    }

    /**
     * 获取组织机构树（用于用户绑定数据范围，可以渲染是否选中信息）
     *
     * @author liaoxiting
     * @date 2021/3/19 22:20
     */
    @GetResource(name = "获取组织机构树(用于用户绑定数据范围)", path = "/hrOrganization/userBindOrgScope")
    public ResponseData<List<OrganizationTreeNode>> userBindOrgScope(@Validated(HrOrganizationRequest.userBindOrgScope.class) HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.organizationTree(hrOrganizationRequest));
    }

    /**
     * Layui版本--获取组织机构树（用于角色配置数据范围类型，并且数据范围类型是指定组织机构时）
     *
     * @author liaoxiting
     * @date 2021/1/9 18:37
     */
    @GetResource(name = "Layui版本--获取组织机构树（用于角色配置数据范围类型，并且数据范围类型是指定组织机构时）", path = "/hrOrganization/roleBindOrgScope")
    public List<ZTreeNode> roleBindOrgScope(@Validated(HrOrganizationRequest.roleBindOrgScope.class) HrOrganizationRequest hrOrganizationRequest) {
        return hrOrganizationService.orgZTree(hrOrganizationRequest, false);
    }

    /**
     * AntdVue版本--获取组织机构树（用于角色配置数据范围类型，并且数据范围类型是指定组织机构时）
     *
     * @author liaoxiting
     * @date 2021/1/9 18:37
     */
    @GetResource(name = "AntdVue版本--获取组织机构树（用于角色配置数据范围类型，并且数据范围类型是指定组织机构时）", path = "/hrOrganization/roleBindOrgScopeAntdv")
    public ResponseData<List<ZTreeNode>> roleBindOrgScopeAntdv(@Validated(HrOrganizationRequest.roleBindOrgScope.class) HrOrganizationRequest hrOrganizationRequest) {
        List<ZTreeNode> zTreeNodes = hrOrganizationService.orgZTree(hrOrganizationRequest, true);
        return new SuccessResponseData<>(zTreeNodes);
    }

    /**
     * 获取所有组织机构树（树形）
     * <p>
     * 一般用在组织机构选择界面
     *
     * @author liaoxiting
     * @date 2021/1/9 18:37
     */
    @GetResource(name = "获取所有组织机构树（树形）", path = "/hrOrganization/getOrgTreeList")
    public ResponseData<List<OrganizationTreeNode>> getOrgTreeList(HrOrganizationRequest hrOrganizationRequest) {
        List<OrganizationTreeNode> list = hrOrganizationService.getOrgTreeList(hrOrganizationRequest);
        return new SuccessResponseData<>(list);
    }

    /**
     * 获取所有组织机构树列表，post方式
     *
     * @author liaoxiting
     * @date 2022/9/27 9:50
     */
    @PostResource(name = "获取所有组织机构树列表，post方式", path = "/hrOrganization/post/getOrgTreeList")
    public ResponseData<List<OrganizationTreeNode>> postGetOrgTreeList(@RequestBody HrOrganizationRequest hrOrganizationRequest) {
        List<OrganizationTreeNode> list = hrOrganizationService.getOrgTreeList(hrOrganizationRequest);
        return new SuccessResponseData<>(list);
    }

    /**
     * 获取某个公司下部门的机构树
     *
     * @author liaoxiting
     * @date 2021/1/9 18:37
     */
    @GetResource(name = "获取某个公司下部门的机构树", path = "/hrOrganization/getDeptOrgTree")
    public ResponseData<List<OrganizationTreeNode>> getDeptOrgTree(@Validated(HrOrganizationRequest.detail.class) HrOrganizationRequest hrOrganizationRequest) {
        List<OrganizationTreeNode> list = hrOrganizationService.getDeptOrgTree(hrOrganizationRequest.getOrgId());
        return new SuccessResponseData<>(list);
    }

    /**
     * 批量获取组织机构信息列表
     *
     * @author liaoxiting
     * @date 2022/11/2 13:56
     */
    @PostResource(name = "批量获取组织机构信息列表", path = "/hrOrganization/getOrgInfoListByIds")
    public ResponseData<List<HrOrganizationDTO>> getOrgInfoListByIds(@RequestBody @Validated(HrOrganizationRequest.batchQuery.class) HrOrganizationRequest hrOrganizationRequest) {
        List<HrOrganizationDTO> orgDetailList = hrOrganizationService.getOrgDetailList(hrOrganizationRequest.getOrgIdList());
        return new SuccessResponseData<>(orgDetailList);
    }

}
