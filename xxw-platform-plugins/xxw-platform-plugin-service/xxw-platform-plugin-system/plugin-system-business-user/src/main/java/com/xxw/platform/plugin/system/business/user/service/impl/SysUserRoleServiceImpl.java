package com.xxw.platform.plugin.system.business.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.UserRoleRequest;
import com.xxw.platform.plugin.system.business.user.entity.SysUserRole;
import com.xxw.platform.plugin.system.business.user.mapper.SysUserRoleMapper;
import com.xxw.platform.plugin.system.business.user.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户角色service接口实现类
 *
 * @author liaoxiting
 * @date 2020/11/6 10:28
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Resource(name = "userRoleCacheApi")
    private CacheOperatorApi<List<Long>> userRoleCacheApi;

    @Override
    public void add(UserRoleRequest userRoleRequest) {
        SysUserRole sysUserRole = new SysUserRole();
        BeanUtil.copyProperties(userRoleRequest, sysUserRole);
        this.save(sysUserRole);
    }

    @Override
    public void del(UserRoleRequest userRoleRequest) {
        SysUserRole sysUserRole = querySysUserRoleById(userRoleRequest);
        this.removeById(sysUserRole.getUserRoleId());
    }

    @Override
    public void delByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        this.remove(queryWrapper);
    }

    @Override
    public void edit(UserRoleRequest userRoleRequest) {
        SysUserRole sysUserRole = this.querySysUserRoleById(userRoleRequest);
        BeanUtil.copyProperties(userRoleRequest, sysUserRole);
        this.updateById(sysUserRole);
    }

    @Override
    public SysUserRole detail(UserRoleRequest userRoleRequest) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = this.createQueryWrapper(userRoleRequest);
        return this.getOne(queryWrapper, false);
    }

    @Override
    public List<SysUserRole> findList(UserRoleRequest userRoleRequest) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = this.createQueryWrapper(userRoleRequest);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysUserRole> findListByUserId(Long userId) {
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setUserId(userId);
        return this.findList(userRoleRequest);
    }

    @Override

    public List<Long> findRoleIdsByUserId(Long userId) {

        // 先从缓存获取用户绑定的角色
        String key = String.valueOf(userId);
        List<Long> userRolesCache = userRoleCacheApi.get(key);
        if (userRolesCache != null) {
            return userRolesCache;
        }

        // 从数据库查询角色
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setUserId(userId);
        List<SysUserRole> sysUserRoleList = this.findList(userRoleRequest);
        List<Long> userRoles = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        userRoleCacheApi.put(key, userRoles);
        return userRoles;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(SysUserRequest sysUserRequest) {
        // 获取用户id
        Long userId = sysUserRequest.getUserId();

        // 删除已有角色
        this.delByUserId(userId);

        // 为该用户授权角色
        List<Long> rileIds = sysUserRequest.getGrantRoleIdList();

        // 批量添加角色
        List<SysUserRole> sysUserRoleList = CollUtil.newArrayList();
        rileIds.forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRoleList.add(sysUserRole);
        });

        this.saveBatch(sysUserRoleList);

        // 清除用户角色的缓存
        userRoleCacheApi.remove(String.valueOf(userId));
    }

    /**
     * 根据主键查询
     *
     * @param userRoleRequest dto实体
     * @return
     * @author liaoxiting
     * @date 2021/2/3 15:02
     */
    private SysUserRole querySysUserRoleById(UserRoleRequest userRoleRequest) {
        return this.getById(userRoleRequest.getUserRoleId());
    }

    /**
     * 构建 QueryWrapper
     *
     * @param userRoleRequest dto实体
     * @author liaoxiting
     * @date 2021/2/3 14:54
     */
    private LambdaQueryWrapper<SysUserRole> createQueryWrapper(UserRoleRequest userRoleRequest) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        // SQL拼接
        queryWrapper.eq(ObjectUtil.isNotNull(userRoleRequest.getUserRoleId()), SysUserRole::getUserRoleId, userRoleRequest.getUserRoleId());
        queryWrapper.eq(ObjectUtil.isNotNull(userRoleRequest.getUserId()), SysUserRole::getUserId, userRoleRequest.getUserId());
        queryWrapper.eq(ObjectUtil.isNotNull(userRoleRequest.getRoleId()), SysUserRole::getRoleId, userRoleRequest.getRoleId());

        return queryWrapper;
    }

}
