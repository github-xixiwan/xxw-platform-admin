package com.xxw.platform.plugin.system.business.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.api.UserOrgServiceApi;
import com.xxw.platform.plugin.system.api.pojo.user.request.UserOrgRequest;
import com.xxw.platform.plugin.system.business.user.entity.SysUserOrg;

import java.util.List;

/**
 * 用户组织机构关联信息
 *
 * @author liaoxiting
 * @date 2020/12/19 22:17
 */
public interface SysUserOrgService extends IService<SysUserOrg>, UserOrgServiceApi {

    /**
     * 新增
     *
     * @param userOrgResponse 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void add(UserOrgRequest userOrgResponse);

    /**
     * 新增
     *
     * @param userId     用户id
     * @param orgId      机构id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void add(Long userId, Long orgId);

    /**
     * 新增
     *
     * @param userId     用户id
     * @param orgId      机构id
     * @param positionId 职位id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void add(Long userId, Long orgId, Long positionId);

    /**
     * 删除
     *
     * @param userOrgResponse 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void del(UserOrgRequest userOrgResponse);

    /**
     * 删除
     *
     * @param userId 用户id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void delByUserId(Long userId);

    /**
     * 修改
     *
     * @param userOrgResponse 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void edit(UserOrgRequest userOrgResponse);

    /**
     * 修改
     *
     * @param userId     用户id
     * @param orgId      机构id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void edit(Long userId, Long orgId);

    /**
     * 修改
     *
     * @param userId     用户id
     * @param orgId      机构id
     * @param positionId 职位id
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void edit(Long userId, Long orgId, Long positionId);

    /**
     * 详情
     *
     * @param userOrgResponse 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    SysUserOrg detail(UserOrgRequest userOrgResponse);

    /**
     * 查询-列表
     *
     * @param userOrgResponse 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    List<SysUserOrg> findList(UserOrgRequest userOrgResponse);
}
