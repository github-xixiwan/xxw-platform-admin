package com.xxw.platform.plugin.file.starter;

import com.xxw.platform.plugin.file.api.FileOperatorApi;
import com.xxw.platform.plugin.file.api.expander.FileConfigExpander;
import com.xxw.platform.plugin.file.api.pojo.props.LocalFileProperties;
import com.xxw.platform.plugin.file.sdk.local.LocalFileOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件的自动配置
 *
 * @author liaoxiting
 * @date 2020/12/1 14:34
 */
@Configuration
public class FileAutoConfiguration {

    /**
     * 本地文件操作
     *
     * @author liaoxiting
     * @date 2020/12/1 14:40
     */
    @Bean
    @ConditionalOnMissingBean(FileOperatorApi.class)
    public FileOperatorApi fileOperatorApi() {

        LocalFileProperties localFileProperties = new LocalFileProperties();

        // 从系统配置表中读取配置
        localFileProperties.setLocalFileSavePathLinux(FileConfigExpander.getLocalFileSavePathLinux());
        localFileProperties.setLocalFileSavePathWin(FileConfigExpander.getLocalFileSavePathWindows());

        return new LocalFileOperator(localFileProperties);
    }

}
