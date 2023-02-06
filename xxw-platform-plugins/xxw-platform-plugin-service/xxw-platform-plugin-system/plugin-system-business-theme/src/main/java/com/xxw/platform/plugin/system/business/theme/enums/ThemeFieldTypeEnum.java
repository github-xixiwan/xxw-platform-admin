package com.xxw.platform.plugin.system.business.theme.enums;

import lombok.Getter;

/**
 * 字段类型枚举
 *
 * @author liaoxiting
 * @date 2022/1/1 22:29
 */
@Getter
public enum ThemeFieldTypeEnum {

    /**
     * 字符串类型
     */
    STRING("string", "字符串类型"),

    /**
     * 文件类型
     */
    FILE("file", "文件类型");

    private final String code;

    private final String message;

    ThemeFieldTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
