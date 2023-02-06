package com.xxw.platform.frame.common.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xxw.platform.frame.common.base.ReadableEnum;
import lombok.Getter;

/**
 * 性别的枚举
 *
 * @author liaoxiting
 * @date 2020/10/17 10:01
 */
@Getter
public enum SexEnum implements ReadableEnum<SexEnum> {

    /**
     * 男
     */
    M("M", "男"),

    /**
     * 女
     */
    F("F", "女");

    @EnumValue
    @JsonValue
    private final String code;

    private final String message;

    SexEnum(String code, String message) {
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
    public static SexEnum codeToEnum(String code) {
        if (null != code) {
            for (SexEnum e : SexEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * 编码转化成中文含义
     *
     * @author liaoxiting
     * @date 2021/1/11 22:34
     */
    public static String codeToMessage(String code) {
        if (null != code) {
            for (SexEnum e : SexEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e.getMessage();
                }
            }
        }
        return "未知";
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
    public SexEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (SexEnum value : SexEnum.values()) {
            if (value.code.equals(originValue)) {
                return value;
            }
        }
        return null;
    }
}
