package com.xxw.platform.plugin.ds.api.annotation;

import java.lang.annotation.*;

/**
 * 多数据源标识的注解
 *
 * @author liaoxiting
 * @date 2020/10/31 22:50
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSource {

    /**
     * 数据源的名称
     */
    String name() default "";

}
