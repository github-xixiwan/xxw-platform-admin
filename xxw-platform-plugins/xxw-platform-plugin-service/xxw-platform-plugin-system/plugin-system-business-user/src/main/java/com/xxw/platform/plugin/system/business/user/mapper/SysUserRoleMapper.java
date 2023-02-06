package com.xxw.platform.plugin.system.business.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxw.platform.plugin.system.business.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户角色mapper接口
 *
 * @author liaoxiting
 * @date 2020/11/6 14:50
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 获取用户的管理员角色列表
     *
     * @author liaoxiting
     * @date 2022/9/30 13:20
     */
    List<SysUserRole> getAdminUserRoleList(@Param("userIdList") List<Long> userIdList);

}

