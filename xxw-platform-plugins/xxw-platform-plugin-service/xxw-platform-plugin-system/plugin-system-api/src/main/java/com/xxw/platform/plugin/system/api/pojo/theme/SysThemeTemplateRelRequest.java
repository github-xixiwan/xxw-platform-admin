package com.xxw.platform.plugin.system.api.pojo.theme;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 系统主题模板属性关系参数
 *
 * @author liaoxiting
 * @date 2021/12/24 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysThemeTemplateRelRequest extends BaseRequest {

    /**
     * 主键ID
     */
    @ChineseDescription("主键ID")
    private Long relationId;

    /**
     * 模板ID
     */
    @NotNull(message = "模板ID不能为空", groups = {add.class})
    @ChineseDescription("模板ID")
    private Long templateId;

    /**
     * 属性编码集合
     */
    @NotEmpty(message = "属性编码集合不能为空", groups = {add.class, delete.class})
    @ChineseDescription("属性编码集合")
    private String[] fieldCodes;
}
