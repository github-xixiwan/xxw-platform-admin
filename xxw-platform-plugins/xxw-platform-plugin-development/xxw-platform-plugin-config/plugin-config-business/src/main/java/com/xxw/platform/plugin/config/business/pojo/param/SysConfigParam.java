package com.xxw.platform.plugin.config.business.pojo.param;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.plugin.validator.api.validators.flag.FlagValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统参数配置参数
 *
 * @author liaoxiting
 * @date 2020/4/14 10:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysConfigParam extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "configId不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键")
    private Long configId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("名称")
    private String configName;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("编码")
    private String configCode;

    /**
     * 配置值
     */
    @NotBlank(message = "配置值不能为空", groups = {add.class, edit.class})
    @ChineseDescription("配置值")
    private String configValue;

    /**
     * 是否是系统参数：Y-是，N-否
     */
    @NotBlank(message = "是否是系统参数不能为空", groups = {add.class, edit.class})
    @FlagValue(message = "是否是系统参数格式错误，正确格式应该Y或者N", groups = {add.class, edit.class})
    @ChineseDescription("是否系统参数")
    private String sysFlag;

    /**
     * 备注
     */
    @ChineseDescription("备注")
    private String remark;

    /**
     * 状态：1-正常，2停用
     */
    @ChineseDescription("状态")
    private Integer statusFlag;

    /**
     * 常量所属分类的编码，来自于“常量的分类”字典
     */
    @NotBlank(message = "量所属分类的编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("常量所属分类的编码")
    private String groupCode;

}
