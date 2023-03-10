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
 * ?????????????????? ???????????????
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

        // ?????????????????????????????????
        validateConnection(databaseInfoRequest);

        // ????????????????????????
        DatabaseInfo entity = new DatabaseInfo();
        BeanUtil.copyProperties(databaseInfoRequest, entity);

        // ???????????????????????????
        entity.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(entity);

        // ???????????????????????????????????????
        addDataSourceToContext(entity, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDatasourceCode(String datasourceCode) {

        LambdaQueryWrapper<DatabaseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DatabaseInfo::getDbName, datasourceCode);
        DatabaseInfo databaseInfo = this.getOne(wrapper, false);

        // ?????????????????????
        this.removeById(databaseInfo.getDbId());

        // ?????????????????????????????????
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

        // ????????????????????????????????????
        if (databaseInfo.getDbName().startsWith(RuleConstants.TENANT_DB_PREFIX)) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.TENANT_DATASOURCE_CANT_DELETE);
        }

        // ????????????????????????
        if (DatasourceContainerConstants.MASTER_DATASOURCE_NAME.equals(databaseInfo.getDbName())) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.MASTER_DATASOURCE_CANT_DELETE);
        }

        // ??????????????????????????????
        LambdaUpdateWrapper<DatabaseInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DatabaseInfo::getDelFlag, YesOrNotEnum.Y.getCode());
        updateWrapper.eq(DatabaseInfo::getDbId, databaseInfoRequest.getDbId());
        this.update(updateWrapper);

        // ?????????????????????????????????
        DataSourceContext.removeDataSource(databaseInfo.getDbName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DatabaseInfoRequest databaseInfoRequest) {

        DatabaseInfo databaseInfo = this.queryDatabaseInfoById(databaseInfoRequest);

        // ??????????????????????????????
        if (!databaseInfoRequest.getDbName().equals(databaseInfo.getDbName())) {
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.EDIT_DATASOURCE_NAME_ERROR, databaseInfo.getDbName());
        }

        // ?????????????????????????????????
        validateConnection(databaseInfoRequest);

        // ?????????????????????
        BeanUtil.copyProperties(databaseInfoRequest, databaseInfo);
        this.updateById(databaseInfo);

        // ???????????????????????????????????????
        addDataSourceToContext(databaseInfo, true);
    }

    @Override
    public PageResult<DatabaseInfo> findPage(DatabaseInfoRequest databaseInfoRequest) {
        LambdaQueryWrapper<DatabaseInfo> queryWrapper = createWrapper(databaseInfoRequest);

        // ??????????????????
        Page<DatabaseInfo> result = this.page(PageFactory.defaultPage(), queryWrapper);

        // ?????????????????????????????????????????????????????????????????????????????????
        SysGroupRequest sysGroupRequest = new SysGroupRequest();
        sysGroupRequest.setGroupBizCode(DatasourceContainerConstants.DATASOURCE_GROUP_CODE);
        List<SysGroupDTO> list = groupApi.findGroupList(sysGroupRequest, true);

        // ????????????
        List<DatabaseInfo> records = result.getRecords();
        for (DatabaseInfo record : records) {
            record.setPassword("***");

            // ??????????????????
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

        // ????????????
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
     * ???????????????????????????????????????
     *
     * @param databaseInfo ?????????????????????
     * @author liaoxiting
     * @date 2020/12/19 16:16
     */
    private void addDataSourceToContext(DatabaseInfo databaseInfo, Boolean removeOldDatasource) {

        // ?????????????????????????????????
        if (removeOldDatasource) {
            DataSourceContext.removeDataSource(databaseInfo.getDbName());
        } else {
            // ?????????context??????????????????????????????
            DataSource dataSource = DataSourceContext.getDataSources().get(databaseInfo.getDbName());
            if (dataSource != null) {
                String userTip = StrUtil.format(DatasourceContainerExceptionEnum.DATASOURCE_NAME_REPEAT.getUserTip(), databaseInfo.getDbName());
                throw new DatasourceContainerException(DatasourceContainerExceptionEnum.DATASOURCE_NAME_REPEAT, userTip);
            }
        }

        // ???????????????????????????????????????
        DruidProperties druidProperties = DruidPropertiesFactory.createDruidProperties(databaseInfo);
        DruidDataSource druidDataSource = DruidDatasourceFactory.createDruidDataSource(druidProperties);
        DataSourceContext.addDataSource(databaseInfo.getDbName(), druidDataSource, druidProperties);

        // ??????????????????
        try {
            druidDataSource.init();
        } catch (SQLException exception) {
            log.error("???????????????????????????", exception);
            String userTip = StrUtil.format(DatasourceContainerExceptionEnum.INIT_DATASOURCE_ERROR.getUserTip(), exception.getMessage());
            throw new DatasourceContainerException(DatasourceContainerExceptionEnum.INIT_DATASOURCE_ERROR, userTip);
        }
    }

    /**
     * ???????????????????????????id
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
     * ??????wrapper
     *
     * @author liaoxiting
     * @date 2021/1/8 14:16
     */
    private LambdaQueryWrapper<DatabaseInfo> createWrapper(DatabaseInfoRequest databaseInfoRequest) {
        LambdaQueryWrapper<DatabaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        // ?????????????????????
        queryWrapper.eq(DatabaseInfo::getDelFlag, YesOrNotEnum.N.getCode());

        if (ObjectUtil.isEmpty(databaseInfoRequest)) {
            return queryWrapper;
        }

        // ????????????????????????
        String dbName = databaseInfoRequest.getDbName();

        // ??????sql ??????
        queryWrapper.like(ObjectUtil.isNotEmpty(dbName), DatabaseInfo::getDbName, dbName);

        // ??????????????????
        queryWrapper.eq(ObjectUtil.isNotEmpty(databaseInfoRequest.getStatusFlag()), DatabaseInfo::getStatusFlag, databaseInfoRequest.getStatusFlag());

        // ?????????????????????????????????
        String conditionGroupName = databaseInfoRequest.getConditionGroupName();
        List<Long> userBizIds;
        if (ObjectUtil.isNotEmpty(conditionGroupName) && !conditionGroupName.equals(GroupConstants.ALL_GROUP_NAME)) {

            // ????????????????????????????????????????????????????????????????????????id?????????not in??????
            SysGroupRequest sysGroupRequest = new SysGroupRequest();
            sysGroupRequest.setGroupBizCode(DatasourceContainerConstants.DATASOURCE_GROUP_CODE);

            if (conditionGroupName.equals(GroupConstants.GROUP_DELETE_NAME)) {

                // ?????????????????????????????????id??????
                userBizIds = groupApi.findUserGroupDataList(sysGroupRequest);
                queryWrapper.nested(ObjectUtil.isNotEmpty(userBizIds), i -> i.notIn(DatabaseInfo::getDbId, userBizIds));
            } else {

                // ?????????????????????????????????????????????id
                sysGroupRequest.setGroupName(conditionGroupName);
                userBizIds = groupApi.findUserGroupDataList(sysGroupRequest);
                queryWrapper.nested(ObjectUtil.isNotEmpty(userBizIds), i -> i.in(DatabaseInfo::getDbId, userBizIds));
            }
        }
        return queryWrapper;
    }
}
