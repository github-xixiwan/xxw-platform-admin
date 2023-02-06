package com.xxw.platform.plugin.scanner.sdk;

import cn.hutool.core.util.ObjectUtil;
import com.xxw.platform.frame.common.listener.ApplicationReadyListener;
import com.xxw.platform.plugin.scanner.api.DevOpsDetectApi;
import com.xxw.platform.plugin.scanner.api.DevOpsReportApi;
import com.xxw.platform.plugin.scanner.api.ResourceCollectorApi;
import com.xxw.platform.plugin.scanner.api.ResourceReportApi;
import com.xxw.platform.plugin.scanner.api.constants.ScannerConstants;
import com.xxw.platform.plugin.scanner.api.holder.InitScanFlagHolder;
import com.xxw.platform.plugin.scanner.api.pojo.devops.DevOpsReportProperties;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ReportResourceParam;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import com.xxw.platform.plugin.scanner.api.pojo.resource.SysResourcePersistencePojo;
import com.xxw.platform.plugin.scanner.api.pojo.scanner.ScannerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import java.util.List;
import java.util.Map;

/**
 * 监听项目初始化完毕，汇报资源到服务（可为远程服务，可为本服务）
 *
 * @author liaoxiting
 * @date 2020/10/19 22:27
 */
@Slf4j
public class ResourceReportListener extends ApplicationReadyListener implements Ordered {

    @Override
    public void eventCallback(ApplicationReadyEvent event) {

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();

        // 获取有没有开资源扫描开关
        ScannerProperties scannerProperties = applicationContext.getBean(ScannerProperties.class);
        if (!scannerProperties.getOpen()) {
            // 设置已经扫描标识
            InitScanFlagHolder.setFlag();
            return;
        }

        // 如果项目还没进行资源扫描
        if (!InitScanFlagHolder.getFlag()) {

            long beginSaveLocal = System.currentTimeMillis();

            // 获取当前系统的所有资源
            ResourceCollectorApi resourceCollectorApi = applicationContext.getBean(ResourceCollectorApi.class);
            Map<String, Map<String, ResourceDefinition>> modularResources = resourceCollectorApi.getModularResources();

            // 持久化资源，发送资源到资源服务或本项目
            ResourceReportApi resourceService = applicationContext.getBean(ResourceReportApi.class);
            List<SysResourcePersistencePojo> persistencePojos = resourceService.reportResourcesAndGetResult(new ReportResourceParam(scannerProperties.getAppCode(), modularResources));

            long saveLocalFinish = System.currentTimeMillis();
            log.info("存储本地接口资源完成，耗时：{}ms", (saveLocalFinish - beginSaveLocal));

            // 向DevOps一体化平台汇报资源，只有两种情况会汇报资源：1.本地配置了远程服务器地址；2.本地化集成了devops模块
            DevOpsReportProperties devOpsReportProperties = applicationContext.getBean(DevOpsReportProperties.class);
            DevOpsDetectApi devOpsDetectApi = null;
            try {
                devOpsDetectApi = applicationContext.getBean(DevOpsDetectApi.class);
            } catch (Exception ignored) {
            }

            // 判断是否配置了host或者本地有集成化的devops平台
            if (ObjectUtil.isNotEmpty(devOpsReportProperties.getServerHost()) || devOpsDetectApi != null) {
                DevOpsReportApi devOpsReportApi = applicationContext.getBean(DevOpsReportApi.class);
                try {
                    devOpsReportApi.reportResources(devOpsReportProperties, persistencePojos);
                    log.info("向DevOps平台汇报资源信息完成，耗时：{}ms", (System.currentTimeMillis() - saveLocalFinish));
                } catch (Exception e) {
                    log.error("向DevOps平台汇报异常出现网络错误，如无法联通DevOps平台可关闭相关配置。", e);
                }
            }

            // 设置标识已经扫描过
            InitScanFlagHolder.setFlag();
        }

    }

    @Override
    public int getOrder() {
        return ScannerConstants.REPORT_RESOURCE_LISTENER_SORT;
    }

}
