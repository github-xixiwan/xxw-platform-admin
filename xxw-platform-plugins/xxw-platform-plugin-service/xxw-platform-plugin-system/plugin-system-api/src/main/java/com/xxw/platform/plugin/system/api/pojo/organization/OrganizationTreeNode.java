package com.xxw.platform.plugin.system.api.pojo.organization;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.tree.factory.base.AbstractTreeNode;
import com.xxw.platform.frame.common.tree.xmtree.base.AbstractXmSelectNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 组织机构树节点
 *
 * @author liaoxiting
 * @date 2020/12/27 18:36
 */
@Data
public class OrganizationTreeNode implements AbstractTreeNode<OrganizationTreeNode>, AbstractXmSelectNode {

    /**
     * 父id，一级节点父id是0
     */
    @ChineseDescription("父id，一级节点父id是0")
    private Long parentId;

    /**
     * 节点名称
     */
    @ChineseDescription("节点名称")
    private String title;

    /**
     * 节点值
     */
    @ChineseDescription("节点值")
    private Long id;

    /**
     * 是否展开状态 不展开-false 展开-true
     */
    @ChineseDescription("是否展开状态 不展开-false 展开-true")
    private boolean spread = true;

    /**
     * 是否选中
     */
    @ChineseDescription("是否选中")
    private boolean selected = false;

    /**
     * 组织名称
     */
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织编码
     */
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 税号
     */
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 组织机构描述
     */
    @ChineseDescription("组织机构描述")
    private String orgRemark;

    /**
     * 子节点的集合
     */
    @ChineseDescription("子节点的集合")
    private List<OrganizationTreeNode> children;

    @Override
    public String getNodeId() {
        return this.id.toString();
    }

    @Override
    public String getNodeParentId() {
        return this.parentId.toString();
    }

    @Override
    public void setChildrenNodes(List<OrganizationTreeNode> childrenNodes) {
        this.children = childrenNodes;
    }

    @Override
    public String getName() {
        return this.title;
    }

    @Override
    public String getValue() {
        return String.valueOf(id);
    }

    @Override
    public Boolean getSelected() {
        return this.selected;
    }

    @Override
    public Boolean getDisabled() {
        return false;
    }

    @Override
    public List getChildren() {
        return this.children;
    }

}
