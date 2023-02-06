package com.xxw.platform.plugin.auth.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.auth.api.constants.AuthConstants;

/**
 * 认证类异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class AuthException extends ServiceException {

    public AuthException(AbstractExceptionEnum exception, Object... params) {
        super(AuthConstants.AUTH_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public AuthException(AbstractExceptionEnum exception) {
        super(AuthConstants.AUTH_MODULE_NAME, exception);
    }

}
