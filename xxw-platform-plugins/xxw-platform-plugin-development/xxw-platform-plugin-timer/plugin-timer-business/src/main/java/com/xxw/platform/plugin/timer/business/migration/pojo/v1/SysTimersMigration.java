package com.xxw.platform.plugin.timer.business.migration.pojo.v1;

import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务
 *
 * @author liaoxiting
 * @date 2020/6/30 18:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTimersMigration extends BaseEntity {

    /**
     * 定时器id
     */
    private Long timerId;

    /**
     * 任务名称
     */
    private String timerName;

    /**
     * 执行任务的class的类名（实现了TimerAction接口的类的全称）
     */
    private String actionClass;

    /**
     * 定时任务表达式
     */
    private String cron;

    /**
     * 参数
     */
    private String params;

    /**
     * 状态：1-运行，2-停止
     */
    private Integer jobStatus;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    private String delFlag;

}
