package com.xxw.platform.plugin.file.api.pojo.props;

import lombok.Data;

/**
 * 腾讯云cos文件存储配置
 *
 * @author liaoxiting
 * @date 2020/10/26 11:49
 */
@Data
public class TenCosProperties {

    /**
     * secretId
     */
    private String secretId;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 地域id（默认北京）
     */
    private String regionId = "ap-beijing";

}
