package com.xxw.platform.plugin.ds.sdk;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.plugin.ds.api.constants.DatasourceContainerConstants;
import com.xxw.platform.plugin.ds.api.context.CurrentDataSourceContext;
import com.xxw.platform.plugin.ds.sdk.context.DataSourceContext;

import javax.sql.DataSource;
import java.util.Map;
/**
 * 动态数据源的实现，带切换功能的
 *
 * @author liaoxiting
 * @date 2020/11/1 0:08
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 决断当前正在进行的service或者mapper用哪个数据源
     *
     * @author liaoxiting
     * @date 2020/11/1 0:08
     */
    @Override
    protected DataSource determineDataSource() {

        // 获取当前Context存储的数据源名称
        String dataSourceName = CurrentDataSourceContext.getDataSourceName();

        // 如果当前Context没有值，就用主数据源
        if (StrUtil.isEmpty(dataSourceName)) {
            dataSourceName = DatasourceContainerConstants.MASTER_DATASOURCE_NAME;
        }

        // 从数据源容器中获取对应的数据源
        Map<String, DataSource> dataSources = DataSourceContext.getDataSources();
        return dataSources.get(dataSourceName);
    }

}
