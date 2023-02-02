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
import com.xxw.platform.plugin.expand.business.entity.SysExpandField;
import com.xxw.platform.plugin.expand.business.enums.SysExpandFieldExceptionEnum;
import com.xxw.platform.plugin.expand.business.mapper.SysExpandFieldMapper;
import com.xxw.platform.plugin.expand.business.request.SysExpandFieldRequest;
import com.xxw.platform.plugin.expand.business.service.SysExpandFieldService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务拓展-字段信息业务实现层
 *
 * @author fengshuonan
 * @date 2022/03/29 23:47
 */
@Service
public class SysExpandFieldServiceImpl extends ServiceImpl<SysExpandFieldMapper, SysExpandField> implements SysExpandFieldService {

	@Override
    public void add(SysExpandFieldRequest sysExpandFieldRequest) {
        SysExpandField sysExpandField = new SysExpandField();
        BeanUtil.copyProperties(sysExpandFieldRequest, sysExpandField);
        this.save(sysExpandField);
    }

    @Override
    public void del(SysExpandFieldRequest sysExpandFieldRequest) {
        SysExpandField sysExpandField = this.querySysExpandField(sysExpandFieldRequest);
        this.removeById(sysExpandField.getFieldId());
    }

    @Override
    public void edit(SysExpandFieldRequest sysExpandFieldRequest) {
        SysExpandField sysExpandField = this.querySysExpandField(sysExpandFieldRequest);
        BeanUtil.copyProperties(sysExpandFieldRequest, sysExpandField);
        this.updateById(sysExpandField);
    }

    @Override
    public SysExpandField detail(SysExpandFieldRequest sysExpandFieldRequest) {
        return this.querySysExpandField(sysExpandFieldRequest);
    }

    @Override
    public PageResult<SysExpandField> findPage(SysExpandFieldRequest sysExpandFieldRequest) {
        LambdaQueryWrapper<SysExpandField> wrapper = createWrapper(sysExpandFieldRequest);
        Page<SysExpandField> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysExpandField> findList(SysExpandFieldRequest sysExpandFieldRequest) {
        LambdaQueryWrapper<SysExpandField> wrapper = this.createWrapper(sysExpandFieldRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2022/03/29 23:47
     */
    private SysExpandField querySysExpandField(SysExpandFieldRequest sysExpandFieldRequest) {
        SysExpandField sysExpandField = this.getById(sysExpandFieldRequest.getFieldId());
        if (ObjectUtil.isEmpty(sysExpandField)) {
            throw new ServiceException(SysExpandFieldExceptionEnum.SYS_EXPAND_FIELD_NOT_EXISTED);
        }
        return sysExpandField;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2022/03/29 23:47
     */
    private LambdaQueryWrapper<SysExpandField> createWrapper(SysExpandFieldRequest sysExpandFieldRequest) {
        LambdaQueryWrapper<SysExpandField> queryWrapper = new LambdaQueryWrapper<>();

        Long expandId = sysExpandFieldRequest.getExpandId();

        queryWrapper.eq(ObjectUtil.isNotNull(expandId), SysExpandField::getExpandId, expandId);

        return queryWrapper;
    }

}
