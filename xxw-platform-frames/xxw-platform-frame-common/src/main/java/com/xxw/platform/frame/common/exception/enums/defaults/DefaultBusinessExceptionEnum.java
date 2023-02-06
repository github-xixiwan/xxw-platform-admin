package com.xxw.platform.frame.common.exception.enums.defaults;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 系统执行出错，业务本身逻辑问题导致的错误（一级宏观码）
 *
 * @author liaoxiting
 * @date 2020/10/15 17:18
 */
@Getter
public enum DefaultBusinessExceptionEnum implements AbstractExceptionEnum {

    /**
     * 系统执行出错（一级宏观错误码）
     */
    SYSTEM_RUNTIME_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + RuleConstants.FIRST_LEVEL_WIDE_CODE, "系统执行出错，请检查系统运行状况"),

    /**
     * 404找不到资源
     */
    NOT_FOUND(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + RuleConstants.FIRST_LEVEL_WIDE_CODE, "404：找不到请求的资源");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    DefaultBusinessExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
