package com.xxw.platform.plugin.log.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.log.api.constants.LogConstants;

/**
 * 日志异常枚举
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class LogException extends ServiceException {

    public LogException(AbstractExceptionEnum exception, Object... params) {
        super(LogConstants.LOG_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public LogException(AbstractExceptionEnum exception) {
        super(LogConstants.LOG_MODULE_NAME, exception);
    }

}
