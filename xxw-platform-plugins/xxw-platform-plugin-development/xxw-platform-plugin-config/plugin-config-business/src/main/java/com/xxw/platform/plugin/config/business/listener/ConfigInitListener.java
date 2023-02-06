package com.xxw.platform.plugin.config.business.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import com.xxw.platform.frame.common.context.ApplicationPropertiesContext;
import com.xxw.platform.frame.common.listener.ContextInitializedListener;
import com.xxw.platform.plugin.config.api.context.ConfigContext;
import com.xxw.platform.plugin.config.api.exception.ConfigException;
import com.xxw.platform.plugin.config.api.exception.enums.ConfigExceptionEnum;
import com.xxw.platform.plugin.config.business.factory.SysConfigDataFactory;
import com.xxw.platform.plugin.config.sdk.map.ConfigContainer;
import com.xxw.platform.plugin.config.sdk.redis.RedisConfigContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 初始化系统配置表
 * <p>
 * 当spring装配好配置后，就去数据库读constants
 * <p>
 *
 * @author liaoxiting
 * @date 2020/6/6 23:39
 */
@Slf4j
public class ConfigInitListener extends ContextInitializedListener implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    @Override
    public void eventCallback(ApplicationContextInitializedEvent event) {

        // 获取environment参数
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        // 是否采用redis进行sys_config缓存的装载和读取（true/false）
        String redisConfigCacheEnable = environment.getProperty("redis.config.cache.enable");
        Boolean redisConfigCacheEnableFlag = Convert.toBool(redisConfigCacheEnable, false);

        if (redisConfigCacheEnableFlag) {
            // 获取Redis的相关配置
            String redisHost = environment.getProperty("spring.redis.host");
            String redisPort = environment.getProperty("spring.redis.port");
            String redisPassword = environment.getProperty("spring.redis.password");
            String dbNumber = environment.getProperty("spring.redis.database");

            // 初始化Config Api
            ConfigContext.setConfigApi(new RedisConfigContainer(redisHost, Convert.toInt(redisPort, 6379), redisPassword, Convert.toInt(dbNumber, 0)));
        } else {
            // 初始化Config Api，内存方式
            ConfigContext.setConfigApi(new ConfigContainer());
        }

        // 初始化ApplicationPropertiesContext
        ApplicationPropertiesContext.getInstance().initConfigs(environment);

        // 获取数据库连接配置
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername, dataSourcePassword)) {
            throw new ConfigException(ConfigExceptionEnum.APP_DB_CONFIG_ERROR);
        }

        Connection conn = null;
        try {
            Class.forName(driverClassName);
            assert dataSourceUrl != null;
            conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);

            // 获取sys_config表的数据
            List<Entity> entityList = SysConfigDataFactory.getSysConfigDataApi(dataSourceUrl).getConfigs(conn);

            // 将查询到的参数配置添加到缓存
            if (ObjectUtil.isNotEmpty(entityList)) {
                entityList.forEach(sysConfig -> ConfigContext.me().putConfig(sysConfig.getStr("config_code"), sysConfig.getStr("config_value")));
            }
        } catch (ClassNotFoundException e) {
            log.error("初始化系统配置表失败，找不到{}类 : {}", driverClassName, e);
            throw new ConfigException(ConfigExceptionEnum.CLASS_NOT_FOUND_ERROR);
        } catch (SQLException sqlException) {
            log.error("初始化系统配置表失败，执行查询语句失败", sqlException);
            throw new ConfigException(ConfigExceptionEnum.CONFIG_SQL_EXE_ERROR);
        } finally {
            DbUtil.close(conn);
        }
    }

}
