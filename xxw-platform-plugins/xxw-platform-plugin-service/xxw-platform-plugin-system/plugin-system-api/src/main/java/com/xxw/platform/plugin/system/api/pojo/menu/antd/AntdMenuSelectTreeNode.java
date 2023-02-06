package com.xxw.platform.plugin.system.api.pojo.menu.antd;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜单树节点，用在新增和修改菜单，下拉选父级时候
 *
 * @author liaoxiting
 * @date 2020/4/5 12:03
 */
@Data
public class AntdMenuSelectTreeNode implements AbstractTreeNode<AntdMenuSelectTreeNode> {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long id;

    /**
     * 父id
     */
    @ChineseDescription("父id")
    private Long parentId;

    /**
     * 名称
     */
    @ChineseDescription("名称")
    private String title;

    /**
     * 值
     */
    @ChineseDescription("值")
    private String value;

    /**
     * 排序，越小优先级越高
     */
    @ChineseDescription("排序，越小优先级越高")
    private BigDecimal weight;

    /**
     * 子节点
     */
    @ChineseDescription("子节点")
    private List<AntdMenuSelectTreeNode> children;

    @Override
    public String getNodeId() {
        return id.toString();
    }

    @Override
    public String getNodeParentId() {
        return this.parentId.toString();
    }

    @Override
    public void setChildrenNodes(List<AntdMenuSelectTreeNode> childrenNodes) {
        this.children = childrenNodes;
    }
}
