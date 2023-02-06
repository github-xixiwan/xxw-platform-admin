package com.xxw.platform.plugin.db.flyway;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.listener.ContextInitializedListener;
import com.xxw.platform.plugin.db.api.exception.DaoException;
import com.xxw.platform.plugin.db.api.exception.enums.FlywayExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 初始化flyway配置
 * <p>
 * 当spring装配好配置后开始初始化
 *
 * @author liaoxiting
 * @date 2021/1/17 21:14
 */
@Slf4j
public class FlywayInitListener extends ContextInitializedListener implements Ordered {

    private static final String FLYWAY_LOCATIONS = "classpath:db/migration/mysql";

    @Override
    public void eventCallback(ApplicationContextInitializedEvent event) {

        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");

        // 判断是否开启 sharding jdbc
        Boolean enableSharding = environment.getProperty("spring.shardingsphere.enabled", Boolean.class);

        // 如果开启了sharding jdbc，则读取 sharding jdbc 主库配置
        if (ObjectUtil.isNotNull(enableSharding) && enableSharding) {
            driverClassName = environment.getProperty("spring.shardingsphere.datasource.master.driver-class-name");
            dataSourceUrl = environment.getProperty("spring.shardingsphere.datasource.master.url");
            dataSourceUsername = environment.getProperty("spring.shardingsphere.datasource.master.username");
            dataSourcePassword = environment.getProperty("spring.shardingsphere.datasource.master.password");
        }

        // flyway的配置
        String enabledStr = environment.getProperty("spring.flyway.enabled");
        String locations = environment.getProperty("spring.flyway.locations");
        String baselineOnMigrateStr = environment.getProperty("spring.flyway.baseline-on-migrate");
        String outOfOrderStr = environment.getProperty("spring.flyway.out-of-order");
        String placeholder = environment.getProperty("spring.flyway.placeholder-replacement");

        // 是否开启flyway，默认false.
        boolean enabled = false;
        if (StrUtil.isNotBlank(enabledStr)) {
            enabled = Boolean.parseBoolean(enabledStr);
        }

        // 如果未开启flyway 直接return
        if (!enabled) {
            return;
        }

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername, dataSourcePassword, driverClassName)) {
            throw new DaoException(FlywayExceptionEnum.DB_CONFIG_ERROR);
        }

        // 当迁移时发现目标schema非空，而且带有没有元数据的表时，是否自动执行基准迁移，默认false.
        boolean baselineOnMigrate = false;
        if (StrUtil.isNotBlank(baselineOnMigrateStr)) {
            baselineOnMigrate = Boolean.parseBoolean(baselineOnMigrateStr);
        }

        // 如果未设置flyway路径，则设置为默认flyway路径
        if (StrUtil.isBlank(locations)) {
            locations = FLYWAY_LOCATIONS;
        }

        // 是否允许无序的迁移 开发环境最好开启, 生产环境关闭
        boolean outOfOrder = false;
        if (StrUtil.isNotBlank(outOfOrderStr)) {
            outOfOrder = Boolean.parseBoolean(outOfOrderStr);
        }

        // 是否开启占位符
        boolean enablePlaceholder = true;
        if (StrUtil.isNotBlank(placeholder)) {
            enablePlaceholder = Boolean.parseBoolean(placeholder);
        }

        DriverManagerDataSource dmDataSource = null;
        try {
            assert dataSourceUrl != null;
            // 手动创建数据源
            dmDataSource = new DriverManagerDataSource();
            dmDataSource.setDriverClassName(driverClassName);
            dmDataSource.setUrl(dataSourceUrl);
            dmDataSource.setUsername(dataSourceUsername);
            dmDataSource.setPassword(dataSourcePassword);

            // flyway配置
            Flyway flyway = Flyway.configure()
                    .dataSource(dmDataSource)

                    // 迁移脚本的位置
                    .locations(locations)

                    // 当迁移时发现目标schema非空，而且带有没有元数据的表时，是否自动执行基准迁移
                    .baselineOnMigrate(baselineOnMigrate)

                    // 是否允许无序的迁移 开发环境最好开启 , 生产环境关闭
                    .outOfOrder(outOfOrder)

                    // 是否开启占位符
                    .placeholderReplacement(enablePlaceholder)

                    // 忽略丢失的脚本
                    .ignoreMissingMigrations(true)

                    .load();

            // 执行迁移
            flyway.migrate();

        } catch (Exception e) {
            log.error("flyway初始化失败", e);
            throw new DaoException(FlywayExceptionEnum.FLYWAY_MIGRATE_ERROR, e.getMessage());
        }

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
