package com.xxw.platform.plugin.system.business.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.business.user.entity.SysUserGroup;
import com.xxw.platform.plugin.system.business.user.request.SysUserGroupRequest;

import java.util.List;

/**
 * 用户组 服务类
 *
 * @author liaoxiting
 * @date 2022/09/26 10:12
 */
public interface SysUserGroupService extends IService<SysUserGroup> {

    /**
     * 新增
     *
     * @param sysUserGroupRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    SysUserGroup add(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 删除
     *
     * @param sysUserGroupRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    void del(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 编辑
     *
     * @param sysUserGroupRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    void edit(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 查询详情
     *
     * @param sysUserGroupRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    SysUserGroup detail(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 获取列表
     *
     * @param sysUserGroupRequest 请求参数
     * @return List<SysUserGroup>   返回结果
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    List<SysUserGroup> findList(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysUserGroupRequest 请求参数
     * @return PageResult<SysUserGroup>   返回结果
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    PageResult<SysUserGroup> findPage(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 获取选择关系列表
     *
     * @author liaoxiting
     * @date 2022/9/26 17:30
     */
    List<SimpleDict> getSelectRelationList();

}