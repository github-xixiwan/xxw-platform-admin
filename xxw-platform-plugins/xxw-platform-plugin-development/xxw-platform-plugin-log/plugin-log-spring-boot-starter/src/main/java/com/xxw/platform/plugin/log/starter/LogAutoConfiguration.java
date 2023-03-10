package com.xxw.platform.plugin.log.starter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.xxw.platform.plugin.log.api.LogManagerApi;
import com.xxw.platform.plugin.log.api.LogRecordApi;
import com.xxw.platform.plugin.log.api.enums.LogSaveTypeEnum;
import com.xxw.platform.plugin.log.api.expander.LogConfigExpander;
import com.xxw.platform.plugin.log.api.pojo.log.SysLogProperties;
import com.xxw.platform.plugin.log.api.threadpool.LogManagerThreadPool;
import com.xxw.platform.plugin.log.business.request.RequestApiLogRecordAop;
import com.xxw.platform.plugin.log.sdk.db.DbLogManagerServiceImpl;
import com.xxw.platform.plugin.log.sdk.db.DbLogRecordServiceImpl;
import com.xxw.platform.plugin.log.sdk.db.service.SysLogService;
import com.xxw.platform.plugin.log.sdk.db.service.impl.SysLogServiceImpl;
import com.xxw.platform.plugin.log.sdk.file.FileLogManagerServiceImpl;
import com.xxw.platform.plugin.log.sdk.file.FileLogRecordServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 系统日志的自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 17:12
 */
@Configuration
public class LogAutoConfiguration {

    /**
     * 日志配置的前缀
     */
    public static final String SYS_LOG_PREFIX = "sys-log";
    /**
     * 系统日志service
     *
     * @return sysLogService
     * @author liaoxiting
     * @date 2020/12/28 22:09
     */
    @Bean
    @ConditionalOnMissingBean(SysLogService.class)
    @ConditionalOnProperty(prefix = SYS_LOG_PREFIX, name = "type", havingValue = "db")
    public SysLogService sysLogService() {
        return new SysLogServiceImpl();
    }

    /**
     * 系统日志的配置
     *
     * @author liaoxiting
     * @date 2020/12/20 14:17
     */
    @Bean
    @ConfigurationProperties(prefix = SYS_LOG_PREFIX)
    public SysLogProperties sysLogProperties() {
        return new SysLogProperties();
    }

    /**
     * 每个请求接口记录日志的AOP
     * 根据配置文件初始化日志记录器
     * 日志存储类型：db-数据库，file-文件，默认存储在数据库中
     *
     * @param sysLogProperties 系统日志配置文件
     * @param sysLogService    系统日志service
     * @author liaoxiting
     * @date 2020/12/20 13:02
     */
    @Bean
    public RequestApiLogRecordAop requestApiLogRecordAop(SysLogProperties sysLogProperties, SysLogServiceImpl sysLogService) {

        // 如果类型是文件
        if (StrUtil.isNotBlank(sysLogProperties.getType())
                && LogSaveTypeEnum.FILE.getCode().equals(sysLogProperties.getType())) {

            // 修改为从sys_config中获取日志存储位置
            String fileSavePath = "";
            if (SystemUtil.getOsInfo().isWindows()) {
                fileSavePath = LogConfigExpander.getLogFileSavePathWindows();
            } else {
                fileSavePath = LogConfigExpander.getLogFileSavePathLinux();
            }

            return new RequestApiLogRecordAop(new FileLogRecordServiceImpl(fileSavePath, new LogManagerThreadPool()));
        }

        // 其他情况用数据库存储日志
        return new RequestApiLogRecordAop(new DbLogRecordServiceImpl(new LogManagerThreadPool(), sysLogService));
    }

    /**
     * 日志管理器
     *
     * @param sysLogProperties 系统日志配置文件
     * @author liaoxiting
     * @date 2020/12/20 18:53
     */
    @Bean
    public LogManagerApi logManagerApi(SysLogProperties sysLogProperties) {

        // 如果类型是文件
        if (StrUtil.isNotBlank(sysLogProperties.getType())
                && LogSaveTypeEnum.FILE.getCode().equals(sysLogProperties.getType())) {

            // 修改为从sys_config中获取日志存储位置
            String fileSavePath = "";
            if (SystemUtil.getOsInfo().isWindows()) {
                fileSavePath = LogConfigExpander.getLogFileSavePathWindows();
            } else {
                fileSavePath = LogConfigExpander.getLogFileSavePathLinux();
            }

            return new FileLogManagerServiceImpl(fileSavePath);
        }

        // 其他情况用数据库存储日志
        return new DbLogManagerServiceImpl();
    }

    /**
     * 日志记录的api
     *
     * @author liaoxiting
     * @date 2021/3/4 22:16
     */
    @Bean
    public LogRecordApi logRecordApi(SysLogServiceImpl sysLogService) {
        return new DbLogRecordServiceImpl(new LogManagerThreadPool(), sysLogService);
    }

}
