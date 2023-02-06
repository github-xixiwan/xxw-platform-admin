package com.xxw.platform.plugin.system.api.enums;

import lombok.Getter;

/**
 * 组织机构类型
 *
 * @author liaoxiting
 * @date 2022/5/21 11:26
 */
@Getter
public enum OrgTypeEnum {

    /**
     * 公司
     */
    COMPANY(1),

    /**
     * 部门
     */
    DEPT(2);

    private final Integer code;

    OrgTypeEnum(Integer code) {
        this.code = code;
    }

}
