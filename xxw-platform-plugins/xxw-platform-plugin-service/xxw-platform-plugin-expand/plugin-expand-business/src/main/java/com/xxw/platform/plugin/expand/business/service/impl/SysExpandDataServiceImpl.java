package com.xxw.platform.plugin.expand.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.business.entity.SysExpand;
import com.xxw.platform.plugin.expand.business.entity.SysExpandData;
import com.xxw.platform.plugin.expand.business.entity.SysExpandField;
import com.xxw.platform.plugin.expand.business.enums.SysExpandDataExceptionEnum;
import com.xxw.platform.plugin.expand.business.mapper.SysExpandDataMapper;
import com.xxw.platform.plugin.expand.business.request.SysExpandDataRequest;
import com.xxw.platform.plugin.expand.business.request.SysExpandFieldRequest;
import com.xxw.platform.plugin.expand.business.request.SysExpandRequest;
import com.xxw.platform.plugin.expand.business.service.SysExpandDataService;
import com.xxw.platform.plugin.expand.business.service.SysExpandFieldService;
import com.xxw.platform.plugin.expand.business.service.SysExpandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务拓展-具体数据业务实现层
 *
 * @author liaoxiting
 * @date 2022/03/29 23:47
 */
@Service
public class SysExpandDataServiceImpl extends ServiceImpl<SysExpandDataMapper, SysExpandData> implements SysExpandDataService {

    @Resource
    private SysExpandService sysExpandService;

    @Resource
    private SysExpandFieldService sysExpandFieldService;

    @Override
    public void add(SysExpandDataRequest sysExpandDataRequest) {
        SysExpandData sysExpandData = new SysExpandData();
        BeanUtil.copyProperties(sysExpandDataRequest, sysExpandData);
        this.save(sysExpandData);
    }

    @Override
    public void del(SysExpandDataRequest sysExpandDataRequest) {
        SysExpandData sysExpandData = this.querySysExpandData(sysExpandDataRequest);
        this.removeById(sysExpandData.getExpandDataId());
    }

    @Override
    public void edit(SysExpandDataRequest sysExpandDataRequest) {
        SysExpandData sysExpandData = this.querySysExpandData(sysExpandDataRequest);
        BeanUtil.copyProperties(sysExpandDataRequest, sysExpandData);
        this.updateById(sysExpandData);
    }

    @Override
    public SysExpandData detail(SysExpandDataRequest sysExpandDataRequest) {
        // 获取拓展数据详情
        SysExpandData sysExpandData = this.querySysExpandData(sysExpandDataRequest);

        // 获取拓展业务信息
        SysExpandRequest sysExpandRequest = new SysExpandRequest();
        sysExpandRequest.setExpandId(sysExpandData.getExpandId());
        SysExpand detail = sysExpandService.detail(sysExpandRequest);

        // 获取拓展业务的字段信息
        SysExpandFieldRequest sysExpandFieldRequest = new SysExpandFieldRequest();
        sysExpandFieldRequest.setExpandId(sysExpandData.getExpandId());
        List<SysExpandField> list = sysExpandFieldService.findList(sysExpandFieldRequest);

        // 设置返回结果的元数据字段信息
        sysExpandData.setExpandInfo(detail);
        sysExpandData.setFieldInfoList(list);
        return sysExpandData;
    }

    @Override
    public SysExpandData detailByPrimaryFieldValue(Long primaryFieldValue) {
        LambdaQueryWrapper<SysExpandData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExpandData::getPrimaryFieldValue, primaryFieldValue);
        return this.getOne(wrapper, false);
    }

    @Override
    public PageResult<SysExpandData> findPage(SysExpandDataRequest sysExpandDataRequest) {
        LambdaQueryWrapper<SysExpandData> wrapper = createWrapper(sysExpandDataRequest);
        Page<SysExpandData> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysExpandData> findList(SysExpandDataRequest sysExpandDataRequest) {
        LambdaQueryWrapper<SysExpandData> wrapper = this.createWrapper(sysExpandDataRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    private SysExpandData querySysExpandData(SysExpandDataRequest sysExpandDataRequest) {
        SysExpandData sysExpandData = this.getById(sysExpandDataRequest.getExpandDataId());
        if (ObjectUtil.isEmpty(sysExpandData)) {
            throw new ServiceException(SysExpandDataExceptionEnum.SYS_EXPAND_DATA_NOT_EXISTED);
        }
        return sysExpandData;
    }

    /**
     * 创建查询wrapper
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    private LambdaQueryWrapper<SysExpandData> createWrapper(SysExpandDataRequest sysExpandDataRequest) {
        LambdaQueryWrapper<SysExpandData> queryWrapper = new LambdaQueryWrapper<>();

        Long expandDataId = sysExpandDataRequest.getExpandDataId();
        Long expandId = sysExpandDataRequest.getExpandId();
        Long primaryFieldValue = sysExpandDataRequest.getPrimaryFieldValue();
        String expandData = sysExpandDataRequest.getExpandData();

        queryWrapper.eq(ObjectUtil.isNotNull(expandDataId), SysExpandData::getExpandDataId, expandDataId);
        queryWrapper.eq(ObjectUtil.isNotNull(expandId), SysExpandData::getExpandId, expandId);
        queryWrapper.eq(ObjectUtil.isNotNull(primaryFieldValue), SysExpandData::getPrimaryFieldValue, primaryFieldValue);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandData), SysExpandData::getExpandData, expandData);

        return queryWrapper;
    }

}
