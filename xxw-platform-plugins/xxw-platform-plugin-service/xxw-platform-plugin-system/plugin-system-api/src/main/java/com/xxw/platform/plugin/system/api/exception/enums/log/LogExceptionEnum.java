package com.xxw.platform.plugin.system.api.exception.enums.log;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import lombok.Getter;

/**
 * 日志相关异常枚举
 *
 * @author liaoxiting
 * @date 2021/2/8 17:45
 */
@Getter
public enum LogExceptionEnum implements AbstractExceptionEnum {

    /**
     * 日志不存在
     */
    LOG_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "21", "日志不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    LogExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
