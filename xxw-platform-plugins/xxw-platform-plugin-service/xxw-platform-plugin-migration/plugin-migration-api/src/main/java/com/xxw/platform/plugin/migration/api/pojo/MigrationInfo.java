package com.xxw.platform.plugin.migration.api.pojo;

import lombok.Data;

/**
 * 迁移数据信息
 *
 * @author liaoxiting
 * @date 2021/7/6 16:30
 */
@Data
public class MigrationInfo {

    /**
     * 版本
     */
    private String version;

    /**
     * 数据
     */
    private Object data;

}
