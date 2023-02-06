package com.xxw.platform.plugin.system.api.enums;

import lombok.Getter;

/**
 * 用户组的选择详情
 * <p>
 * 授权对象类型：1-用户，2-部门，3-角色，4-职位，5-关系，6-部门审批人
 *
 * @author liaoxiting
 * @date 2022/9/26 14:31
 */
@Getter
public enum UserGroupSelectTypeEnum {

    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 部门
     */
    DEPT(2, "部门"),

    /**
     * 角色
     */
    ROLE(3, "角色"),

    /**
     * 职位
     */
    POSITION(4, "职位"),

    /**
     * 关系
     */
    RELATION(5, "关系"),

    /**
     * 部门审批人
     */
    APPROVER(6, "部门审批人");

    private final Integer code;

    private final String message;

    UserGroupSelectTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
