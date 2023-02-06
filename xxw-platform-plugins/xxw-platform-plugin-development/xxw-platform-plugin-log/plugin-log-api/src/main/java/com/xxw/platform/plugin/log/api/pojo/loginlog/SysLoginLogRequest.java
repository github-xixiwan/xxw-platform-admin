package com.xxw.platform.plugin.log.api.pojo.loginlog;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 登录日志表
 *
 * @author liaoxiting
 * @date 2021/1/13 11:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLoginLogRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "llgId不能为空", groups = {detail.class})
    @ChineseDescription("主鍵id")
    private Long llgId;

    /**
     * 日志名称
     */
    @ChineseDescription("日志名称")
    private String llgName;

    /**
     * 是否执行成功
     */
    @ChineseDescription("是否执行成功")
    private String llgSucceed;

    /**
     * 具体消息
     */
    @ChineseDescription("具体消息")
    private String llgMessage;

    /**
     * 登录ip
     */
    @ChineseDescription("登录ip")
    private String llgIpAddress;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 创建时间
     */
    @ChineseDescription("创建时间")
    private Date createTime;

    /**
     * 开始时间
     */
    @ChineseDescription("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ChineseDescription("结束时间")
    private String endTime;

}
