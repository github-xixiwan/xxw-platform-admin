package com.xxw.platform.plugin.message.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.message.api.constants.MessageConstants;
import lombok.Getter;

/**
 * 消息异常枚举
 *
 * @author liaoxiting
 * @date 2021/1/1 21:14
 */
@Getter
public enum MessageExceptionEnum implements AbstractExceptionEnum {

    /**
     * 发送系统消息时，传入的参数中receiveUserIds不合法
     */
    ERROR_RECEIVE_USER_IDS(RuleConstants.BUSINESS_ERROR_TYPE_CODE + MessageConstants.MESSAGE_EXCEPTION_STEP_CODE + "01", "接收用户id字符串不合法！系统无人可接收消息！"),

    /**
     * 消息记录不存在
     */
    NOT_EXISTED(RuleConstants.BUSINESS_ERROR_TYPE_CODE + MessageConstants.MESSAGE_EXCEPTION_STEP_CODE + "01", "消息记录不存在，id为：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    MessageExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
