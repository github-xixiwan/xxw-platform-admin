package com.xxw.platform.frame.common.annotation;

import com.xxw.platform.frame.common.base.SimpleFieldFormatProcess;
import com.xxw.platform.frame.common.enums.FormatTypeEnum;

import java.lang.annotation.*;

/**
 * json字段的格式化，可以将类似id值，转化为具体的具有可读性的名称，例如：用户id -> 用户姓名
 *
 * @author liaoxiting
 * @date 2022/9/6 11:34
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SimpleFieldFormat {

    /**
     * 字段格式化的类型 详情见：{@link FormatTypeEnum}
     * <p>
     * 默认采用包装型，不改变原有的字段
     */
    FormatTypeEnum formatType() default FormatTypeEnum.ADD_FIELD;

    /**
     * 具体处理值转化的过程【必传】
     */
    Class<? extends SimpleFieldFormatProcess> processClass();

}
