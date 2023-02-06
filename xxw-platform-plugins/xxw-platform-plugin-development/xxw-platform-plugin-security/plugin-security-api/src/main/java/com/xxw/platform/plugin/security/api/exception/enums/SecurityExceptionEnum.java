package com.xxw.platform.plugin.security.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.security.api.constants.SecurityConstants;
import lombok.Getter;

/**
 * 安全模块异常枚举
 *
 * @author liaoxiting
 * @date 2021/2/19 8:46
 */
@Getter
public enum SecurityExceptionEnum implements AbstractExceptionEnum {

    /**
     * 生成验证码错误
     */
    CAPTCHA_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + SecurityConstants.SECURITY_EXCEPTION_STEP_CODE + "01", "生成验证码错误"),

    /**
     * 验证码过期，请从新生成验证码
     */
    CAPTCHA_INVALID_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + SecurityConstants.SECURITY_EXCEPTION_STEP_CODE + "02", "验证码过期，请从新生成验证码");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SecurityExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
