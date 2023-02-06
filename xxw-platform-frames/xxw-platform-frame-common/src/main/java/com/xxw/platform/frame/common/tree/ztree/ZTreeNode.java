package com.xxw.platform.frame.common.tree.ztree;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.constants.TreeConstants;
import com.xxw.platform.frame.common.tree.factory.base.AbstractTreeNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * jquery zTree 插件的节点封装
 *
 * @author liaoxiting
 * @date 2021/1/6 21:47
 */
@ToString
@EqualsAndHashCode
public class ZTreeNode implements AbstractTreeNode<ZTreeNode> {

    /**
     * 节点id
     */
    @Getter
    @Setter
    @ChineseDescription("节点id")
    private Long id;

    /**
     * 父节点id
     */
    @ChineseDescription("父节点id")
    private Long pId;

    /**
     * 节点名称
     */
    @Getter
    @Setter
    @ChineseDescription("节点名称")
    private String name;

    /**
     * 是否打开节点
     */
    @Getter
    @Setter
    @ChineseDescription("是否打开节点")
    private Boolean open;

    /**
     * 是否被选中
     */
    @Getter
    @Setter
    @ChineseDescription("是否被选中")
    private Boolean checked;

    /**
     * 节点图标  single or group
     */
    @Getter
    @Setter
    @ChineseDescription("节点图标")
    private String iconSkin;

    /**
     * 子节点集合
     */
    @Getter
    @Setter
    @ChineseDescription("子节点集合")
    private List<ZTreeNode> children;

    /**
     * 创建ztree的父级节点
     *
     * @author liaoxiting
     * @date 2021/1/6 21:47
     */
    public static ZTreeNode createParent() {
        ZTreeNode zTreeNode = new ZTreeNode();
        zTreeNode.setChecked(true);
        zTreeNode.setId(TreeConstants.DEFAULT_PARENT_ID);
        zTreeNode.setName("顶级");
        zTreeNode.setOpen(true);
        zTreeNode.setpId(TreeConstants.VIRTUAL_ROOT_PARENT_ID);
        return zTreeNode;
    }
    @Override
    public String getNodeId() {
        return id.toString();
    }

    @Override
    public String getNodeParentId() {
        return pId.toString();
    }

    @Override
    public void setChildrenNodes(List<ZTreeNode> childrenNodes) {
        this.children = childrenNodes;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getpId() {
        return pId;
    }
}
