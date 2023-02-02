package com.xxw.platform.plugin.system.business.theme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.theme.SysThemeTemplateExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateRelRequest;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplate;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplateRel;
import com.xxw.platform.plugin.system.business.theme.mapper.SysThemeTemplateRelMapper;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeTemplateRelService;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统主题模板属性关系service接口实现类
 *
 * @author xixiaowei
 * @date 2021/12/17 16:14
 */
@Service
public class SysThemeTemplateRelServiceImpl extends ServiceImpl<SysThemeTemplateRelMapper, SysThemeTemplateRel> implements SysThemeTemplateRelService {

    @Resource
    private SysThemeTemplateService sysThemeTemplateService;

    @Override
    public void add(SysThemeTemplateRelRequest sysThemeTemplateRelRequest) {
        // 校验模板状态
        this.checkTemplateStatus(sysThemeTemplateRelRequest);

        // 获取请求中的所有属性编码
        String[] fieldCodes = sysThemeTemplateRelRequest.getFieldCodes();

        List<SysThemeTemplateRel> sysThemeTemplateRels = new ArrayList<>();

        // 填充对象
        for (String fieldCode : fieldCodes) {
            SysThemeTemplateRel sysThemeTemplateRel = new SysThemeTemplateRel();
            sysThemeTemplateRel.setTemplateId(sysThemeTemplateRelRequest.getTemplateId());
            sysThemeTemplateRel.setFieldCode(fieldCode);

            sysThemeTemplateRels.add(sysThemeTemplateRel);
        }

        // 保存关系
        this.saveBatch(sysThemeTemplateRels);
    }

    /**
     * 校验模板使用状态
     *
     * @author xixiaowei
     * @date 2021/12/30 17:28
     */
    private void checkTemplateStatus(SysThemeTemplateRelRequest sysThemeTemplateRelRequest) {
        // 判断当前模板是否被使用
        LambdaQueryWrapper<SysThemeTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThemeTemplate::getTemplateId, sysThemeTemplateRelRequest.getTemplateId());
        SysThemeTemplate sysThemeTemplate = sysThemeTemplateService.getOne(queryWrapper, true);
        // 判断状态，如果是启用则禁止操作
        if (YesOrNotEnum.Y.getCode().equals(sysThemeTemplate.getStatusFlag().toString())) {
            throw new SystemModularException(SysThemeTemplateExceptionEnum.TEMPLATE_IS_USED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysThemeTemplateRelRequest sysThemeTemplateRelRequest) {
        // 校验模板状态
        this.checkTemplateStatus(sysThemeTemplateRelRequest);

        // 获取请求中的所有属性编码
        String[] fieldCodes = sysThemeTemplateRelRequest.getFieldCodes();

        // 构建删除条件
        LambdaQueryWrapper<SysThemeTemplateRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysThemeTemplateRel::getFieldCode, fieldCodes);

        this.remove(queryWrapper);
    }
}
