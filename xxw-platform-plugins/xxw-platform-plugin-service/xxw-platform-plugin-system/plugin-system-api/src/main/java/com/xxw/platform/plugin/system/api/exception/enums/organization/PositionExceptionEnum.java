package com.xxw.platform.plugin.system.api.exception.enums.organization;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import lombok.Getter;

/**
 * 职务异常
 *
 * @author liaoxiting
 * @date 2020/11/6 18:08
 */
@Getter
public enum PositionExceptionEnum implements AbstractExceptionEnum {

    /**
     * 找不到职务
     */
    CANT_FIND_POSITION(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "58", "查询不到组该职务，错误的职务ID：{}"),

    /**
     * 职务删除失败
     */
    CANT_DELETE_POSITION(RuleConstants.BUSINESS_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "59", "职务删除失败，该职务下有关联人员");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    PositionExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
