package com.xxw.platform.plugin.i18n.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.i18n.api.constants.TranslationConstants;

/**
 * 多语言翻译的异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class TranslationException extends ServiceException {

    public TranslationException(AbstractExceptionEnum exception, Object... params) {
        super(TranslationConstants.I18N_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public TranslationException(AbstractExceptionEnum exception) {
        super(TranslationConstants.I18N_MODULE_NAME, exception);
    }

}
