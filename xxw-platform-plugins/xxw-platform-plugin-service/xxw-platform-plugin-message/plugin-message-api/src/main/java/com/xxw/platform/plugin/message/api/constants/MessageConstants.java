package com.xxw.platform.plugin.message.api.constants;

/**
 * message模块的常量
 *
 * @author liaoxiting
 * @date 2021/1/1 20:58
 */
public interface MessageConstants {

    /**
     * 消息模块的名称
     */
    String MESSAGE_MODULE_NAME = "xxw-platform-plugin-message";

    /**
     * 异常枚举的步进值
     */
    String MESSAGE_EXCEPTION_STEP_CODE = "23";

    /**
     * 发送所有用户标识
     */
    String RECEIVE_ALL_USER_FLAG = "all";

    /**
     * 默认websocket-url
     */
    String DEFAULT_WS_URL = "ws://localhost:8080/webSocket/{token}";

    /**
     * 系统配置中websocket url的变量编码
     */
    String WEB_SOCKET_WS_URL_CONFIG_CODE = "WEB_SOCKET_WS_URL";

}
