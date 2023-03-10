package com.xxw.platform.plugin.scanner.api.pojo.devops;

import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;
import lombok.Data;

/**
 * 资源向DevOps平台汇总，需要进行的配置
 *
 * @author liaoxiting
 * @date 2022/1/11 14:29
 */
@Data
public class DevOpsReportProperties {

    /**
     * DevOps平台的服务端地址，例如：http://127.0.0.1:8087
     */
    private String serverHost;

    /**
     * 当前项目在DevOps平台的唯一标识，由DevOps平台颁发
     */
    private String projectUniqueCode;

    /**
     * 当前项目和DevOps平台的交互秘钥（jwt秘钥）
     */
    private String projectInteractionSecretKey;

    /**
     * Token的有效期
     */
    private Long tokenValidityPeriodSeconds;

    /**
     * FieldMetadata类的全路径
     * <p>
     * 默认是com.xxw.platform开头的
     * <p>
     * 如果FieldMetadata没有改过包名，不用管这个配置，如果FieldMetadata改过包名，这里则填写改过包名的全路径
     */
    private String fieldMetadataClassPath = ScannerConstants.FIELD_METADATA_CLASS_ALL_PATH;

}
