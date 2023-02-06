package com.xxw.platform.plugin.system.business.organization.factory;

import com.xxw.platform.frame.common.tree.ztree.ZTreeNode;
import com.xxw.platform.plugin.system.api.pojo.organization.OrganizationTreeNode;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrganization;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织机构实体转化
 *
 * @author liaoxiting
 * @date 2021/1/6 21:03
 */
public class OrganizationFactory {

    /**
     * 实体转换
     *
     * @param hrOrganization 机构信息
     * @return LayuiOrganizationTreeNode layui树实体对象
     * @author liaoxiting
     * @date 2021/1/5 21:07
     */
    public static OrganizationTreeNode parseOrganizationTreeNode(HrOrganization hrOrganization) {
        OrganizationTreeNode treeNode = new OrganizationTreeNode();
        treeNode.setId(hrOrganization.getOrgId());
        treeNode.setParentId(hrOrganization.getOrgParentId());
        treeNode.setTitle(hrOrganization.getOrgName());

        treeNode.setOrgName(hrOrganization.getOrgName());
        treeNode.setOrgCode(hrOrganization.getOrgCode());
        treeNode.setStatusFlag(hrOrganization.getStatusFlag());
        treeNode.setOrgSort(hrOrganization.getOrgSort());
        treeNode.setOrgRemark(hrOrganization.getOrgRemark());
        treeNode.setOrgType(hrOrganization.getOrgType());

        return treeNode;
    }

    /**
     * 实体转zTree形式
     *
     * @author liaoxiting
     * @date 2021/1/9 18:43
     */
    public static List<ZTreeNode> parseZTree(List<HrOrganization> organizationList) {
        ArrayList<ZTreeNode> zTreeNodes = new ArrayList<>();
        for (HrOrganization hrOrganization : organizationList) {
            ZTreeNode zTreeNode = new ZTreeNode();
            zTreeNode.setId(hrOrganization.getOrgId());
            zTreeNode.setpId(hrOrganization.getOrgParentId());
            zTreeNode.setName(hrOrganization.getOrgName());
            zTreeNode.setOpen(true);
            zTreeNodes.add(zTreeNode);
        }
        return zTreeNodes;
    }

}
