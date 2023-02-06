package com.xxw.platform.plugin.system.business.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.business.user.entity.SysUserGroupDetail;
import com.xxw.platform.plugin.system.business.user.request.SysUserGroupDetailRequest;

import java.util.List;

/**
 * 用户组详情 服务类
 *
 * @author liaoxiting
 * @date 2022/09/26 10:12
 */
public interface SysUserGroupDetailService extends IService<SysUserGroupDetail> {

	/**
     * 新增
     *
     * @param sysUserGroupDetailRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    void add(SysUserGroupDetailRequest sysUserGroupDetailRequest);

	/**
     * 删除
     *
     * @param sysUserGroupDetailRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    void del(SysUserGroupDetailRequest sysUserGroupDetailRequest);

	/**
     * 编辑
     *
     * @param sysUserGroupDetailRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    void edit(SysUserGroupDetailRequest sysUserGroupDetailRequest);

	/**
     * 查询详情
     *
     * @param sysUserGroupDetailRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    SysUserGroupDetail detail(SysUserGroupDetailRequest sysUserGroupDetailRequest);

	/**
     * 获取列表
     *
     * @param sysUserGroupDetailRequest        请求参数
     * @return List<SysUserGroupDetail>   返回结果
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    List<SysUserGroupDetail> findList(SysUserGroupDetailRequest sysUserGroupDetailRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysUserGroupDetailRequest              请求参数
     * @return PageResult<SysUserGroupDetail>   返回结果
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    PageResult<SysUserGroupDetail> findPage(SysUserGroupDetailRequest sysUserGroupDetailRequest);

}