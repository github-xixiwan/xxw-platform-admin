package com.xxw.platform.plugin.db.api.exception.enums;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.plugin.db.api.constants.DbConstants;
import lombok.Getter;

/**
 * 不同数据库类型的枚举
 * <p>
 * 用于标识mapping.xml中不同数据库的标识
 *
 * @author liaoxiting
 * @date 2020/6/20 21:08
 */
@Getter
public enum DbInitEnum implements AbstractExceptionEnum {

    /**
     * 初始化数据库，存在为空的字段
     */
    INIT_TABLE_EMPTY_PARAMS(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "01", "初始化数据库，存在为空的字段"),

    /**
     * 数据库字段与实体字段不一致
     */
    FIELD_VALIDATE_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "02", "数据库字段与实体字段不一致");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    DbInitEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
