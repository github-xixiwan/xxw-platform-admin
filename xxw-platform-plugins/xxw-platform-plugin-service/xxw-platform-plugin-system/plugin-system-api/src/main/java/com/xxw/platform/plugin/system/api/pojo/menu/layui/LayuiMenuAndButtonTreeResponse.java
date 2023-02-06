package com.xxw.platform.plugin.system.api.pojo.menu.layui;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 角色分配资源和菜单的树
 *
 * @author liaoxiting
 * @date 2021/1/9 16:59
 */
@Data
public class LayuiMenuAndButtonTreeResponse implements AbstractTreeNode<LayuiMenuAndButtonTreeResponse> {

    /**
     * 节点ID
     */
    @ChineseDescription("节点ID")
    private Long id;

    /**
     * 节点父ID
     */
    @ChineseDescription("节点父ID")
    private Long pid;

    /**
     * 节点名称
     */
    @ChineseDescription("节点名称")
    private String name;

    /**
     * 是否是菜单(如果是false,则pid是菜单的id)
     */
    @ChineseDescription("是否是菜单(如果是false,则pid是菜单的id)")
    private Boolean menuFlag;

    /**
     * 是否选择(已拥有的是true)
     */
    @ChineseDescription("是否选择(已拥有的是true)")
    private Boolean checked;

    /**
     * 按钮code
     */
    @ChineseDescription("按钮code")
    private String buttonCode;

    /**
     * 子节点集合
     */
    @ChineseDescription("子节点集合")
    private List<LayuiMenuAndButtonTreeResponse> children;

    @Override
    public String getNodeId() {
        return this.id.toString();
    }

    @Override
    public String getNodeParentId() {
        return this.pid.toString();
    }

    @Override
    public void setChildrenNodes(List<LayuiMenuAndButtonTreeResponse> childrenNodes) {
        this.children = childrenNodes;
    }
}
