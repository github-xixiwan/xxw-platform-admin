package com.xxw.platform.plugin.system.business.home.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 常用功能的统计次数实例类
 *
 * @author liaoxiting
 * @date 2022/02/10 21:17
 */
@TableName("sys_statistics_count")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysStatisticsCount extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "stat_count_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long statCountId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 访问的地址
     */
    @TableField("stat_url_id")
    @ChineseDescription("访问地址")
    private Long statUrlId;

    /**
     * 访问的次数
     */
    @TableField("stat_count")
    @ChineseDescription("访问次数")
    private Integer statCount;

}
