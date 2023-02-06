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
public enum DatabaseExceptionEnum implements AbstractExceptionEnum {

    /**
     * 创建数据库异常
     */
    CREATE_DATABASE_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "01", "创建数据库异常，具体信息：{}"),

    /**
     * 查询表的所有字段错误
     */
    FIELD_GET_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "02", "查询表的所有字段错误，具体信息：{}"),

    /**
     * 查询所有表错误
     */
    TABLE_LIST_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "03", "查询所有表错误，具体信息：{}"),

    /**
     * sql执行错误
     */
    SQL_EXEC_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "04", "sql执行错误，具体信息：{}"),

    /**
     * 查询所有库错误
     */
    DATABASE_LIST_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + DbConstants.DB_EXCEPTION_STEP_CODE + "05", "查询所有库错误，具体信息：{}");
    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    DatabaseExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
