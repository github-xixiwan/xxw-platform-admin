package com.xxw.platform.plugin.office.api.exception;

import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.office.api.constants.OfficeConstants;

/**
 * Office模块异常
 *
 * @author liaoxiting
 * @date 2020/11/4 10:15
 */
public class OfficeException extends ServiceException {

    public OfficeException(AbstractExceptionEnum exception) {
        super(OfficeConstants.OFFICE_MODULE_NAME, exception);
    }

    public OfficeException(String errorCode, String userTip) {
        super(OfficeConstants.OFFICE_MODULE_NAME, errorCode, userTip);
    }

}
