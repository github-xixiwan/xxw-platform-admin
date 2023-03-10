package com.xxw.platform.plugin.system.business.home.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.business.home.entity.SysStatisticsCount;
import com.xxw.platform.plugin.system.business.home.enums.SysStatisticsCountExceptionEnum;
import com.xxw.platform.plugin.system.business.home.mapper.SysStatisticsCountMapper;
import com.xxw.platform.plugin.system.business.home.pojo.request.SysStatisticsCountRequest;
import com.xxw.platform.plugin.system.business.home.service.SysStatisticsCountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 常用功能的统计次数业务实现层
 *
 * @author liaoxiting
 * @date 2022/02/10 21:17
 */
@Service
public class SysStatisticsCountServiceImpl extends ServiceImpl<SysStatisticsCountMapper, SysStatisticsCount> implements SysStatisticsCountService {

    @Override
    public void add(SysStatisticsCountRequest sysStatisticsCountRequest) {
        SysStatisticsCount sysStatisticsCount = new SysStatisticsCount();
        BeanUtil.copyProperties(sysStatisticsCountRequest, sysStatisticsCount);
        this.save(sysStatisticsCount);
    }

    @Override
    public void del(SysStatisticsCountRequest sysStatisticsCountRequest) {
        SysStatisticsCount sysStatisticsCount = this.querySysStatisticsCount(sysStatisticsCountRequest);
        this.removeById(sysStatisticsCount.getStatCountId());
    }

    @Override
    public void edit(SysStatisticsCountRequest sysStatisticsCountRequest) {
        SysStatisticsCount sysStatisticsCount = this.querySysStatisticsCount(sysStatisticsCountRequest);
        BeanUtil.copyProperties(sysStatisticsCountRequest, sysStatisticsCount);
        this.updateById(sysStatisticsCount);
    }

    @Override
    public SysStatisticsCount detail(SysStatisticsCountRequest sysStatisticsCountRequest) {
        return this.querySysStatisticsCount(sysStatisticsCountRequest);
    }

    @Override
    public PageResult<SysStatisticsCount> findPage(SysStatisticsCountRequest sysStatisticsCountRequest) {
        LambdaQueryWrapper<SysStatisticsCount> wrapper = createWrapper(sysStatisticsCountRequest);
        Page<SysStatisticsCount> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public Integer getUserUrlCount(Long userId, Long statUrlId) {
        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(statUrlId)) {
            throw new SystemModularException(SysStatisticsCountExceptionEnum.PARAM_EMPTY);
        }
        LambdaQueryWrapper<SysStatisticsCount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysStatisticsCount::getUserId, userId);
        queryWrapper.eq(SysStatisticsCount::getStatUrlId, statUrlId);
        SysStatisticsCount one = this.getOne(queryWrapper);
        if (one != null) {
            return one.getStatCount();
        } else {
            return 0;
        }
    }

    @Override
    public List<SysStatisticsCount> findList(SysStatisticsCountRequest sysStatisticsCountRequest) {
        LambdaQueryWrapper<SysStatisticsCount> wrapper = this.createWrapper(sysStatisticsCountRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author liaoxiting
     * @date 2022/02/10 21:17
     */
    private SysStatisticsCount querySysStatisticsCount(SysStatisticsCountRequest sysStatisticsCountRequest) {
        SysStatisticsCount sysStatisticsCount = this.getById(sysStatisticsCountRequest.getStatCountId());
        if (ObjectUtil.isEmpty(sysStatisticsCount)) {
            throw new ServiceException(SysStatisticsCountExceptionEnum.SYS_STATISTICS_COUNT_NOT_EXISTED);
        }
        return sysStatisticsCount;
    }

    /**
     * 创建查询wrapper
     *
     * @author liaoxiting
     * @date 2022/02/10 21:17
     */
    private LambdaQueryWrapper<SysStatisticsCount> createWrapper(SysStatisticsCountRequest sysStatisticsCountRequest) {
        LambdaQueryWrapper<SysStatisticsCount> queryWrapper = new LambdaQueryWrapper<>();

        Long userId = sysStatisticsCountRequest.getUserId();

        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysStatisticsCount::getUserId, userId);

        return queryWrapper;
    }

}
