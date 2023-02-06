package com.xxw.platform.plugin.ds.sdk.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.xxw.platform.frame.common.listener.ContextInitializedListener;
import com.xxw.platform.plugin.db.api.factory.DruidDatasourceFactory;
import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import com.xxw.platform.plugin.ds.api.exception.DatasourceContainerException;
import com.xxw.platform.plugin.ds.api.exception.enums.DatasourceContainerExceptionEnum;
import com.xxw.platform.plugin.ds.sdk.context.DataSourceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 多数据源的初始化，加入到数据源Context中的过程
 *
 * @author liaoxiting
 * @date 2020/11/1 0:02
 */
@Slf4j
public class DataSourceInitListener extends ContextInitializedListener implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200;
    }

    @Override
    public void eventCallback(ApplicationContextInitializedEvent event) {

        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String dataSourceDriver = environment.getProperty("spring.datasource.driver-class-name");
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername, dataSourcePassword)) {
            String userTip = StrUtil.format(DatasourceContainerExceptionEnum.DB_CONNECTION_INFO_EMPTY_ERROR.getUserTip(), dataSourceUrl, dataSourceUsername);
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.DB_CONNECTION_INFO_EMPTY_ERROR, userTip);
        }

        // 创建主数据源的properties
        DruidProperties druidProperties = new DruidProperties();
        druidProperties.setDriverClassName(dataSourceDriver);
        druidProperties.setUrl(dataSourceUrl);
        druidProperties.setUsername(dataSourceUsername);
        druidProperties.setPassword(dataSourcePassword);

        // 设置其他数据源配置
        this.setOtherDruidConfigs(environment, druidProperties);

        // 创建主数据源
        DruidDataSource druidDataSource = DruidDatasourceFactory.createDruidDataSource(druidProperties);

        // 初始化数据源容器
        try {
            DataSourceContext.initDataSource(druidProperties, druidDataSource);
        } catch (Exception exception) {
            log.error("初始化数据源容器错误!", exception);
            String userTip = StrUtil.format(DatasourceContainerExceptionEnum.INIT_DATASOURCE_CONTAINER_ERROR.getUserTip(), exception.getMessage());
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.INIT_DATASOURCE_CONTAINER_ERROR, userTip);
        }

    }

    /**
     * 配置其他的druid数据源配控制
     *
     * @author liaoxiting
     * @date 2022/9/15 11:00
     */
    private void setOtherDruidConfigs(ConfigurableEnvironment environment, DruidProperties druidProperties) {

        // 连接池的相关配置
        String initialSize = environment.getProperty("spring.datasource.initialSize");
        String maxActive = environment.getProperty("spring.datasource.maxActive");
        String minIdle = environment.getProperty("spring.datasource.minIdle");
        String maxWait = environment.getProperty("spring.datasource.maxWait");
        String poolPreparedStatements = environment.getProperty("spring.datasource.poolPreparedStatements");
        String maxPoolPreparedStatementPerConnectionSize = environment.getProperty("spring.datasource.maxPoolPreparedStatementPerConnectionSize");
        String validationQuery = environment.getProperty("spring.datasource.validationQuery");
        String validationQueryTimeout = environment.getProperty("spring.datasource.validationQueryTimeout");
        String testOnBorrow = environment.getProperty("spring.datasource.testOnBorrow");
        String testOnReturn = environment.getProperty("spring.datasource.testOnReturn");
        String testWhileIdle = environment.getProperty("spring.datasource.testWhileIdle");
        String keepAlive = environment.getProperty("spring.datasource.keepAlive");
        String timeBetweenEvictionRunsMillis = environment.getProperty("spring.datasource.timeBetweenEvictionRunsMillis");
        String minEvictableIdleTimeMillis = environment.getProperty("spring.datasource.minEvictableIdleTimeMillis");
        String filters = environment.getProperty("spring.datasource.filters");

        // 设置配置
        if (ObjectUtil.isNotEmpty(initialSize)) {
            Integer intValue = Convert.toInt(initialSize);
            druidProperties.setInitialSize(intValue);
        }

        if (ObjectUtil.isNotEmpty(maxActive)) {
            Integer intValue = Convert.toInt(maxActive);
            druidProperties.setMaxActive(intValue);
        }

        if (ObjectUtil.isNotEmpty(minIdle)) {
            Integer intValue = Convert.toInt(minIdle);
            druidProperties.setMinIdle(intValue);
        }

        if (ObjectUtil.isNotEmpty(maxWait)) {
            Integer intValue = Convert.toInt(maxWait);
            druidProperties.setMaxWait(intValue);
        }

        if (ObjectUtil.isNotEmpty(poolPreparedStatements)) {
            boolean booleanValue = Convert.toBool(poolPreparedStatements);
            druidProperties.setPoolPreparedStatements(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(maxPoolPreparedStatementPerConnectionSize)) {
            Integer intValue = Convert.toInt(maxPoolPreparedStatementPerConnectionSize);
            druidProperties.setMaxPoolPreparedStatementPerConnectionSize(intValue);
        }

        if (ObjectUtil.isNotEmpty(validationQuery)) {
            druidProperties.setValidationQuery(validationQuery);
        }

        if (ObjectUtil.isNotEmpty(validationQueryTimeout)) {
            Integer intValue = Convert.toInt(validationQueryTimeout);
            druidProperties.setValidationQueryTimeout(intValue);
        }

        if (ObjectUtil.isNotEmpty(testOnBorrow)) {
            boolean booleanValue = Convert.toBool(testOnBorrow);
            druidProperties.setTestOnBorrow(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(testOnReturn)) {
            boolean booleanValue = Convert.toBool(testOnReturn);
            druidProperties.setTestOnReturn(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(testWhileIdle)) {
            boolean booleanValue = Convert.toBool(testWhileIdle);
            druidProperties.setTestWhileIdle(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(keepAlive)) {
            boolean booleanValue = Convert.toBool(keepAlive);
            druidProperties.setKeepAlive(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(timeBetweenEvictionRunsMillis)) {
            Integer intValue = Convert.toInt(timeBetweenEvictionRunsMillis);
            druidProperties.setTimeBetweenEvictionRunsMillis(intValue);
        }

        if (ObjectUtil.isNotEmpty(minEvictableIdleTimeMillis)) {
            Integer intValue = Convert.toInt(minEvictableIdleTimeMillis);
            druidProperties.setMinEvictableIdleTimeMillis(intValue);
        }

        if (ObjectUtil.isNotEmpty(filters)) {
            druidProperties.setFilters(filters);
        }
    }

}
