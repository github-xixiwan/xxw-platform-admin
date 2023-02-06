package com.xxw.platform.plugin.monitor.api.exception;

import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.monitor.api.constants.MonitorConstants;

/**
 * 监控模块异常
 *
 * @author liaoxiting
 * @date 2021/1/31 22:35
 */
public class MonitorException extends ServiceException {

    public MonitorException(AbstractExceptionEnum exception) {
        super(MonitorConstants.MONITOR_MODULE_NAME, exception);
    }

}
