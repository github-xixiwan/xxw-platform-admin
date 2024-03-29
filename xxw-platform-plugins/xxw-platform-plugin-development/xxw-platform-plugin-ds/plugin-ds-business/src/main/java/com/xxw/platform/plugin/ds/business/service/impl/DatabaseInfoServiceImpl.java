package com.xxw.platform.plugin.ds.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.enums.DbTypeEnum;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.db.api.factory.DruidDatasourceFactory;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.ds.api.constants.DatasourceContainerConstants;
import com.xxw.platform.plugin.ds.api.exception.DatasourceContainerException;
import com.xxw.platform.plugin.ds.api.exception.enums.DatasourceContainerExceptionEnum;
import com.xxw.platform.plugin.ds.api.pojo.DataBaseInfoDto;
import com.xxw.platform.plugin.ds.api.pojo.DataSourceDto;
import com.xxw.platform.plugin.ds.api.pojo.request.DatabaseInfoRequest;
import com.xxw.platform.plugin.ds.business.entity.DatabaseInfo;
import com.xxw.platform.plugin.ds.business.factory.DruidPropertiesFactory;
import com.xxw.platform.plugin.ds.business.mapper.DatabaseInfoMapper;
import com.xxw.platform.plugin.ds.business.service.DatabaseInfoService;
import com.xxw.platform.plugin.ds.sdk.context.DataSourceContext;
import com.xxw.platform.plugin.group.api.GroupApi;
import com.xxw.platform.plugin.group.api.constants.GroupConstants;
import com.xxw.platform.plugin.group.api.pojo.SysGroupDTO;
import com.xxw.platform.plugin.group.api.pojo.SysGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库信息表 服务实现类
 *
 * @author liaoxiting
 * @date 2020/11/1 21:45
 */
@Service
public class DatabaseInfoServiceImpl extends ServiceImpl<DatabaseInfoMapper, DatabaseInfo> implements DatabaseInfoService {

