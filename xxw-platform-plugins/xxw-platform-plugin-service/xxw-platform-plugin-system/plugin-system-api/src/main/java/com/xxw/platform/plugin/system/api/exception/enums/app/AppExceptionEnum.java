package com.xxw.platform.plugin.system.api.exception.enums.app;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import lombok.Getter;

/**
 * 系统应用相关异常枚举
 *
 * @author liaoxiting
 * @date 2020/3/26 10:11
 */
@Getter
public enum AppExceptionEnum implements AbstractExceptionEnum {

    /**
     * 默认激活的系统只能有一个
     */
    APP_ACTIVE_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "11", "默认激活的系统只能有一个，请检查active参数"),

    /**
     * 应用不存在
     */
    APP_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "12", "应用不存在"),

    /**
     * 该应用下有菜单
     */
    APP_CANNOT_DELETE(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "13", "该应用下有菜单，无法删除"),

    /**
     * 激活的应用不能被禁用
     */
    CANT_DISABLE(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "14", "激活的应用不能被禁用"),

    /**
     * 激活应用失败，该应用没有菜单
     */
    ACTIVE_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "15", "激活应用失败，该应用没有菜单");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    AppExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
