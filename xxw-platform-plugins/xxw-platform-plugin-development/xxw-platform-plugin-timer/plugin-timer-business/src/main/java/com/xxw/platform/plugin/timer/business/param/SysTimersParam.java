package com.xxw.platform.plugin.timer.business.param;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 定时任务
 *
 * @author liaoxiting
 * @date 2020/6/30 18:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTimersParam extends BaseRequest {

    /**
     * 定时器id
     */
    @NotNull(message = "主键timerId不能为空", groups = {edit.class, detail.class, delete.class, startTimer.class, stopTimer.class})
    @ChineseDescription("定时器id")
    private Long timerId;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("任务名称")
    private String timerName;

    /**
     * 执行任务的class的类名（实现了TimerTaskRunner接口的类的全称）
     */
    @NotBlank(message = "任务的class的类名不能为空", groups = {add.class, edit.class})
    @ChineseDescription("执行任务的class的类名")
    private String actionClass;

    /**
     * 定时任务表达式
     */
    @NotBlank(message = "定时任务表达式不能为空", groups = {add.class, edit.class})
    @ChineseDescription("定时任务表达式")
    private String cron;

    /**
     * 状态（字典 1运行  2停止）
     */
    @NotNull(message = "任务状态不能为空", groups = {edit.class})
    @ChineseDescription("状态（字典 1运行  2停止）")
    private Integer jobStatus;

    /**
     * 参数
     */
    @ChineseDescription("参数")
    private String params;

    /**
     * 备注信息
     */
    @ChineseDescription("备注信息")
    private String remark;

    /**
     * 是否删除标记
     */
    @ChineseDescription("是否删除标记")
    private String delFlag;

    /**
     * 启用定时任务
     */
    public @interface startTimer {
    }

    /**
     * 停止定时任务
     */
    public @interface stopTimer {
    }

}
