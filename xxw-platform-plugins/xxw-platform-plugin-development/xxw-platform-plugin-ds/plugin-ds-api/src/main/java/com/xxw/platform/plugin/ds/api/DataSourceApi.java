package com.xxw.platform.plugin.ds.api;

import com.xxw.platform.plugin.ds.api.exception.DatasourceContainerException;
import com.xxw.platform.plugin.ds.api.pojo.DataBaseInfoDto;
import com.xxw.platform.plugin.ds.api.pojo.DataSourceDto;
import com.xxw.platform.plugin.ds.api.pojo.request.DatabaseInfoRequest;

import java.util.List;

/**
 * 数据库连接的api
 *
 * @author liaoxiting
 * @date 2021/4/22 14:19
 */
public interface DataSourceApi {

    /**
     * 根据dbId获取数据库连接信息
     *
     * @param dbId 数据库连接id
     * @return 数据库连接信息
     * @throws DatasourceContainerException 找不到对应的dbId会抛出异常
     * @author liaoxiting
     * @date 2021/4/22 14:21
     */
    DataSourceDto getDataSourceInfoById(Long dbId);

    /**
     * 新增数据库信息
     *
     * @param databaseInfoRequest 新增参数
     * @author liaoxiting
     * @date 2020/11/1 21:47
     */
    void add(DatabaseInfoRequest databaseInfoRequest);

    /**
     * 通过数据源编码删除数据源
     *
     * @param datasourceCode 数据源编码
     * @author liaoxiting
     * @date 2021/5/27 10:06
     */
    void deleteByDatasourceCode(String datasourceCode);

    /**
     * 获取所有数据源连接信息
     *
     * @author liaoxiting
     * @date 2022/8/23 14:03
     */
    List<DataBaseInfoDto> getDatasourceList();

}
