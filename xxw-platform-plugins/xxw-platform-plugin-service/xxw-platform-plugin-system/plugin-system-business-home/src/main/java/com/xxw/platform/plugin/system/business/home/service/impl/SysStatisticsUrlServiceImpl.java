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
import com.xxw.platform.plugin.system.business.home.entity.SysStatisticsUrl;
import com.xxw.platform.plugin.system.business.home.enums.SysStatisticsUrlExceptionEnum;
import com.xxw.platform.plugin.system.business.home.mapper.SysStatisticsUrlMapper;
import com.xxw.platform.plugin.system.business.home.pojo.request.SysStatisticsUrlRequest;
import com.xxw.platform.plugin.system.business.home.service.SysStatisticsUrlService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 常用功能列表业务实现层
 *
 * @author liaoxiting
 * @date 2022/02/10 21:17
 */
@Service
public class SysStatisticsUrlServiceImpl extends ServiceImpl<SysStatisticsUrlMapper, SysStatisticsUrl> implements SysStatisticsUrlService {

	@Override
    public void add(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        SysStatisticsUrl sysStatisticsUrl = new SysStatisticsUrl();
        BeanUtil.copyProperties(sysStatisticsUrlRequest, sysStatisticsUrl);
        this.save(sysStatisticsUrl);
    }

    @Override
    public void del(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        SysStatisticsUrl sysStatisticsUrl = this.querySysStatisticsUrl(sysStatisticsUrlRequest);
        this.removeById(sysStatisticsUrl.getStatUrlId());
    }

    @Override
    public void edit(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        SysStatisticsUrl sysStatisticsUrl = this.querySysStatisticsUrl(sysStatisticsUrlRequest);
        BeanUtil.copyProperties(sysStatisticsUrlRequest, sysStatisticsUrl);
        this.updateById(sysStatisticsUrl);
    }

    @Override
    public SysStatisticsUrl detail(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        return this.querySysStatisticsUrl(sysStatisticsUrlRequest);
    }

    @Override
    public PageResult<SysStatisticsUrl> findPage(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        LambdaQueryWrapper<SysStatisticsUrl> wrapper = createWrapper(sysStatisticsUrlRequest);
        Page<SysStatisticsUrl> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysStatisticsUrl> findList(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        LambdaQueryWrapper<SysStatisticsUrl> wrapper = this.createWrapper(sysStatisticsUrlRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author liaoxiting
     * @date 2022/02/10 21:17
     */
    private SysStatisticsUrl querySysStatisticsUrl(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        SysStatisticsUrl sysStatisticsUrl = this.getById(sysStatisticsUrlRequest.getStatUrlId());
        if (ObjectUtil.isEmpty(sysStatisticsUrl)) {
            throw new ServiceException(SysStatisticsUrlExceptionEnum.SYS_STATISTICS_URL_NOT_EXISTED);
        }
        return sysStatisticsUrl;
    }

    /**
     * 创建查询wrapper
     *
     * @author liaoxiting
     * @date 2022/02/10 21:17
     */
    private LambdaQueryWrapper<SysStatisticsUrl> createWrapper(SysStatisticsUrlRequest sysStatisticsUrlRequest) {
        LambdaQueryWrapper<SysStatisticsUrl> queryWrapper = new LambdaQueryWrapper<>();

        Long statUrlId = sysStatisticsUrlRequest.getStatUrlId();
        String statName = sysStatisticsUrlRequest.getStatName();
        Long statMenuId = sysStatisticsUrlRequest.getStatMenuId();
        String statUrl = sysStatisticsUrlRequest.getStatUrl();
        String alwaysShow = sysStatisticsUrlRequest.getAlwaysShow();

        queryWrapper.eq(ObjectUtil.isNotNull(statUrlId), SysStatisticsUrl::getStatUrlId, statUrlId);
        queryWrapper.like(ObjectUtil.isNotEmpty(statName), SysStatisticsUrl::getStatName, statName);
        queryWrapper.eq(ObjectUtil.isNotEmpty(statMenuId), SysStatisticsUrl::getStatMenuId, statMenuId);
        queryWrapper.like(ObjectUtil.isNotEmpty(statUrl), SysStatisticsUrl::getStatUrl, statUrl);
        queryWrapper.like(ObjectUtil.isNotEmpty(alwaysShow), SysStatisticsUrl::getAlwaysShow, alwaysShow);

        return queryWrapper;
    }

}
