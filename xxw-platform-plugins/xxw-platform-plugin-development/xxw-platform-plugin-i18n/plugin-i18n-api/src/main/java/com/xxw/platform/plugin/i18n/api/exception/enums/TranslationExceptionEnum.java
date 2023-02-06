package com.xxw.platform.plugin.i18n.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.i18n.api.constants.TranslationConstants;
import lombok.Getter;

/**
 * 多语言翻译的异常枚举
 *
 * @author liaoxiting
 * @date 2021/1/24 16:40
 */
@Getter
public enum TranslationExceptionEnum implements AbstractExceptionEnum {

    /**
     * 多语言记录不存在
     */
    NOT_EXISTED(RuleConstants.BUSINESS_ERROR_TYPE_CODE + TranslationConstants.I18N_EXCEPTION_STEP_CODE + "01", "多语言记录不存在，id为：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    TranslationExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
