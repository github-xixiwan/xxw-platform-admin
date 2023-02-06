package com.xxw.platform.plugin.system.business.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.business.user.entity.SysUserGroupDetail;
import com.xxw.platform.plugin.system.business.user.enums.SysUserGroupDetailExceptionEnum;
import com.xxw.platform.plugin.system.business.user.mapper.SysUserGroupDetailMapper;
import com.xxw.platform.plugin.system.business.user.request.SysUserGroupDetailRequest;
import com.xxw.platform.plugin.system.business.user.service.SysUserGroupDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户组详情业务实现层
 *
 * @author liaoxiting
 * @date 2022/09/26 10:12
 */
@Service
public class SysUserGroupDetailServiceImpl extends ServiceImpl<SysUserGroupDetailMapper, SysUserGroupDetail> implements SysUserGroupDetailService {

	@Override
    public void add(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = new SysUserGroupDetail();
        BeanUtil.copyProperties(sysUserGroupDetailRequest, sysUserGroupDetail);
        this.save(sysUserGroupDetail);
    }

    @Override
    public void del(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = this.querySysUserGroupDetail(sysUserGroupDetailRequest);
        this.removeById(sysUserGroupDetail.getDetailId());
    }

    @Override
    public void edit(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = this.querySysUserGroupDetail(sysUserGroupDetailRequest);
        BeanUtil.copyProperties(sysUserGroupDetailRequest, sysUserGroupDetail);
        this.updateById(sysUserGroupDetail);
    }

    @Override
    public SysUserGroupDetail detail(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        return this.querySysUserGroupDetail(sysUserGroupDetailRequest);
    }

    @Override
    public PageResult<SysUserGroupDetail> findPage(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        LambdaQueryWrapper<SysUserGroupDetail> wrapper = createWrapper(sysUserGroupDetailRequest);
        Page<SysUserGroupDetail> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysUserGroupDetail> findList(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        LambdaQueryWrapper<SysUserGroupDetail> wrapper = this.createWrapper(sysUserGroupDetailRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    private SysUserGroupDetail querySysUserGroupDetail(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = this.getById(sysUserGroupDetailRequest.getDetailId());
        if (ObjectUtil.isEmpty(sysUserGroupDetail)) {
            throw new ServiceException(SysUserGroupDetailExceptionEnum.SYS_USER_GROUP_DETAIL_NOT_EXISTED);
        }
        return sysUserGroupDetail;
    }

    /**
     * 创建查询wrapper
     *
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    private LambdaQueryWrapper<SysUserGroupDetail> createWrapper(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        LambdaQueryWrapper<SysUserGroupDetail> queryWrapper = new LambdaQueryWrapper<>();

        Long detailId = sysUserGroupDetailRequest.getDetailId();
        Long userGroupId = sysUserGroupDetailRequest.getUserGroupId();
        Integer selectType = sysUserGroupDetailRequest.getSelectType();
        Long selectValue = sysUserGroupDetailRequest.getSelectValue();
        String selectValueName = sysUserGroupDetailRequest.getSelectValueName();
        String subSelectValue = sysUserGroupDetailRequest.getSubSelectValue();
        String subSelectValueName = sysUserGroupDetailRequest.getSubSelectValueName();

        queryWrapper.eq(ObjectUtil.isNotNull(detailId), SysUserGroupDetail::getDetailId, detailId);
        queryWrapper.eq(ObjectUtil.isNotNull(userGroupId), SysUserGroupDetail::getUserGroupId, userGroupId);
        queryWrapper.eq(ObjectUtil.isNotNull(selectType), SysUserGroupDetail::getSelectType, selectType);
        queryWrapper.eq(ObjectUtil.isNotNull(selectValue), SysUserGroupDetail::getSelectValue, selectValue);
        queryWrapper.like(ObjectUtil.isNotEmpty(selectValueName), SysUserGroupDetail::getSelectValueName, selectValueName);
        queryWrapper.like(ObjectUtil.isNotEmpty(subSelectValue), SysUserGroupDetail::getSubSelectValue, subSelectValue);
        queryWrapper.like(ObjectUtil.isNotEmpty(subSelectValueName), SysUserGroupDetail::getSubSelectValueName, subSelectValueName);

        return queryWrapper;
    }

}