    @Resource
    private GroupApi groupApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DatabaseInfoRequest databaseInfoRequest) {

        // 判断数据库连接是否可用
        validateConnection(databaseInfoRequest);

        // 数据库中插入记录
        DatabaseInfo entity = new DatabaseInfo();
        BeanUtil.copyProperties(databaseInfoRequest, entity);

        // 设置状态为启用状态
        entity.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(entity);

        // 往数据源容器文中添加数据源
        addDataSourceToContext(entity, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDatasourceCode(String datasourceCode) {

        LambdaQueryWrapper<DatabaseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DatabaseInfo::getDbName, datasourceCode);
        DatabaseInfo databaseInfo = this.getOne(wrapper, false);

        // 删除数据源信息
        this.removeById(databaseInfo.getDbId());

        // 删除容器中的数据源记录
        DataSourceContext.removeDataSource(datasourceCode);
    }

    @Override
    public List<DataBaseInfoDto> getDatasourceList() {
        LambdaQueryWrapper<DatabaseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DatabaseInfo::getDbId, DatabaseInfo::getDbName, DatabaseInfo::getRemarks, DatabaseInfo::getJdbcUrl);
        List<DatabaseInfo> list = this.list(wrapper);

        return list.stream().map(item -> {
            DataBaseInfoDto dataBaseInfoDto = new DataBaseInfoDto();
            BeanUtil.copyProperties(item, dataBaseInfoDto);
            String type = DbTypeEnum.getTypeByUrl(item.getJdbcUrl());
            dataBaseInfoDto.setDbType(type);
            return dataBaseInfoDto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(DatabaseInfoRequest databaseInfoRequest) {

        DatabaseInfo databaseInfo = this.queryDatabaseInfoById(databaseInfoRequest);

        // 如果是租户数据库不能删除
        if (databaseInfo.getDbName().startsWith(RuleConstants.TENANT_DB_PREFIX)) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.TENANT_DATASOURCE_CANT_DELETE);
        }

        // 不能删除主数据源
        if (DatasourceContainerConstants.MASTER_DATASOURCE_NAME.equals(databaseInfo.getDbName())) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.MASTER_DATASOURCE_CANT_DELETE);
        }

        // 删除库中的数据源记录
        LambdaUpdateWrapper<DatabaseInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DatabaseInfo::getDelFlag, YesOrNotEnum.Y.getCode());
        updateWrapper.eq(DatabaseInfo::getDbId, databaseInfoRequest.getDbId());
        this.update(updateWrapper);

        // 删除容器中的数据源记录
        DataSourceContext.removeDataSource(databaseInfo.getDbName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DatabaseInfoRequest databaseInfoRequest) {

        DatabaseInfo databaseInfo = this.queryDatabaseInfoById(databaseInfoRequest);

        // 不能修改数据源的名称
        if (!databaseInfoRequest.getDbName().equals(databaseInfo.getDbName())) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.EDIT_DATASOURCE_NAME_ERROR, databaseInfo.getDbName());
        }

        // 判断数据库连接是否可用
        validateConnection(databaseInfoRequest);

        // 更新库中的记录
        BeanUtil.copyProperties(databaseInfoRequest, databaseInfo);
        this.updateById(databaseInfo);

        // 往数据源容器文中添加数据源
        addDataSourceToContext(databaseInfo, true);
    }

    @Override
    public PageResult<DatabaseInfo> findPage(DatabaseInfoRequest databaseInfoRequest) {
        LambdaQueryWrapper<DatabaseInfo> queryWrapper = createWrapper(databaseInfoRequest);

        // 查询分页结果
        Page<DatabaseInfo> result = this.page(PageFactory.defaultPage(), queryWrapper);

        // 查询结果中有没有用户挂标签的，有的话就返回中文分组名称
        SysGroupRequest sysGroupRequest = new SysGroupRequest();
        sysGroupRequest.setGroupBizCode(DatasourceContainerConstants.DATASOURCE_GROUP_CODE);
        List<SysGroupDTO> list = groupApi.findGroupList(sysGroupRequest, true);

        // 更新密码
        List<DatabaseInfo> records = result.getRecords();
        for (DatabaseInfo record : records) {
            record.setPassword("***");

            // 增加分组名称
            for (SysGroupDTO sysGroupDTO : list) {
                if (record.getDbId().equals(sysGroupDTO.getBusinessId())) {
                    record.setGroupName(sysGroupDTO.getGroupName());
                }
            }
        }

        return PageResultFactory.createPageResult(result);
    }
    @Override
    public List<DatabaseInfo> findList(DatabaseInfoRequest databaseInfoRequest) {
        LambdaQueryWrapper<DatabaseInfo> wrapper = createWrapper(databaseInfoRequest);
        List<DatabaseInfo> list = this.list(wrapper);

        // 更新密码
        for (DatabaseInfo record : list) {
            record.setPassword("***");
        }

        return list;
    }
    @Override
    public DatabaseInfo detail(DatabaseInfoRequest databaseInfoRequest) {
        DatabaseInfo databaseInfo = this.queryDatabaseInfoById(databaseInfoRequest);
        databaseInfo.setPassword("***");
        return databaseInfo;
    }

    @Override
    public void validateConnection(DatabaseInfoRequest param) {
        Connection conn = null;
        try {
            Class.forName(param.getJdbcDriver());
            conn = DriverManager.getConnection(param.getJdbcUrl(), param.getUsername(), param.getPassword());
        } catch (Exception e) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.VALIDATE_DATASOURCE_ERROR, param.getJdbcUrl(), e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public DataSourceDto getDataSourceInfoById(Long dbId) {
        DataSourceDto dataSourceDto = new DataSourceDto();

        DatabaseInfoRequest databaseInfoRequest = new DatabaseInfoRequest();
        databaseInfoRequest.setDbId(dbId);
        DatabaseInfo databaseInfo = this.queryDatabaseInfoById(databaseInfoRequest);
        BeanUtil.copyProperties(databaseInfo, dataSourceDto);
        return dataSourceDto;
    }

    /**
     * 往数据源容器文中添加数据源
     *
     * @param databaseInfo 数据库信息实体
     * @author liaoxiting
     * @date 2020/12/19 16:16
     */
    private void addDataSourceToContext(DatabaseInfo databaseInfo, Boolean removeOldDatasource) {

        // 删除容器中的数据源记录
        if (removeOldDatasource) {
            DataSourceContext.removeDataSource(databaseInfo.getDbName());
        } else {
            // 先判断context中是否有了这个数据源
            DataSource dataSource = DataSourceContext.getDataSources().get(databaseInfo.getDbName());
            if (dataSource != null) {
                String userTip = StrUtil.format(DatasourceContainerExceptionEnum.DATASOURCE_NAME_REPEAT.getUserTip(), databaseInfo.getDbName());
                throw new DatasourceContainerException(DatasourceContainerExceptionEnum.DATASOURCE_NAME_REPEAT, userTip);
            }
        }

        // 往数据源容器文中添加数据源
        DruidProperties druidProperties = DruidPropertiesFactory.createDruidProperties(databaseInfo);
        DruidDataSource druidDataSource = DruidDatasourceFactory.createDruidDataSource(druidProperties);
        DataSourceContext.addDataSource(databaseInfo.getDbName(), druidDataSource, druidProperties);

        // 初始化数据源
        try {
            druidDataSource.init();
        } catch (SQLException exception) {
            log.error("初始化数据源异常！", exception);
            String userTip = StrUtil.format(DatasourceContainerExceptionEnum.INIT_DATASOURCE_ERROR.getUserTip(), exception.getMessage());
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.INIT_DATASOURCE_ERROR, userTip);
        }
    }

    /**
     * 查询数据库信息通过id
     *
     * @author liaoxiting
     * @date 2021/2/8 9:53
     */
    private DatabaseInfo queryDatabaseInfoById(DatabaseInfoRequest databaseInfoRequest) {
        DatabaseInfo databaseInfo = this.getById(databaseInfoRequest.getDbId());
        if (databaseInfo == null) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.DATASOURCE_INFO_NOT_EXISTED, databaseInfoRequest.getDbId());
        }
        return databaseInfo;
    }

    /**
     * 创建wrapper
     *
     * @author liaoxiting
     * @date 2021/1/8 14:16
     */
    private LambdaQueryWrapper<DatabaseInfo> createWrapper(DatabaseInfoRequest databaseInfoRequest) {
        LambdaQueryWrapper<DatabaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        // 查询没被删除的
        queryWrapper.eq(DatabaseInfo::getDelFlag, YesOrNotEnum.N.getCode());

        if (ObjectUtil.isEmpty(databaseInfoRequest)) {
            return queryWrapper;
        }

        // 根据名称模糊查询
        String dbName = databaseInfoRequest.getDbName();

        // 拼接sql 条件
        queryWrapper.like(ObjectUtil.isNotEmpty(dbName), DatabaseInfo::getDbName, dbName);

        // 拼接状态条件
        queryWrapper.eq(ObjectUtil.isNotEmpty(databaseInfoRequest.getStatusFlag()), DatabaseInfo::getStatusFlag, databaseInfoRequest.getStatusFlag());

        // 拼接分组相关的查询条件
        String conditionGroupName = databaseInfoRequest.getConditionGroupName();
        List<Long> userBizIds;
        if (ObjectUtil.isNotEmpty(conditionGroupName) && !conditionGroupName.equals(GroupConstants.ALL_GROUP_NAME)) {

            // 如果是未分组，则查询当前用户已经分组的所有的业务id，然后not in即可
            SysGroupRequest sysGroupRequest = new SysGroupRequest();
            sysGroupRequest.setGroupBizCode(DatasourceContainerConstants.DATASOURCE_GROUP_CODE);

            if (conditionGroupName.equals(GroupConstants.GROUP_DELETE_NAME)) {

                // 本用户所有分过组的项目id集合
                userBizIds = groupApi.findUserGroupDataList(sysGroupRequest);
                queryWrapper.nested(ObjectUtil.isNotEmpty(userBizIds), i -> i.notIn(DatabaseInfo::getDbId, userBizIds));
            } else {

                // 查询本用户在当前分组所有的业务id
                sysGroupRequest.setGroupName(conditionGroupName);
                userBizIds = groupApi.findUserGroupDataList(sysGroupRequest);
                queryWrapper.nested(ObjectUtil.isNotEmpty(userBizIds), i -> i.in(DatabaseInfo::getDbId, userBizIds));
            }
        }
        return queryWrapper;
    }
}
