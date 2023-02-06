package com.xxw.platform.plugin.system.business.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户数据范围表
 *
 * @author liaoxiting
 * @date 2020/11/6 09:46
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_user_data_scope")
public class SysUserDataScope extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "user_data_scope_id", type = IdType.ASSIGN_ID)
    private Long userDataScopeId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 机构id
     */
    @TableField("org_id")
    private Long orgId;

}
