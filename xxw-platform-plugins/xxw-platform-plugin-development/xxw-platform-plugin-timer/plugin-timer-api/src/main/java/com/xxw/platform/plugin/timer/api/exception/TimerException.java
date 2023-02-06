package com.xxw.platform.plugin.timer.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.timer.api.constants.TimerConstants;

/**
 * 定时器任务的异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class TimerException extends ServiceException {

    public TimerException(AbstractExceptionEnum exception) {
        super(TimerConstants.TIMER_MODULE_NAME, exception);
    }

    public TimerException(AbstractExceptionEnum exception, Object... params) {
        super(TimerConstants.TIMER_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

}
