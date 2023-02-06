package com.xxw.platform.plugin.security.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.security.api.constants.SecurityConstants;
import lombok.Getter;

/**
 * XSS过滤异常的枚举
 *
 * @author liaoxiting
 * @date 2021/1/13 23:23
 */
@Getter
public enum XssFilterExceptionEnum implements AbstractExceptionEnum {

    /**
     * XSS初始化配置为空
     */
    CONFIG_IS_NULL(RuleConstants.BUSINESS_ERROR_TYPE_CODE + SecurityConstants.SECURITY_EXCEPTION_STEP_CODE + "11", "XSS初始化配置为空，请检查XSS过滤器配置是否正确！");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    XssFilterExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
