package com.xxw.platform.plugin.security.sdk.read.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.security.sdk.read.constants.EncryptionConstants;

/**
 * 请求解密，响应加密 异常
 *
 * @author liaoxiting
 * @date 2021/3/23 12:54
 */
public class EncryptionException extends ServiceException {

    public EncryptionException(AbstractExceptionEnum exception, Object... params) {
        super(EncryptionConstants.ENCRYPTION_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public EncryptionException(AbstractExceptionEnum exception) {
        super(EncryptionConstants.ENCRYPTION_MODULE_NAME, exception);
    }

}
