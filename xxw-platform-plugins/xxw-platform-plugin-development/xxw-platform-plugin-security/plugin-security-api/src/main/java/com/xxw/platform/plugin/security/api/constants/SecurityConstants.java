package com.xxw.platform.plugin.security.api.constants;

/**
 * 安全模块常量
 *
 * @author liaoxiting
 * @date 2021/2/19 8:45
 */
public interface SecurityConstants {

    /**
     * 安全模块的名称
     */
    String SECURITY_MODULE_NAME = "xxw-platform-plugin-security";

    /**
     * 异常枚举的步进值
     */
    String SECURITY_EXCEPTION_STEP_CODE = "28";

    /**
     * XSS默认拦截范围
     */
    String DEFAULT_XSS_PATTERN = "/*";

    /**
     * 默认验证码的开关：关闭
     */
    Boolean DEFAULT_CAPTCHA_OPEN = false;

}
