package com.xxw.platform.plugin.system.api.pojo.menu.layui;

import com.xxw.platform.frame.common.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * Layui首页菜单的节点详情
 *
 * @author liaoxiting
 * @date 2020/12/27 18:36
 */
@Data
public class LayuiIndexMenuTreeNode implements AbstractTreeNode<LayuiIndexMenuTreeNode> {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 节点id
     */
    private Long menuId;

    /**
     * 父节点
     */
    private Long menuParentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 节点的url
     */
    private String layuiPath;

    /**
     * 节点图标
     */
    private String layuiIcon;

    /**
     * 子节点的集合
     */
    private List<LayuiIndexMenuTreeNode> children;

    @Override
    public String getNodeId() {
        return this.menuId.toString();
    }

    @Override
    public String getNodeParentId() {
        return this.menuParentId.toString();
    }

    @Override
    public void setChildrenNodes(List<LayuiIndexMenuTreeNode> childrenNodes) {
        this.children = childrenNodes;
    }

}
