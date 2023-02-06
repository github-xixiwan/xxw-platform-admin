package com.xxw.platform.plugin.dict.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.dict.api.constants.DictConstants;

/**
 * 字典模块的异常
 *
 * @author liaoxiting
 * @date 2020/10/29 11:57
 */
public class DictException extends ServiceException {

    public DictException(AbstractExceptionEnum exception, Object... params) {
        super(DictConstants.DICT_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public DictException(AbstractExceptionEnum exception) {
        super(DictConstants.DICT_MODULE_NAME, exception);
    }

}
