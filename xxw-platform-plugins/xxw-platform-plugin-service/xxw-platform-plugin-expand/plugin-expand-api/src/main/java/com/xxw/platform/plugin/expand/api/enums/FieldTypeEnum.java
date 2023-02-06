package com.xxw.platform.plugin.expand.api.enums;

import lombok.Getter;

/**
 * 字段类型枚举
 *
 * @author liaoxiting
 * @date 2022/4/1 10:34
 */
@Getter
public enum FieldTypeEnum {

    /**
     * 字符串
     */
    STR(1),

    /**
     * 数字
     */
    NUM(2),

    /**
     * 字典格式
     */
    DICT(3);

    private final Integer code;

    FieldTypeEnum(Integer code) {
        this.code = code;
    }

}
