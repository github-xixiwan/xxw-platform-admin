package com.xxw.platform.plugin.ds.starter;

import com.xxw.platform.plugin.ds.sdk.DynamicDataSource;
import com.xxw.platform.plugin.ds.sdk.aop.MultiSourceExchangeAop;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据库连接和DAO框架的配置
 * <p>
 * 如果开启此连接池，注意排除 DataSourceAutoConfiguration
 *
 * @author liaoxiting
 * @date 2020/11/30 22:24
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnMissingBean(DataSource.class)
public class DataSourceContainerAutoConfiguration {

    /**
     * 多数据源连接池，如果开启此连接池，注意排除 DataSourceAutoConfiguration
     *
     * @author liaoxiting
     * @date 2020/11/30 22:49
     */
    @Bean
    public DynamicDataSource dataSource() {
        return new DynamicDataSource();
    }

    /**
     * 数据源切换的AOP
     *
     * @author liaoxiting
     * @date 2020/11/30 22:49
     */
    @Bean
    public MultiSourceExchangeAop multiSourceExchangeAop() {
        return new MultiSourceExchangeAop();
    }

}
