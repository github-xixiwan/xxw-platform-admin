package com.xxw.platform.plugin.scanner.api;

import com.xxw.platform.plugin.scanner.api.pojo.devops.DevOpsReportProperties;
import com.xxw.platform.plugin.scanner.api.pojo.resource.SysResourcePersistencePojo;

import java.util.List;

/**
 * 向DevOps一体化平台汇报资源的api
 *
 * @author liaoxiting
 * @date 2022/1/11 14:57
 */
public interface DevOpsReportApi {

    /**
     * 向DevOps一体化平台汇报资源
     *
     * @param devOpsReportProperties         DevOps平台的系统配置
     * @param sysResourcePersistencePojoList 资源汇报具体数据
     * @author liaoxiting
     * @date 2022/1/11 15:02
     */
    void reportResources(DevOpsReportProperties devOpsReportProperties, List<SysResourcePersistencePojo> sysResourcePersistencePojoList);

}
