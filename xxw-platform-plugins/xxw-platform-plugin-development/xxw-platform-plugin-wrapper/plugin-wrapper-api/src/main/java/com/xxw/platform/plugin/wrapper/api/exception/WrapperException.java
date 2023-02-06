package com.xxw.platform.plugin.wrapper.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.wrapper.api.constants.WrapperConstants;

/**
 * Wrapper异常
 *
 * @author liaoxiting
 * @date 2021/1/19 22:24
 */
public class WrapperException extends ServiceException {

    public WrapperException(AbstractExceptionEnum exception, Object... params) {
        super(WrapperConstants.WRAPPER_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public WrapperException(AbstractExceptionEnum exception) {
        super(WrapperConstants.WRAPPER_MODULE_NAME, exception);
    }

}
