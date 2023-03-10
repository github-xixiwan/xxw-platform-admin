package com.xxw.platform.plugin.timer.business.migration.pojo;

import com.xxw.platform.plugin.timer.business.migration.pojo.v1.SysTimersMigration;
import lombok.Data;

import java.util.List;

/**
 * 定时任务迁移信息
 *
 * @author liaoxiting
 * @date 2021/7/8 9:13
 */
@Data
public class TimerMigrationInfo {

    /**
     * 定时任务列表
     */
    private List<SysTimersMigration> sysTimersMigrationList;

}
