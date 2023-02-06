package com.xxw.platform.plugin.system.business.user.service;

import com.xxw.platform.plugin.system.api.pojo.user.SysUserAdminDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysAdminRequest;

import java.util.List;

/**
 * 后台管理员用户业务
 *
 * @author liaoxiting
 * @date 2022/9/30 11:05
 */
public interface SysUserAdminService {

    /**
     * 获取管理员列表
     *
     * @return 返回的包装类中，id是角色id
     * @author liaoxiting
     * @date 2022/9/30 11:06
     */
    List<SysUserAdminDTO> getAdminUserList();

    /**
     * 添加管理员
     *
     * @author liaoxiting
     * @date 2022/9/30 13:12
     */
    void addAdminUser(SysAdminRequest sysAdminRequest);

    /**
     * 删除管理员
     *
     * @author liaoxiting
     * @date 2022/9/30 13:52
     */
    void deleteAdminUser(SysAdminRequest sysAdminRequest);

}
