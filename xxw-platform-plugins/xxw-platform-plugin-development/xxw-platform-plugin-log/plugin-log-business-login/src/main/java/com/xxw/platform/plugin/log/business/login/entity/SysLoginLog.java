package com.xxw.platform.plugin.log.business.login.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志表
 *
 * @author liaoxiting
 * @date 2021/1/13 11:05
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog {

    /**
     * 主键id
     */
    @TableId("llg_id")
    @ChineseDescription("主键id")
    private Long llgId;

    /**
     * 日志名称
     */
    @TableField("llg_name")
    @ChineseDescription("日志名称")
    private String llgName;

    /**
     * 是否执行成功
     */
    @TableField("llg_succeed")
    @ChineseDescription("是否执行成功")
    private String llgSucceed;

    /**
     * 具体消息
     */
    @TableField("llg_message")
    @ChineseDescription("具体消息")
    private String llgMessage;

    /**
     * 登录ip
     */
    @TableField("llg_ip_address")
    @ChineseDescription("登录ip")
    private String llgIpAddress;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ChineseDescription("创建时间")
    private Date createTime;

}
