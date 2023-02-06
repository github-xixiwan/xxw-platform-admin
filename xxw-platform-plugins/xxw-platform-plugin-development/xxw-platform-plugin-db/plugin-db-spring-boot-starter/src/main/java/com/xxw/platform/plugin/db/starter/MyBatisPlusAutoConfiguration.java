package com.xxw.platform.plugin.db.starter;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.xxw.platform.plugin.db.mp.dbid.CustomDatabaseIdProvider;
import com.xxw.platform.plugin.db.mp.fieldfill.CustomMetaObjectHandler;
import com.xxw.platform.plugin.db.mp.injector.CustomInsertBatchSqlInjector;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus的插件配置
 *
 * @author liaoxiting
 * @date 2020/11/30 22:40
 */
@Configuration
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class MyBatisPlusAutoConfiguration {

    /**
     * 新的分页插件
     *
     * @author liaoxiting
     * @date 2020/12/24 13:13
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 使用分页插插件
        interceptor.addInnerInterceptor(paginationInterceptor());

        // 使用乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());

        return interceptor;
    }

    /**
     * 分页插件
     *
     * @author liaoxiting
     * @date 2020/11/30 22:41
     */
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }

    /**
     * 乐观锁插件
     *
     * @author liaoxiting
     * @date 2021/10/28 17:52
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 公共字段填充插件
     *
     * @author liaoxiting
     * @date 2020/11/30 22:41
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    /**
     * 数据库id选择器，兼容多个数据库sql脚本
     *
     * @author liaoxiting
     * @date 2020/11/30 22:42
     */
    @Bean
    public CustomDatabaseIdProvider customDatabaseIdProvider() {
        return new CustomDatabaseIdProvider();
    }

    /**
     * 自定义sqlInjector
     *
     * @author liaoxiting
     * @date 2022/9/17 14:28
     */
    @Bean
    public CustomInsertBatchSqlInjector customInsertBatchSqlInjector() {
        return new CustomInsertBatchSqlInjector();
    }

}
