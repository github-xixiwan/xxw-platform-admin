package com.xxw.platform.plugin.scanner.api.pojo.resource;

import com.xxw.platform.plugin.scanner.api.enums.ParamTypeEnum;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * 方法请求参数的详细信息
 *
 * @author liaoxiting
 * @date 2022/1/20 11:46
 */
@Data
public class ParameterMetadata {

    /**
     * 请求参数类型,param参数还是request body参数
     */
    private ParamTypeEnum paramTypeEnum;

    /**
     * 请求参数的所有注解
     */
    private Annotation[] annotations;

    /**
     * 参数的类型
     */
    private Type parameterizedType;

    /**
     * 参数名，用来记录get请求的参数，例如String userAccount这种参数名userAccount
     */
    private String parameterName;

}
