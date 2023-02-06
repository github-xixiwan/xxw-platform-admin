package com.xxw.platform.plugin.group.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.group.api.constants.GroupConstants;
import lombok.Getter;

/**
 * 业务分组的异常枚举
 *
 * @author liaoxiting
 * @date 2022-06-24 17:15:41
 */
@Getter
public enum GroupExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询不到对应业务分组
     */
    CANT_FIND_GROUP(RuleConstants.BUSINESS_ERROR_TYPE_CODE + GroupConstants.GROUP_EXCEPTION_STEP_CODE + "01", "查询不到对应业务分组，具体信息：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    GroupExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
