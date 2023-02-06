package com.xxw.platform.plugin.system.business.notice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.annotation.SimpleFieldFormat;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import com.xxw.platform.plugin.dict.api.serializer.DictValueSerializer;
import com.xxw.platform.plugin.system.api.format.UserFormatProcess;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 通知表
 *
 * @author liaoxiting
 * @date 2021/1/8 22:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
public class SysNotice extends BaseEntity {

    /**
     * 通知id
     */
    @TableId("notice_id")
    @ChineseDescription("通知id")
    private Long noticeId;

    /**
     * 通知标题
     */
    @TableField("notice_title")
    @ChineseDescription("通知标题")
    private String noticeTitle;

    /**
     * 通知摘要
     */
    @TableField("notice_summary")
    @ChineseDescription("通知摘要")
    private String noticeSummary;

    /**
     * 通知优先级
     */
    @TableField(value = "priority_level")
    @ChineseDescription("通知优先级")
    private String priorityLevel;

    /**
     * 通知开始时间
     */
    @TableField(value = "notice_begin_time")
    @ChineseDescription("通知开始时间")
    private Date noticeBeginTime;

    /**
     * 通知结束时间
     */
    @TableField(value = "notice_end_time")
    @ChineseDescription("通知结束时间")
    private Date noticeEndTime;

    /**
     * 通知内容
     */
    @TableField("notice_content")
    @ChineseDescription("通知内容")
    private String noticeContent;

    /**
     * 通知范围
     */
    @TableField("notice_scope")
    @ChineseDescription("通知范围")
    private String noticeScope;

    /**
     * 是否删除：Y-已删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除：Y-已删除，N-未删除")
    private String delFlag;

    /**
     * 通知优先级的名称
     */
    @JsonSerialize(using = DictValueSerializer.class)
    public String getPriorityLevelValue() {
        return "priority_level|" + this.priorityLevel;
    }

    @Override
    @SimpleFieldFormat(processClass = UserFormatProcess.class)
    public Long getCreateUser() {
        return super.getCreateUser();
    }

}
