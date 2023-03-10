package com.xxw.platform.plugin.scanner.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;
import lombok.Getter;

/**
 * 资源相关的异常枚举
 *
 * @author liaoxiting
 * @date 2020/11/3 13:55
 */
@Getter
public enum ScannerExceptionEnum implements AbstractExceptionEnum {

    /**
     * 获取资源为空
     */
    RESOURCE_DEFINITION_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ScannerConstants.RESOURCE_MODULE_NAME + "01", "获取资源为空，请检查当前请求url是否存在对应的ResourceDefinition"),

    /**
     * 扫描资源过程中，存在不合法控制器名称，请将控制名称以Controller结尾
     */
    ERROR_CONTROLLER_NAME(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ScannerConstants.RESOURCE_MODULE_NAME + "02", "扫描资源过程中，存在不合法控制器名称，请将控制名称以Controller结尾，控制器名称：{}"),

    /**
     * 系统资源尚未初始化完毕，无法使用系统
     */
    SYSTEM_RESOURCE_URL_NOT_INIT(RuleConstants.BUSINESS_ERROR_TYPE_CODE + ScannerConstants.RESOURCE_MODULE_NAME + "03", "系统资源尚未初始化完毕，请稍后使用系统");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    ScannerExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
