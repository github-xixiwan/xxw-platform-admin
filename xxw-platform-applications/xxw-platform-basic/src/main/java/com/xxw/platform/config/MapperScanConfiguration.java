package com.xxw.platform.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 配置mapper包扫描
 *
 * @author fengshuonan
 * @since 2020/12/13 16:11
 */
@Configuration
@MapperScan(basePackages = {"com.xxw.platform.**.mapper"})
public class MapperScanConfiguration {

}
