package com.xxw.platform.plugin.timer.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.annotation.SimpleFieldFormat;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import com.xxw.platform.plugin.system.api.format.UserFormatProcess;
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
@TableName("sys_timers")
public class SysTimers extends BaseEntity {

    /**
     * 定时器id
     */
    @TableId(value = "timer_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("定时器id")
    private Long timerId;

    /**
     * 任务名称
     */
    @TableField("timer_name")
    @ChineseDescription("任务名称")
    private String timerName;

    /**
     * 执行任务的class的类名（实现了TimerAction接口的类的全称）
     */
    @TableField("action_class")
    @ChineseDescription("执行任务的class的类名")
    private String actionClass;

    /**
     * 定时任务表达式
     */
    @TableField("cron")
    @ChineseDescription("定时任务表达式")
    private String cron;

    /**
     * 参数
     */
    @TableField("params")
    @ChineseDescription("参数")
    private String params;

    /**
     * 状态：1-运行，2-停止
     */
    @TableField("job_status")
    @ChineseDescription("状态：1-运行，2-停止")
    private Integer jobStatus;

    /**
     * 备注信息
     */
    @TableField("remark")
    @ChineseDescription("备注信息")
    private String remark;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除")
    private String delFlag;

    @Override
    @SimpleFieldFormat(processClass = UserFormatProcess.class)
    public Long getCreateUser() {
        return super.getCreateUser();
    }

}
