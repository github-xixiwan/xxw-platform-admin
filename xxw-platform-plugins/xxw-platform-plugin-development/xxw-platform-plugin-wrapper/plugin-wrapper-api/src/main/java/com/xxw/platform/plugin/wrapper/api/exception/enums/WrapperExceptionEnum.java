package com.xxw.platform.plugin.wrapper.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.wrapper.api.constants.WrapperConstants;
import lombok.Getter;

/**
 * Wrapper异常的状态码
 *
 * @author liaoxiting
 * @date 2021/1/19 22:24
 */
@Getter
public enum WrapperExceptionEnum implements AbstractExceptionEnum {

    /**
     * 被包装的值不能是基本类型
     */
    BASIC_TYPE_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + WrapperConstants.WRAPPER_EXCEPTION_STEP_CODE + "01", "被包装的值不能是基本类型"),

    /**
     * 字段包装转化异常
     */
    TRANSFER_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + WrapperConstants.WRAPPER_EXCEPTION_STEP_CODE + "02", "字段包装转化异常，具体信息：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    WrapperExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
