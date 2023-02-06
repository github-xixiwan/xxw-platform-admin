package com.xxw.platform.plugin.validator.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.validator.api.constants.ValidatorConstants;

/**
 * 参数校验异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class ParamValidateException extends ServiceException {

    public ParamValidateException(AbstractExceptionEnum exception, Object... params) {
        super(ValidatorConstants.VALIDATOR_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public ParamValidateException(AbstractExceptionEnum exception) {
        super(ValidatorConstants.VALIDATOR_MODULE_NAME, exception);
    }

}
