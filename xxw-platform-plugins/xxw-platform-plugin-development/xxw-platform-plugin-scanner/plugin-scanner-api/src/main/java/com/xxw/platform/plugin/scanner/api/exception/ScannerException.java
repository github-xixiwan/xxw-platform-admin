package com.xxw.platform.plugin.scanner.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;

/**
 * 资源模块的异常
 *
 * @author liaoxiting
 * @date 2020/11/3 13:54
 */
public class ScannerException extends ServiceException {

    public ScannerException(AbstractExceptionEnum exception) {
        super(ScannerConstants.RESOURCE_MODULE_NAME, exception);
    }

    public ScannerException(AbstractExceptionEnum exception, Object... params) {
        super(ScannerConstants.RESOURCE_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

}
