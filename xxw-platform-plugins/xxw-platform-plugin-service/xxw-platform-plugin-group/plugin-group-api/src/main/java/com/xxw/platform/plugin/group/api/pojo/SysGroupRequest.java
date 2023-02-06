package com.xxw.platform.plugin.group.api.pojo;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 业务分组封装类
 *
 * @author liaoxiting
 * @date 2022/05/11 12:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysGroupRequest extends BaseRequest {

    /**
     * 分组id
     */
    @ChineseDescription("分组id")
    private Long groupId;

    /**
     * 所属业务编码
     */
    @ChineseDescription("所属业务编码")
    @NotBlank(message = "groupBizCode业务编码不能为空", groups = {add.class, list.class, delete.class})
    private String groupBizCode;

    /**
     * 分组名称不能为空
     */
    @ChineseDescription("分组名称")
    @NotBlank(message = "分组名称不能为空", groups = {add.class})
    private String groupName;

    /**
     * 业务主键id集合
     */
    @ChineseDescription("业务主键id集合")
    @NotNull(message = "业务主键id集合不能为空", groups = {add.class, delete.class})
    private List<Long> businessIdList;

}
