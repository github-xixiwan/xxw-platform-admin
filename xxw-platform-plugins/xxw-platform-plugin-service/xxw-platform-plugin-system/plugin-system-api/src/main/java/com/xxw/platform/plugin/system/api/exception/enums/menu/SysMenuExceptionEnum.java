package com.xxw.platform.plugin.system.api.exception.enums.menu;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;

/**
 * 系统菜单相关异常枚举
 *
 * @author liaoxiting
 * @date 2020/3/26 10:12
 */
public enum SysMenuExceptionEnum implements AbstractExceptionEnum {

    /**
     * 本菜单无法修改应用，非一级菜单，不能改变所属应用
     */
    CANT_MOVE_APP(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "32", "本菜单无法修改应用，非一级菜单，不能改变所属应用"),

    /**
     * 菜单不存在
     */
    MENU_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "33", "菜单不存在，菜单id：{}"),

    /**
     * 无法获取菜单，所有应用已被禁用
     */
    CANT_FIND_APPS(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SystemConstants.SYSTEM_EXCEPTION_STEP_CODE + "34", "无法获取菜单，所有应用已被禁用");

    private final String errorCode;

    private final String userTip;

    SysMenuExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getUserTip() {
        return userTip;
    }

}
