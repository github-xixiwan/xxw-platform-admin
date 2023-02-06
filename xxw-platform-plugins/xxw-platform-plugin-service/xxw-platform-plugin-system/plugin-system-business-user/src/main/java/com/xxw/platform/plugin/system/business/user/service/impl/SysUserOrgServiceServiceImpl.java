package com.xxw.platform.plugin.system.business.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.user.SysUserOrgExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserOrgDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.UserOrgRequest;
import com.xxw.platform.plugin.system.business.user.entity.SysUserOrg;
import com.xxw.platform.plugin.system.business.user.mapper.SysUserOrgMapper;
import com.xxw.platform.plugin.system.business.user.service.SysUserOrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
/**
 * 用户组织机构关联信息
 *
 * @author liaoxiting
 * @date 2020/12/19 22:17
 */
@Service
public class SysUserOrgServiceServiceImpl extends ServiceImpl<SysUserOrgMapper, SysUserOrg> implements SysUserOrgService {

    @Resource(name = "userOrgCacheApi")
    private CacheOperatorApi<SysUserOrgDTO> userOrgCacheApi;

    @Override
    public SysUserOrgDTO getUserOrgByUserId(Long userId) {

        // 从缓存获取数据
        String key = String.valueOf(userId);
        SysUserOrgDTO sysUserDTOCache = userOrgCacheApi.get(key);
        if (sysUserDTOCache != null) {
            return sysUserDTOCache;
        }

        UserOrgRequest userOrgRequest = new UserOrgRequest();
        userOrgRequest.setUserId(userId);
        SysUserOrg sysUserOrg = this.detail(userOrgRequest);
        if (ObjectUtil.isEmpty(sysUserOrg)) {
            throw new SystemModularException(SysUserOrgExceptionEnum.EMPLOYEE_MANY_MAIN_NOT_FOUND);
        }
        SysUserOrgDTO sysUserOrgDTO = new SysUserOrgDTO();
        BeanUtil.copyProperties(sysUserOrg, sysUserOrgDTO);

        // 加入缓存
        userOrgCacheApi.put(key, sysUserOrgDTO);

        return sysUserOrgDTO;
    }
    @Override
    public void add(UserOrgRequest userOrgResponse) {
        SysUserOrg sysUserOrg = new SysUserOrg();
        BeanUtil.copyProperties(userOrgResponse, sysUserOrg);
        this.save(sysUserOrg);
    }

    @Override
    public void add(Long userId, Long orgId) {
        SysUserOrg sysUserOrg = new SysUserOrg();
        sysUserOrg.setUserId(userId);
        sysUserOrg.setOrgId(orgId);
        this.save(sysUserOrg);
    }

    @Override
    public void add(Long userId, Long orgId, Long positionId) {
        SysUserOrg sysUserOrg = new SysUserOrg();
        sysUserOrg.setUserId(userId);
        sysUserOrg.setOrgId(orgId);
        sysUserOrg.setPositionId(positionId);
        this.save(sysUserOrg);
    }

    @Override
    public void del(UserOrgRequest userOrgResponse) {
        SysUserOrg sysUserOrg = this.querySysUserOrgById(userOrgResponse);
        this.removeById(sysUserOrg.getUserOrgId());

        // 清除缓存
        userOrgCacheApi.remove(String.valueOf(sysUserOrg.getUserId()));
    }

    @Override
    public void delByUserId(Long userId) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserOrg::getUserId, userId);
        this.remove(queryWrapper);

        // 清除缓存
        userOrgCacheApi.remove(String.valueOf(userId));
    }

    @Override
    public void edit(UserOrgRequest userOrgResponse) {
        SysUserOrg sysUserOrg = this.querySysUserOrgById(userOrgResponse);
        BeanUtil.copyProperties(userOrgResponse, sysUserOrg);
        this.updateById(sysUserOrg);

        // 清除缓存
        userOrgCacheApi.remove(String.valueOf(sysUserOrg.getUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Long userId, Long orgId) {

        // 删除已有绑定的组织机构
        this.delByUserId(userId);

        // 新增组织机构绑定
        this.add(userId, orgId);

        // 清除缓存
        userOrgCacheApi.remove(String.valueOf(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Long userId, Long orgId, Long positionId) {

        // 删除已有绑定的组织机构
        this.delByUserId(userId);

        // 新增组织机构绑定
        this.add(userId, orgId, positionId);

        // 清除缓存
        userOrgCacheApi.remove(String.valueOf(userId));
    }
    @Override
    public SysUserOrg detail(UserOrgRequest userOrgResponse) {
        return this.getOne(this.createWrapper(userOrgResponse), false);
    }

    @Override
    public List<SysUserOrg> findList(UserOrgRequest userOrgResponse) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = this.createWrapper(userOrgResponse);
        return this.list(queryWrapper);
    }
    @Override
    public Boolean getUserOrgFlag(Long orgId, Long positionId) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotNull(orgId), SysUserOrg::getOrgId, orgId);
        queryWrapper.eq(ObjectUtil.isNotNull(positionId), SysUserOrg::getPositionId, positionId);
        return this.list(queryWrapper).size() > 0;
    }
    /**
     * 根据主键id获取对象
     *
     * @author liaoxiting
     * @date 2021/1/26 13:28
     */
    private SysUserOrg querySysUserOrgById(UserOrgRequest userOrgResponse) {
        SysUserOrg sysUserOrg = this.getById(userOrgResponse.getUserOrgId());
        if (ObjectUtil.isEmpty(sysUserOrg)) {
            throw new SystemModularException(SysUserOrgExceptionEnum.USER_ORG_NOT_EXIST, sysUserOrg.getOrgId());
        }
        return sysUserOrg;
    }

    /**
     * 实体构建queryWrapper
     *
     * @author liaoxiting
     * @date 2021/1/24 22:03
     */
    private LambdaQueryWrapper<SysUserOrg> createWrapper(UserOrgRequest userOrgResponse) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotEmpty(userOrgResponse.getUserOrgId()), SysUserOrg::getUserOrgId, userOrgResponse.getUserOrgId());
        queryWrapper.eq(ObjectUtil.isNotEmpty(userOrgResponse.getUserId()), SysUserOrg::getUserId, userOrgResponse.getUserId());
        queryWrapper.eq(ObjectUtil.isNotEmpty(userOrgResponse.getOrgId()), SysUserOrg::getOrgId, userOrgResponse.getOrgId());
        queryWrapper.eq(ObjectUtil.isNotEmpty(userOrgResponse.getPositionId()), SysUserOrg::getPositionId, userOrgResponse.getPositionId());
        return queryWrapper;
    }

}
