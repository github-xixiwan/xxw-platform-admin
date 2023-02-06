package com.xxw.platform.plugin.security.sdk.df.annotation;

import java.lang.annotation.*;

/**
 * 需要加密字段注解
 * <p>
 * 该注解作用范围在字段上面(该类需要加 {@link ProtectedData} 注解)
 *
 * @author liaoxiting
 * @date 2021/7/3 10:57
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

}
