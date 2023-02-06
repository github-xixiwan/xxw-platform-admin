package com.xxw.platform.plugin.system.api;

import com.xxw.platform.plugin.system.api.enums.DetectModeEnum;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationRequest;
import com.xxw.platform.plugin.system.api.pojo.organization.OrganizationTreeNode;

import java.util.List;

/**
 * 组织机构api
 *
 * @author liaoxiting
 * @date 2021/1/15 10:40
 */
public interface OrganizationServiceApi {

    /**
     * 查询系统组织机构
     *
     * @return 组织机构列表
     * @author liaoxiting
     * @date 2021/1/15 10:41
     */
    List<HrOrganizationDTO> orgList();

    /**
     * 获取组织机构详情
     *
     * @author yexing
     * @date 2022/3/8 23:32
     */
    HrOrganizationDTO getOrgDetail(Long orgId);

    /**
     * 批量获取组织机构信息详情
     *
     * @author liaoxiting
     * @date 2022/10/31 20:02
     */
    List<HrOrganizationDTO> getOrgDetailList(List<Long> orgIdList);

    /**
     * 获取组织机构下拉选择树
     *
     * @author liaoxiting
     * @date 2022/6/8 14:40
     */
    List<OrganizationTreeNode> getOrgTreeList(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 获取某个公司下，所有部门树的集合
     *
     * @author liaoxiting
     * @date 2022/6/16 18:26
     */
    List<OrganizationTreeNode> getDeptOrgTree(Long orgId);

    /**
     * 获取指定组织机构的上级组织机构是什么
     * <p>
     * 自下而上：逐级向上获取直到获取到最高级
     * 自上而下：逐级向下获取，直到获取到本层机构
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表直接返回本部门
     * @param detectModeEnum 自上而下还是自下而上
     * @return 上级机构的id
     * @author liaoxiting
     * @date 2022/9/18 15:02
     */
    Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum);

}
