package com.xxw.platform.plugin.socket.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.socket.api.constants.SocketConstants;
import lombok.Getter;

/**
 * Socket模块相关异常枚举
 *
 * @author liaoxiting
 * @date 2021/6/1 上午11:25
 */
@Getter
public enum SocketExceptionEnum implements AbstractExceptionEnum {

    /**
     * Socket操作异常
     */
    SOCKET_ERROR(RuleConstants.THIRD_ERROR_TYPE_CODE + SocketConstants.SOCKET_EXCEPTION_STEP_CODE + "01", "操作异常，具体信息为：{}"),

    /**
     * 会话不存在
     */
    SESSION_NOT_EXIST(RuleConstants.THIRD_ERROR_TYPE_CODE + SocketConstants.SOCKET_EXCEPTION_STEP_CODE + "02", "会话不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SocketExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
