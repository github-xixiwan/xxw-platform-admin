package com.xxw.platform.plugin.system.business.home.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 常用功能的统计次数异常相关枚举
 *
 * @author liaoxiting
 * @date 2022/02/10 21:17
 */
@Getter
public enum SysStatisticsCountExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_STATISTICS_COUNT_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在"),

    /**
     * 请求参数不完整，无法查询用户请求次数
     */
    PARAM_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10002", "请求参数不完整，无法查询用户请求次数");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysStatisticsCountExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
