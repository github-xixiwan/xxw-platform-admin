package com.xxw.platform.plugin.security.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.security.api.constants.SecurityConstants;

/**
 * XSS过滤异常
 *
 * @author liaoxiting
 * @date 2021/1/13 23:22
 */
public class XssFilterException extends ServiceException {

    public XssFilterException(AbstractExceptionEnum exception, Object... params) {
        super(SecurityConstants.SECURITY_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public XssFilterException(AbstractExceptionEnum exception) {
        super(SecurityConstants.SECURITY_MODULE_NAME, exception);
    }

}
