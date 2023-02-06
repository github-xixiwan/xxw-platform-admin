package com.xxw.platform.plugin.config.api.exception;

import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.config.api.constants.ConfigConstants;

/**
 * 系统配置表的异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class ConfigException extends ServiceException {

    public ConfigException(String errorCode, String userTip) {
        super(ConfigConstants.CONFIG_MODULE_NAME, errorCode, userTip);
    }

    public ConfigException(AbstractExceptionEnum exception) {
        super(ConfigConstants.CONFIG_MODULE_NAME, exception);
    }

    public ConfigException(AbstractExceptionEnum exception, String userTip) {
        super(ConfigConstants.CONFIG_MODULE_NAME, exception.getErrorCode(), userTip);
    }

}
