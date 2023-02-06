package com.xxw.platform.plugin.scanner.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;
import lombok.Getter;

/**
 * DevOps一体化平台异常枚举
 *
 * @author liaoxiting
 * @date 2022/1/11 17:31
 */
@Getter
public enum DevOpsExceptionEnum implements AbstractExceptionEnum {

    /**
     * DevOps汇报资源出错，Http调用出错！
     */
    HTTP_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ScannerConstants.RESOURCE_MODULE_NAME + "01", "DevOps汇报资源出错，Http调用出错！"),

    /**
     * DevOps汇报资源出错，Http调用获取返回结果出错！
     */
    HTTP_RESPONSE_EMPTY(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ScannerConstants.RESOURCE_MODULE_NAME + "02", "DevOps汇报资源出错，Http调用获取返回结果为空！"),

    /**
     * DevOps汇报资源出错，Http调用获取返回结果出错！
     */
    HTTP_RESPONSE_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ScannerConstants.RESOURCE_MODULE_NAME + "02", "DevOps汇报资源出错，Http调用获取返回结果错误！具体原因为：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    DevOpsExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
