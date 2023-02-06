package com.xxw.platform.plugin.security.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.security.api.constants.SecurityConstants;
import lombok.Getter;

/**
 * 计数器校验
 *
 * @author liaoxiting
 * @date 2020/11/14 17:54
 */
@Getter
public enum CountValidateExceptionEnum implements AbstractExceptionEnum {

    /**
     * 中断执行
     */
    INTERRUPT_EXECUTION(RuleConstants.BUSINESS_ERROR_TYPE_CODE + SecurityConstants.SECURITY_EXCEPTION_STEP_CODE + "01", "满足自定义策略要求,程序已中断执行!"),

    /**
     * 限流错误
     */
    TRAFFIC_LIMIT_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + SecurityConstants.SECURITY_EXCEPTION_STEP_CODE + "02", "已触发限流机制,请稍后重新访问!");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    CountValidateExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
