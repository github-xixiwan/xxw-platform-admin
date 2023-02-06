package com.xxw.platform.plugin.scanner.starter;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.plugin.scanner.api.DevOpsDetectApi;
import com.xxw.platform.plugin.scanner.api.ResourceCollectorApi;
import com.xxw.platform.plugin.scanner.api.pojo.devops.DevOpsReportProperties;
import com.xxw.platform.plugin.scanner.api.pojo.scanner.ScannerProperties;
import com.xxw.platform.plugin.scanner.sdk.ApiResourceScanner;
import com.xxw.platform.plugin.scanner.sdk.DefaultResourceCollector;
import com.xxw.platform.plugin.scanner.sdk.devops.DefaultDevOpsReportImpl;
import com.xxw.platform.plugin.scanner.sdk.devops.LocalizedDevOpsReportImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 资源的自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 17:24
 */
@Configuration
public class ResourceAutoConfiguration {

    public static final String SCANNER_PREFIX = "scanner";

    public static final String DEVOPS_REPORT_PREFIX = "devops";

    @Value("${spring.application.name:}")
    private String springApplicationName;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * 资源扫描器的配置
     *
     * @author liaoxiting
     * @date 2020/12/3 17:54
     */
    @Bean
    @ConfigurationProperties(prefix = SCANNER_PREFIX)
    public ScannerProperties scannerProperties() {
        return new ScannerProperties();
    }

    /**
     * DevOps一体化平台的交互配置
     *
     * @author liaoxiting
     * @date 2020/12/3 17:54
     */
    @Bean
    @ConfigurationProperties(prefix = DEVOPS_REPORT_PREFIX)
    public DevOpsReportProperties devOpsReportProperties() {
        return new DevOpsReportProperties();
    }

    /**
     * 资源扫描器
     *
     * @author liaoxiting
     * @date 2020/12/1 17:29
     */
    @Bean
    @ConditionalOnMissingBean(ApiResourceScanner.class)
    @ConditionalOnProperty(prefix = ResourceAutoConfiguration.SCANNER_PREFIX, name = "open", havingValue = "true")
    public ApiResourceScanner apiResourceScanner(ResourceCollectorApi resourceCollectorApi, ScannerProperties scannerProperties) {
        if (StrUtil.isBlank(scannerProperties.getAppCode())) {
            scannerProperties.setAppCode(springApplicationName);
        }
        if (StrUtil.isBlank(scannerProperties.getContextPath())) {
            scannerProperties.setContextPath(contextPath);
        }
        return new ApiResourceScanner(resourceCollectorApi, scannerProperties);
    }

    /**
     * 资源搜集器
     *
     * @author liaoxiting
     * @date 2020/12/1 17:29
     */
    @Bean
    @ConditionalOnMissingBean(ResourceCollectorApi.class)
    @ConditionalOnProperty(prefix = ResourceAutoConfiguration.SCANNER_PREFIX, name = "open", havingValue = "true")
    public ResourceCollectorApi resourceCollectorApi() {
        return new DefaultResourceCollector();
    }

    /**
     * 向DevOps平台汇报资源，传统方式，远程资源汇报
     *
     * @author liaoxiting
     * @date 2022/4/2 14:41
     */
    @Bean
    @ConditionalOnMissingBean(DevOpsDetectApi.class)
    public DefaultDevOpsReportImpl defaultDevOpsReport() {
        return new DefaultDevOpsReportImpl();
    }

    /**
     * 向DevOps平台汇报资源，新方式，本地化集成运维平台
     *
     * @author liaoxiting
     * @date 2022/10/18 0:03
     */
    @Bean
    @ConditionalOnBean(DevOpsDetectApi.class)
    public LocalizedDevOpsReportImpl localizedDevOpsReport() {
        return new LocalizedDevOpsReportImpl();
    }

}
