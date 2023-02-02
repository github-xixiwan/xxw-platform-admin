package com.xxw.platform.plugin.scanner.sdk.devops;

import com.xxw.platform.plugin.scanner.api.DevOpsDetectApi;
import com.xxw.platform.plugin.scanner.api.DevOpsReportApi;
import com.xxw.platform.plugin.scanner.api.pojo.devops.DevOpsReportProperties;
import com.xxw.platform.plugin.scanner.api.pojo.devops.DevOpsReportResourceParam;
import com.xxw.platform.plugin.scanner.api.pojo.resource.SysResourcePersistencePojo;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新的方式向devops平台汇报资源，通过本地化集成devops平台
 *
 * @author fengshuonan
 * @date 2022/10/18 0:04
 */
public class LocalizedDevOpsReportImpl implements DevOpsReportApi {

    @Resource
    private DevOpsDetectApi devOpsDetectApi;

    @Override
    public void reportResources(DevOpsReportProperties devOpsReportProperties, List<SysResourcePersistencePojo> sysResourcePersistencePojoList) {

        // 组装请求参数
        DevOpsReportResourceParam devOpsReportResourceParam = new DevOpsReportResourceParam(
                devOpsReportProperties.getProjectUniqueCode(), null, sysResourcePersistencePojoList, devOpsReportProperties.getFieldMetadataClassPath());

        // 进行post请求，汇报资源
        devOpsDetectApi.saveResource(devOpsReportResourceParam);
    }

}
