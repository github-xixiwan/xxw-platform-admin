package com.xxw.platform.plugin.system.business.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.api.DictApi;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.business.user.entity.SysUserGroup;
import com.xxw.platform.plugin.system.business.user.entity.SysUserGroupDetail;
import com.xxw.platform.plugin.system.business.user.enums.SysUserGroupExceptionEnum;
import com.xxw.platform.plugin.system.business.user.factory.UserGroupFactory;
import com.xxw.platform.plugin.system.business.user.mapper.SysUserGroupMapper;
import com.xxw.platform.plugin.system.business.user.request.SysUserGroupRequest;
import com.xxw.platform.plugin.system.business.user.service.SysUserGroupDetailService;
import com.xxw.platform.plugin.system.business.user.service.SysUserGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组业务实现层
 *
 * @author liaoxiting
 * @date 2022/09/26 10:12
 */
@Service
public class SysUserGroupServiceImpl extends ServiceImpl<SysUserGroupMapper, SysUserGroup> implements SysUserGroupService {

    @Resource
    private SysUserGroupDetailService sysUserGroupDetailService;

    @Resource
    private DictApi dictApi;

    @Override
    public SysUserGroup add(SysUserGroupRequest sysUserGroupRequest) {

        SysUserGroup sysUserGroup = new SysUserGroup();
        sysUserGroup.setUserGroupId(IdWorker.getId());
        sysUserGroup.setUserGroupTitle(sysUserGroupRequest.getUserGroupTitle());
        sysUserGroup.setUserGroupDetailName(sysUserGroupRequest.getUserGroupDetailName());

        // 解析各个请求的list，转化成detail实体
        List<SysUserGroupDetail> userGroupDetail = UserGroupFactory.createUserGroupDetail(sysUserGroup.getUserGroupId(), sysUserGroupRequest);

        // 保存用户组信息和用户组的详情
        this.save(sysUserGroup);
        if (ObjectUtil.isNotEmpty(userGroupDetail)) {
            this.sysUserGroupDetailService.saveBatch(userGroupDetail);
        }

        return sysUserGroup;
    }

    @Override
    public void del(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.querySysUserGroup(sysUserGroupRequest);
        this.removeById(sysUserGroup.getUserGroupId());
    }

    @Override
    public void edit(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.querySysUserGroup(sysUserGroupRequest);
        BeanUtil.copyProperties(sysUserGroupRequest, sysUserGroup);
        this.updateById(sysUserGroup);
    }

    @Override
    public SysUserGroup detail(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.querySysUserGroup(sysUserGroupRequest);

        // 查询用户组的详情
        LambdaQueryWrapper<SysUserGroupDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserGroupDetail::getUserGroupId, sysUserGroup.getUserGroupId());
        List<SysUserGroupDetail> detailList = sysUserGroupDetailService.list(queryWrapper);

        // 将用户组的详情列表，转化为单独的list返回给前端
        return UserGroupFactory.parseToEntity(sysUserGroup, detailList);
    }

    @Override
    public PageResult<SysUserGroup> findPage(SysUserGroupRequest sysUserGroupRequest) {
        LambdaQueryWrapper<SysUserGroup> wrapper = createWrapper(sysUserGroupRequest);
        Page<SysUserGroup> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SimpleDict> getSelectRelationList() {
        return dictApi.getDictDetailsByDictTypeCode(SystemConstants.SELECT_TYPE_DICT_TYPE_CODE);
    }

    @Override
    public List<SysUserGroup> findList(SysUserGroupRequest sysUserGroupRequest) {
        LambdaQueryWrapper<SysUserGroup> wrapper = this.createWrapper(sysUserGroupRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    private SysUserGroup querySysUserGroup(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.getById(sysUserGroupRequest.getUserGroupId());
        if (ObjectUtil.isEmpty(sysUserGroup)) {
            throw new ServiceException(SysUserGroupExceptionEnum.SYS_USER_GROUP_NOT_EXISTED);
        }
        return sysUserGroup;
    }

    /**
     * 创建查询wrapper
     *
     * @author liaoxiting
     * @date 2022/09/26 10:12
     */
    private LambdaQueryWrapper<SysUserGroup> createWrapper(SysUserGroupRequest sysUserGroupRequest) {
        LambdaQueryWrapper<SysUserGroup> queryWrapper = new LambdaQueryWrapper<>();

        Long userGroupId = sysUserGroupRequest.getUserGroupId();
        String userGroupTitle = sysUserGroupRequest.getUserGroupTitle();
        String userGroupDetailName = sysUserGroupRequest.getUserGroupDetailName();

        queryWrapper.eq(ObjectUtil.isNotNull(userGroupId), SysUserGroup::getUserGroupId, userGroupId);
        queryWrapper.like(ObjectUtil.isNotEmpty(userGroupTitle), SysUserGroup::getUserGroupTitle, userGroupTitle);
        queryWrapper.like(ObjectUtil.isNotEmpty(userGroupDetailName), SysUserGroup::getUserGroupDetailName, userGroupDetailName);

        return queryWrapper;
    }

}