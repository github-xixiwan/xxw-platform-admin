package com.xxw.platform.plugin.security.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.security.api.constants.SecurityConstants;

/**
 * 安全模块异常
 *
 * @author liaoxiting
 * @date 2021/2/19 8:48
 */
public class SecurityException extends ServiceException {

    public SecurityException(AbstractExceptionEnum exception, Object... params) {
        super(SecurityConstants.SECURITY_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public SecurityException(AbstractExceptionEnum exception) {
        super(SecurityConstants.SECURITY_MODULE_NAME, exception);
    }

}
