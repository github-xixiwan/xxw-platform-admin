package com.xxw.platform.frame.common.enums;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xxw.platform.frame.common.base.ReadableEnum;
import lombok.Getter;

/**
 * 公共状态，一般用来表示开启和关闭
 *
 * @author liaoxiting
 * @date 2020/10/14 21:31
 */
@Getter
public enum StatusEnum implements ReadableEnum<StatusEnum> {

    /**
     * 启用
     */
    ENABLE(1, "启用"),

    /**
     * 禁用
     */
    DISABLE(2, "禁用");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取枚举
     *
     * @author liaoxiting
     * @date 2020/10/29 18:59
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatusEnum codeToEnum(Integer code) {
        if (null != code) {
            for (StatusEnum item : StatusEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public Object getKey() {
        return this.code;
    }

    @Override
    public Object getName() {
        return this.message;
    }

    @Override
    public StatusEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (StatusEnum value : StatusEnum.values()) {
            if (value.code.equals(Convert.toInt(originValue))) {
                return value;
            }
        }
        return null;
    }
}
