package com.xxw.platform.plugin.system.business.home.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 常用功能的统计次数封装类
 *
 * @author fengshuonan
 * @date 2022/02/10 21:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysStatisticsCountRequest extends BaseRequest {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键ID")
    private Long statCountId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 访问的地址
     */
    @ChineseDescription("访问的地址")
    private Long statUrlId;

    /**
     * 访问的次数
     */
    @ChineseDescription("访问的次数")
    private Integer statCount;

}
