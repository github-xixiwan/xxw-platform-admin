package com.xxw.platform.frame.common.annotation;

import com.xxw.platform.frame.common.base.ReadableEnum;
import com.xxw.platform.frame.common.enums.FormatTypeEnum;
import java.lang.annotation.*;

/**
 * json字段的格式化，可以将枚举转化为可读性的值，例如：SexEnum.M -> "男"，例如："M" -> "男“
 *
 * @author liaoxiting
 * @date 2022/9/6 11:34
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface EnumFieldFormat {

    /**
     * 字段格式化的类型 详情见：{@link FormatTypeEnum}
     * <p>
     * 默认采用包装型，不改变原有的字段
     */
    FormatTypeEnum formatType() default FormatTypeEnum.ADD_FIELD;

    /**
     * 具体处理值转化过程的枚举【必传】
     */
    Class<? extends ReadableEnum<?>> processEnum();

}
