package com.xxw.platform.plugin.file.api.pojo.props;

import lombok.Data;

/**
 * MinIO文件存储配置
 *
 * @author liaoxiting
 * @date 2020/10/31 11:19
 */
@Data
public class MinIoProperties {

    /**
     * 服务器端点 MinIO服务器地址 默认：http://127.0.0.1:9000
     */
    private String endpoint = "http://127.0.0.1:9000";

    /**
     * MinIO accessKey
     */
    private String accessKey;

    /**
     * MinIO secretKey
     */
    private String secretKey;

}
