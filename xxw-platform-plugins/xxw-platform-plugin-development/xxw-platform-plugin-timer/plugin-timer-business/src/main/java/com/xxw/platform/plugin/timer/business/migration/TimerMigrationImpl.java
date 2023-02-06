package com.xxw.platform.plugin.timer.business.migration;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxw.platform.plugin.migration.api.AccessMigrationApi;
import com.xxw.platform.plugin.migration.api.enums.MigrationAggregationTypeEnum;
import com.xxw.platform.plugin.migration.api.pojo.MigrationInfo;
import com.xxw.platform.plugin.timer.api.enums.TimerJobStatusEnum;
import com.xxw.platform.plugin.timer.business.entity.SysTimers;
import com.xxw.platform.plugin.timer.business.migration.pojo.TimerMigrationInfo;
import com.xxw.platform.plugin.timer.business.migration.pojo.v1.SysTimersMigration;
import com.xxw.platform.plugin.timer.business.param.SysTimersParam;
import com.xxw.platform.plugin.timer.business.service.SysTimersService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 定时任务迁移接入实现
 *
 * @author liaoxiting
 * @date 2021/7/8 9:24
 */
@Component
public class TimerMigrationImpl implements AccessMigrationApi {

    @Resource
    private SysTimersService sysTimersService;

    @Override
    public String getAppName() {
        return "系统应用";
    }

    @Override
    public String getModuleName() {
        return "定时任务";
    }

    @Override
    public MigrationInfo exportData() {
        MigrationInfo migrationInfo = new MigrationInfo();
        migrationInfo.setVersion("v1");

        // 聚合对象
        TimerMigrationInfo timerMigrationInfo = new TimerMigrationInfo();
        migrationInfo.setData(timerMigrationInfo);

        // 填充数据
        List<SysTimers> sysTimers = sysTimersService.list();
        List<SysTimersMigration> sysTimersMigrations;
        if (ObjectUtil.isNotEmpty(sysTimers)) {
            sysTimersMigrations = sysTimers.stream().map(item -> BeanUtil.toBean(item, SysTimersMigration.class)).collect(Collectors.toList());
        } else {
            sysTimersMigrations = new ArrayList<>();
        }
        timerMigrationInfo.setSysTimersMigrationList(sysTimersMigrations);

        return migrationInfo;
    }

    @Override
    public boolean importData(String type, MigrationInfo data) {
        TimerMigrationInfo timerMigrationInfo = JSONObject.parseObject(String.valueOf(data.getData()), TimerMigrationInfo.class);

        if (MigrationAggregationTypeEnum.MIGRATION_INCREMENTAL.getCode().equals(type)) {
            // 查询配置信息
            List<SysTimersMigration> sysTimersMigrationList = timerMigrationInfo.getSysTimersMigrationList();
            if (ObjectUtil.isNotEmpty(sysTimersMigrationList)) {
                Set<Long> ids = sysTimersMigrationList.stream().map(SysTimersMigration::getTimerId).collect(Collectors.toSet());

                // 组装查询条件
                LambdaQueryWrapper<SysTimers> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.select(SysTimers::getTimerId);
                lambdaQueryWrapper.in(SysTimers::getTimerId, ids);
                List<SysTimers> dbList = sysTimersService.list(lambdaQueryWrapper);

                // 移除已存在项
                for (SysTimers db : dbList) {
                    sysTimersMigrationList.removeIf(item -> item.getTimerId().equals(db.getTimerId()));
                }
            }
        }

        // 入库
        List<SysTimersMigration> sysTimersMigrationList = timerMigrationInfo.getSysTimersMigrationList();
        if (ObjectUtil.isNotEmpty(sysTimersMigrationList)) {
            List<SysTimers> sysTimers = sysTimersMigrationList.stream().map(item -> BeanUtil.toBean(item, SysTimers.class)).collect(Collectors.toList());
            sysTimersService.saveOrUpdateBatch(sysTimers);

            // 启动任务
            for (SysTimers sysTimer : sysTimers) {
                SysTimersParam sysTimersParam = BeanUtil.toBean(sysTimer, SysTimersParam.class);
                if (TimerJobStatusEnum.RUNNING.getCode().equals(sysTimer.getJobStatus())) {
                    sysTimersService.start(sysTimersParam);
                } else {
                    sysTimersService.stop(sysTimersParam);
                }
            }
        }
        return true;
    }
}
