package com.xxw.platform.plugin.message.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.message.api.constants.MessageConstants;

/**
 * 消息异常枚举
 *
 * @author liaoxiting
 * @date 2021/1/1 20:55
 */
public class MessageException extends ServiceException {

    public MessageException(AbstractExceptionEnum exception, Object... params) {
        super(MessageConstants.MESSAGE_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public MessageException(AbstractExceptionEnum exception) {
        super(MessageConstants.MESSAGE_MODULE_NAME, exception);
    }

}
