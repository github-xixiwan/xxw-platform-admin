package com.xxw.platform.plugin.socket.api.exception;

import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.socket.api.constants.SocketConstants;

/**
 * Socket模块异常
 *
 * @author liaoxiting
 * @date 2021/6/1 上午11:23
 */
public class SocketException extends ServiceException {

    public SocketException(AbstractExceptionEnum exception) {
        super(SocketConstants.SOCKET_MODULE_NAME, exception);
    }

    public SocketException(String errorCode, String userTip) {
        super(SocketConstants.SOCKET_MODULE_NAME, errorCode, userTip);
    }

}
