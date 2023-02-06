package com.xxw.platform.core.consts;

import com.xxw.platform.BasicApplication;

/**
 * 项目的常量
 *
 * @author liaoxiting
 * @since 2020/12/16 14:28
 */
public interface ProjectConstants {

    /**
     * 项目的模块名称
     */
    String PROJECT_MODULE_NAME = "xxw-platform-frame-web";

    /**
     * 异常枚举的步进值
     */
    String BUSINESS_EXCEPTION_STEP_CODE = "100";

    /**
     * 项目的包名
     */
    String ROOT_PACKAGE_NAME = BasicApplication.class.getPackage().getName();

}
