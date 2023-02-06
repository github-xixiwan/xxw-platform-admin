package com.xxw.platform.frame.common.annotation;

import java.lang.annotation.*;

/**
 * 用来标记在控制器类或方法上，进行判断是否需要对接口进行日志记录
 *
 * @author liaoxiting
 * @date 2022/1/12 20:48
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessLog {

    /**
     * 是否进行日志记录，默认是开启
     */
    boolean openLog() default true;

}
