package com.xxw.platform.core.exception;

import com.xxw.platform.core.consts.ProjectConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;

/**
 * 业务异常
 *
 * @author liaoxiting
 * @since 2020/12/16 14:26
 */
public class BusinessException extends ServiceException {

    public BusinessException(AbstractExceptionEnum exception, String userTip) {
        super(ProjectConstants.PROJECT_MODULE_NAME, exception.getErrorCode(), userTip);
    }

    public BusinessException(AbstractExceptionEnum exception) {
        super(ProjectConstants.PROJECT_MODULE_NAME, exception);
    }

}
