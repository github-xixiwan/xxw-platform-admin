package com.xxw.platform.plugin.group.api.pojo;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * 业务分组实例类
 *
 * @author liaoxiting
 * @date 2022/05/11 12:54
 */
@Data
public class SysGroupDTO {

    /**
     * 分组id
     */
    @ChineseDescription("分组id")
    private Long groupId;

    /**
     * 所属业务编码
     */
    @ChineseDescription("所属业务编码")
    private String groupBizCode;

    /**
     * 分组名称
     */
    @ChineseDescription("分组名称")
    private String groupName;

    /**
     * 业务主键id
     */
    @ChineseDescription("业务主键id")
    private Long businessId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

}
