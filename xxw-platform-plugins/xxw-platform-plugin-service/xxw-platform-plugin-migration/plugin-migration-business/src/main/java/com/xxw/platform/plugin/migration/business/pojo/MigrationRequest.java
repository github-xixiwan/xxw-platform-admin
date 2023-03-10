package com.xxw.platform.plugin.migration.business.pojo;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.util.List;

/**
 * 迁移数据请求对象
 *
 * @author liaoxiting
 * @date 2021/7/7 9:15
 */
@Data
public class MigrationRequest {

    /**
     * 应用名称
     */
    @ChineseDescription("应用名称")
    private String appName;

    /**
     * 模块列表
     */
    @ChineseDescription("模块列表")
    private List<String> moduleNames;

}
