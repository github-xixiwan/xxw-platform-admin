package com.xxw.platform.plugin.system.business.resource.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.plugin.scanner.api.ResourceReportApi;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ReportResourceParam;
import com.xxw.platform.plugin.scanner.api.pojo.resource.SysResourcePersistencePojo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微服务汇报资源接口
 *
 * @author liaoxiting
 * @date 2021/9/25 17:55
 */
@RestController
@ApiResource(name = "微服务汇报资源接口", resBizType = ResBizTypeEnum.SYSTEM)
public class ResourceReportController {

    @Resource
    private ResourceReportApi resourceReportApi;

    /**
     * 接收远程服务资源
     *
     * @author liaoxiting
     * @date 2021/9/25 17:55
     */
    @PostResource(path = "/resourceService/reportResources", name = "汇报资源")
    public void reportResources(@RequestBody ReportResourceParam reportResourceReq) {
        resourceReportApi.reportResources(reportResourceReq);
    }

    /**
     * 接收远程服务资源
     *
     * @author liaoxiting
     * @date 2021/9/25 17:55
     */
    @PostResource(path = "/resourceService/reportResourcesAndGetResult", name = "汇报资源")
    public List<SysResourcePersistencePojo> reportResourcesAndGetResult(@RequestBody ReportResourceParam reportResourceReq) {
        return resourceReportApi.reportResourcesAndGetResult(reportResourceReq);
    }

}
