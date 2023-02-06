package com.xxw.platform.plugin.system.api.pojo.menu;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 新版的角色绑定菜单和按钮，用在角色分配菜单和按钮节点
 *
 * @author liaoxiting
 * @date 2021/8/10 22:36
 */
@Data
public class MenuAndButtonTreeResponse implements AbstractTreeNode<MenuAndButtonTreeResponse> {

    /**
     * 节点ID，可以是菜单id和按钮id
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
     * code
     */
    @ChineseDescription("code")
    private String code;

    /**
     * 是否选择(已拥有的是true)
     */
    @ChineseDescription("是否选择(已拥有的是true)")
    private Boolean checked;

    /**
     * 当前节点的按钮集合
     */
    @ChineseDescription("当前节点的按钮集合")
    private List<MenuAndButtonTreeResponse> buttons;

    /**
     * 子节点集合
     */
    @ChineseDescription("子节点集合")
    private List<MenuAndButtonTreeResponse> children;

    @Override
    public String getNodeId() {
        if (this.id != null) {
            return this.id.toString();
        } else {
            return "";
        }
    }

    @Override
    public String getNodeParentId() {
        if (this.pid != null) {
            return this.pid.toString();
        } else {
            return "";
        }
    }

    @Override
    public void setChildrenNodes(List<MenuAndButtonTreeResponse> childrenNodes) {
        this.children = childrenNodes;
    }
}
