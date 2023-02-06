package com.xxw.platform.plugin.log.api.pojo.record;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.annotation.SimpleFieldFormat;
import com.xxw.platform.plugin.system.api.format.UserFormatProcess;
import lombok.Data;

import java.util.Date;

/**
 * 日志记录需要的参数
 *
 * @author liaoxiting
 * @date 2020/10/27 16:31
 */
@Data
public class LogRecordDTO {

    /**
     * 日志id
     */
    @ChineseDescription("日志id")
    private Long logId;

    /**
     * 日志的名称，一般为业务名称
     */
    @ChineseDescription("日志名称")
    private String logName;

    /**
     * 日志记录的内容
     */
    @ChineseDescription("日志记录内容")
    private Object logContent;

    /**
     * 服务名称，一般为spring.application.name
     */
    @ChineseDescription("服务名称")
    private String appName;

    /**
     * http或方法的请求参数体
     */
    @ChineseDescription("http或方法的请求参数体")
    private String requestParams;

    /**
     * http或方法的请求结果
     */
    @ChineseDescription("http或方法的请求结果")
    private String requestResult;

    /**
     * 操作发生的时间
     */
    @ChineseDescription("操作发生的时间")
    private Date dateTime;

    /**
     * 当前服务器的ip
     */
    @ChineseDescription("当前服务器的ip")
    private String serverIp;

    /**
     * 客户端请求的token
     * <p>
     * 如果是http请求，并且用户已经登录，可以带这项
     */
    @ChineseDescription("客户端请求的token")
    private String token;

    /**
     * 客户端请求的用户id
     * <p>
     * 如果是http请求，并且用户已经登录，可以带这项
     */
    @ChineseDescription("客户端请求的用户id")
    @SimpleFieldFormat(processClass = UserFormatProcess.class)
    private Long userId;

    /**
     * 客户端的ip
     * <p>
     * 如果是http请求，可以带这项
     */
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 当前用户请求的requestUrl
     * <p>
     * 如果是http请求，可以带这项
     */
    @ChineseDescription("当前用户请求的requestUrl")
    private String requestUrl;

    /**
     * 请求方式（GET POST PUT DELETE)
     * <p>
     * 如果是http请求，可以带这项
     */
    @ChineseDescription("请求方式")
    private String httpMethod;

    /**
     * 浏览器
     * <p>
     * 如果是http请求，可以带这项
     */
    @ChineseDescription("浏览器")
    private String clientBrowser;

    /**
     * 操作系统
     * <p>
     * 如果是http请求，可以带这项
     */
    @ChineseDescription("操作系统")
    private String clientOs;

    /**
     * 创建时间
     */
    @ChineseDescription("创建时间")
    private Date createTime;

}
