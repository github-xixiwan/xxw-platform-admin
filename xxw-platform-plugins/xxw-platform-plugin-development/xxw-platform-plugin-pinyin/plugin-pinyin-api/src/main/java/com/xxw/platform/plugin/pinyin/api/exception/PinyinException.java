package com.xxw.platform.plugin.pinyin.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.pinyin.api.constants.PinyinConstants;
import lombok.Getter;

/**
 * 拼音异常
 *
 * @author liaoxiting
 * @date 2020/12/3 18:10
 */
@Getter
public class PinyinException extends ServiceException {

    public PinyinException(AbstractExceptionEnum exceptionEnum) {
        super(PinyinConstants.PINYIN_MODULE_NAME, exceptionEnum);
    }

    public PinyinException(AbstractExceptionEnum exception, Object... params) {
        super(PinyinConstants.PINYIN_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

}
