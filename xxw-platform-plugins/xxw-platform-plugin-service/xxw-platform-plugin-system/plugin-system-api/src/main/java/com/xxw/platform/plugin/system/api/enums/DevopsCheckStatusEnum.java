package com.xxw.platform.plugin.system.api.enums;

import lombok.Getter;

/**
 * 运维平台检测状态
 *
 * @author liaoxiting
 * @date 2020/11/23 21:30
 */
@Getter
public enum DevopsCheckStatusEnum {

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(0),

    /**
     * 账号密码错误
     */
    ACCOUNT_PASSWORD_ERROR(1),

    /**
     * 请求方未打开开关
     */
    REQUESTER_NOT_OPEN_SWITCH(2),

    /**
     * 成功
     */
    SUCCESSFUL(999);

    private final Integer code;

    DevopsCheckStatusEnum(Integer code) {
        this.code = code;
    }

}
