package com.xxw.platform.plugin.security.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.security.api.constants.SecurityConstants;

/**
 * 计数器校验异常
 *
 * @author liaoxiting
 * @date 2020/11/14 17:53
 */
public class CountValidateException extends ServiceException {

    public CountValidateException(AbstractExceptionEnum exception, Object... params) {
        super(SecurityConstants.SECURITY_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public CountValidateException(AbstractExceptionEnum exception) {
        super(SecurityConstants.SECURITY_MODULE_NAME, exception);
    }

}
