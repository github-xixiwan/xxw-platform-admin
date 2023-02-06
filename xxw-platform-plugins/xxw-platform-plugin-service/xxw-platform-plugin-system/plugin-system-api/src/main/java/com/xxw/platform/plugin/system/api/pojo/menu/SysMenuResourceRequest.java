package com.xxw.platform.plugin.system.api.pojo.menu;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单资源的请求
 *
 * @author liaoxiting
 * @date 2021/8/8 22:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuResourceRequest extends BaseRequest {

    /**
     * 业务id不能为空
     */
    @NotNull(message = "业务id不能为空", groups = {SysMenuResourceRequest.list.class, SysMenuResourceRequest.add.class})
    @ChineseDescription("业务id")
    private Long businessId;

    /**
     * 绑定资源的类型，1：菜单，2：菜单下按钮
     */
    @NotNull(message = "绑定的资源类型不能为空", groups = {SysMenuResourceRequest.add.class})
    @ChineseDescription("绑定资源的类型")
    private Integer businessType;

    /**
     * 模块下所有的资源
     */
    @ChineseDescription("模块下所有的资源")
    private List<String> modularTotalResource;

    /**
     * 模块下选中的资源
     */
    @ChineseDescription("模块下选中的资源")
    private List<String> selectedResource;

}
