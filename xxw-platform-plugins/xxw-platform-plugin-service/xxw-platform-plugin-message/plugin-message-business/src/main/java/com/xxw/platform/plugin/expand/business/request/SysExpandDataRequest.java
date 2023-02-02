package com.xxw.platform.plugin.expand.business.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 业务拓展-具体数据封装类
 *
 * @author fengshuonan
 * @date 2022/03/29 23:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysExpandDataRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键id")
    private Long expandDataId;

    /**
     * 拓展业务id
     */
    @ChineseDescription("拓展业务id")
    private Long expandId;

    /**
     * 业务主键id
     */
    @ChineseDescription("业务主键id")
    private Long primaryFieldValue;

    /**
     * 拓展业务具体数据
     */
    @ChineseDescription("拓展业务具体数据")
    private String expandData;

}
