package com.xxw.platform.frame.common.tree.factory.base;

import java.util.List;

/**
 * 树形节点的抽象接口
 *
 * @author liaoxiting
 * @date 2020/10/15 14:31
 */
public interface AbstractTreeNode<T> {

    /**
     * 获取节点id
     *
     * @return 节点的id标识
     * @author liaoxiting
     * @date 2020/10/15 15:28
     */
    String getNodeId();

    /**
     * 获取节点父id
     *
     * @return 父节点的id
     * @author liaoxiting
     * @date 2020/10/15 15:28
     */
    String getNodeParentId();

    /**
     * 设置children
     *
     * @param childrenNodes 设置节点的子节点
     * @author liaoxiting
     * @date 2020/10/15 15:28
     */
    void setChildrenNodes(List<T> childrenNodes);

}
