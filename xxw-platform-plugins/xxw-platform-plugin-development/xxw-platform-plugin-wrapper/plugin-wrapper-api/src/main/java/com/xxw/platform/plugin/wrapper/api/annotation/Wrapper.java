package com.xxw.platform.plugin.wrapper.api.annotation;

import com.xxw.platform.plugin.wrapper.api.BaseWrapper;

import java.lang.annotation.*;

/**
 * 结果包装的注解，一般用在Controller层，给最后响应结果做包装
 *
 * @author liaoxiting
 * @date 2020/7/24 17:10
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Wrapper {

    /**
     * 具体包装类
     */
    Class<? extends BaseWrapper<?>>[] value();

}
