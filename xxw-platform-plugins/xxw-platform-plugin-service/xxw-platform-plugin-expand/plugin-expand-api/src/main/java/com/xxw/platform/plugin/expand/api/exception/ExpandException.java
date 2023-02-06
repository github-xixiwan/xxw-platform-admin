package com.xxw.platform.plugin.expand.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.expand.api.constants.ExpandConstants;

/**
 * 拓展字段异常
 *
 * @author liaoxiting
 * @date 2022-03-29 23:14:31
 */
public class ExpandException extends ServiceException {

    public ExpandException(AbstractExceptionEnum exception, Object... params) {
        super(ExpandConstants.EXPAND_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public ExpandException(AbstractExceptionEnum exception) {
        super(ExpandConstants.EXPAND_MODULE_NAME, exception);
    }

}
