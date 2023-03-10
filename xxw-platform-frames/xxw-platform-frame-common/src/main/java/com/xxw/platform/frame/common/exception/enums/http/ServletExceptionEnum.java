package com.xxw.platform.frame.common.exception.enums.http;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * servlet相关业务异常
 *
 * @author liaoxiting
 * @date 2020/10/15 17:39
 */
@Getter
public enum ServletExceptionEnum implements AbstractExceptionEnum {

    /**
     * 获取不到http context异常
     */
    HTTP_CONTEXT_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + RuleConstants.RULE_EXCEPTION_STEP_CODE + "01", "获取不到http context，请确认当前请求是http请求");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    ServletExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
