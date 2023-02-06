package com.xxw.platform.plugin.file.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.file.api.constants.FileConstants;

/**
 * 系统配置表的异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class FileException extends ServiceException {

    public FileException(AbstractExceptionEnum exception) {
        super(FileConstants.FILE_MODULE_NAME, exception);
    }

    public FileException(AbstractExceptionEnum exception, Object... params) {
        super(FileConstants.FILE_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

}
