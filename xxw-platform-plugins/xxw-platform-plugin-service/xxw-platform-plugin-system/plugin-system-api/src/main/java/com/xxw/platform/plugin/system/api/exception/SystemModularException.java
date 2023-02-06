package com.xxw.platform.plugin.system.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;

/**
 * 系统管理模块的异常
 *
 * @author liaoxiting
 * @date 2020/11/4 15:50
 */
public class SystemModularException extends ServiceException {

    public SystemModularException(AbstractExceptionEnum exception, Object... params) {
        super(SystemConstants.SYSTEM_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public SystemModularException(AbstractExceptionEnum exception) {
        super(SystemConstants.SYSTEM_MODULE_NAME, exception);
    }

}
