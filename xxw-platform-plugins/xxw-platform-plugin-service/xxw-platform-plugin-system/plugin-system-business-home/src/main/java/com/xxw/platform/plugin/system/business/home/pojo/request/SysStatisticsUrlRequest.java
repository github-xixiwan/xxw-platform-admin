package com.xxw.platform.plugin.system.business.home.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 常用功能列表封装类
 *
 * @author liaoxiting
 * @date 2022/02/10 21:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysStatisticsUrlRequest extends BaseRequest {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键ID")
    private Long statUrlId;

    /**
     * 被统计名称
     */
    @ChineseDescription("被统计名称")
    private String statName;

    /**
     * 被统计菜单ID
     */
    @ChineseDescription("被统计菜单ID")
    private Long statMenuId;

    /**
     * 被统计的URL
     */
    @ChineseDescription("被统计的URL")
    private String statUrl;

    /**
     * 是否常驻显示，Y-是，N-否
     */
    @ChineseDescription("是否常驻显示，Y-是，N-否")
    private String alwaysShow;

}
