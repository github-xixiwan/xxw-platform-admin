package com.xxw.platform.plugin.system.api.exception.enums.notice;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import lombok.Getter;

/**
 * 通知管理相关异常枚举
 *
 * @author liaoxiting
 * @date 2021/1/9 16:11
 */
@Getter
public enum NoticeExceptionEnum implements AbstractExceptionEnum {

    /**
     * 通知不存在
     */
    NOTICE_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "41", "通知不存在，id为：{}"),

    /**
     * 通知范围不允许修改
     */
    NOTICE_SCOPE_NOT_EDIT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "42", "通知范围不允许修改");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    NoticeExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
