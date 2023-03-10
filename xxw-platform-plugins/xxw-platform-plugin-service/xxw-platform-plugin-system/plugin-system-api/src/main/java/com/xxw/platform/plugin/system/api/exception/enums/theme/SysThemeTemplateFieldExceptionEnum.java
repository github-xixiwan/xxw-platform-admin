package com.xxw.platform.plugin.system.api.exception.enums.theme;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import lombok.Getter;

/**
 * 系统主题模板属性异常类
 *
 * @author liaoxiting
 * @date 2021/12/23 17:01
 */
@Getter
public enum SysThemeTemplateFieldExceptionEnum implements AbstractExceptionEnum {

    /**
     * 系统主题模板属性不存在
     */
    FIELD_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "101", "系统主题模板属性不存在"),

    /**
     * 系统主题模板属性被使用
     */
    FIELD_IS_USED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "102", "系统主题模板属性正在被使用，不允许操作");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysThemeTemplateFieldExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }
}
