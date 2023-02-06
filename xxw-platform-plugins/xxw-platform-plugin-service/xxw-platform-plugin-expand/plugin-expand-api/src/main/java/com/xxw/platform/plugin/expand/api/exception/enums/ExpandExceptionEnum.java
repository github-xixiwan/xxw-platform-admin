package com.xxw.platform.plugin.expand.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.expand.api.constants.ExpandConstants;
import lombok.Getter;

/**
 * 拓展字段的异常枚举
 *
 * @author liaoxiting
 * @date 2022-03-29 23:14:31
 */
@Getter
public enum ExpandExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询不到对应拓展字段
     */
    CANT_FIND_EXPAND(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ExpandConstants.EXPAND_EXCEPTION_STEP_CODE + "01", "查询不到对应拓展字段，具体信息：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    ExpandExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
