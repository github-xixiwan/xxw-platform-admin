package com.xxw.platform.plugin.log.api.pojo.manage;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 日志管理的查询参数
 *
 * @author liaoxiting
 * @date 2020/10/28 11:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogManagerRequest extends BaseRequest {

    /**
     * 单条日志id
     */
    @NotNull(message = "日志id不能为空", groups = {detail.class})
    @ChineseDescription("单条日志id")
    private Long logId;

    /**
     * 查询的起始时间
     */
    @NotBlank(message = "起始时间不能为空", groups = {delete.class})
    @ChineseDescription("查询的起始时间")
    private String beginDate;

    /**
     * 查询日志的结束时间
     */
    @ChineseDescription("查询的结束时间")
    @NotBlank(message = "结束时间不能为空", groups = {delete.class})
    private String endDate;

    /**
     * 日志的名称，一般为业务名称
     */
    @ChineseDescription("日志名称")
    private String logName;

    /**
     * 服务名称，一般为spring.application.name
     */
    @NotBlank(message = "服务名称不能为空", groups = {delete.class})
    @ChineseDescription("服务名称")
    private String appName;

    /**
     * 当前服务器的ip
     */
    @ChineseDescription("当前服务器ip")
    private String serverIp;

    /**
     * 客户端请求的用户id
     */
    @ChineseDescription("客户端请求的用户id")
    private Long userId;

    /**
     * 客户端的ip
     */
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 当前用户请求的url
     */
    @ChineseDescription("当前用户的请求url")
    private String requestUrl;

}
