package com.xxw.platform.plugin.scanner.api;

import com.xxw.platform.plugin.scanner.api.pojo.resource.ReportResourceParam;
import com.xxw.platform.plugin.scanner.api.pojo.resource.SysResourcePersistencePojo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 资源持久化服务api，将扫描的资源汇报给系统管理用
 *
 * @author liaoxiting
 * @date 2018-02-06 14:30
 */
public interface ResourceReportApi {

    /**
     * 持久化资源集合到某个服务中
     * <p>
     * 如果是单体项目，则吧资源汇报给本服务
     * <p>
     * 如果是微服务项目，则会有个consumer会将本服务的资源发送给资源管理者（一般为system服务）
     *
     * @param reportResourceReq 资源汇报接口
     * @author liaoxiting
     * @date 2020/10/19 22:02
     */
    @RequestMapping(value = "/resourceService/reportResources", method = RequestMethod.POST)
    void reportResources(@RequestBody ReportResourceParam reportResourceReq);

    /**
     * 持久化资源集合到某个服务中
     * <p>
     * 如果是单体项目，则吧资源汇报给本服务
     * <p>
     * 如果是微服务项目，则会有个consumer会将本服务的资源发送给资源管理者（一般为system服务）
     *
     * @param reportResourceReq 资源汇报接口
     * @author liaoxiting
     * @date 2020/10/19 22:02
     */
    @RequestMapping(value = "/resourceService/reportResourcesAndGetResult", method = RequestMethod.POST)
    List<SysResourcePersistencePojo> reportResourcesAndGetResult(@RequestBody ReportResourceParam reportResourceReq);

}
