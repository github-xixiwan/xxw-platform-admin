package com.xxw.platform.plugin.expand.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.api.DictApi;
import com.xxw.platform.plugin.expand.api.enums.FieldTypeEnum;
import com.xxw.platform.plugin.expand.api.pojo.ExpandDataInfo;
import com.xxw.platform.plugin.expand.api.pojo.ExpandFieldInfo;
import com.xxw.platform.plugin.expand.business.entity.SysExpand;
import com.xxw.platform.plugin.expand.business.entity.SysExpandData;
import com.xxw.platform.plugin.expand.business.entity.SysExpandField;
import com.xxw.platform.plugin.expand.business.enums.SysExpandExceptionEnum;
import com.xxw.platform.plugin.expand.business.mapper.SysExpandMapper;
import com.xxw.platform.plugin.expand.business.request.SysExpandFieldRequest;
import com.xxw.platform.plugin.expand.business.request.SysExpandRequest;
import com.xxw.platform.plugin.expand.business.service.SysExpandDataService;
import com.xxw.platform.plugin.expand.business.service.SysExpandFieldService;
import com.xxw.platform.plugin.expand.business.service.SysExpandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务拓展业务实现层
 *
 * @author liaoxiting
 * @date 2022/03/29 23:47
 */
@Service
public class SysExpandServiceImpl extends ServiceImpl<SysExpandMapper, SysExpand> implements SysExpandService {

    @Resource
    private SysExpandFieldService sysExpandFieldService;

    @Resource
    private SysExpandDataService sysExpandDataService;

    @Resource
    private DictApi dictApi;

    @Override
    public void add(SysExpandRequest sysExpandRequest) {
        SysExpand sysExpand = new SysExpand();
        BeanUtil.copyProperties(sysExpandRequest, sysExpand);

        // 设置启用状态
        sysExpand.setExpandStatus(StatusEnum.ENABLE.getCode());

        this.save(sysExpand);
    }

    @Override
    public void del(SysExpandRequest sysExpandRequest) {
        SysExpand sysExpand = this.querySysExpand(sysExpandRequest);
        this.removeById(sysExpand.getExpandId());
    }

    @Override
    public void edit(SysExpandRequest sysExpandRequest) {
        SysExpand sysExpand = this.querySysExpand(sysExpandRequest);
        BeanUtil.copyProperties(sysExpandRequest, sysExpand);
        this.updateById(sysExpand);
    }

    @Override
    public SysExpand detail(SysExpandRequest sysExpandRequest) {
        return this.querySysExpand(sysExpandRequest);
    }

    @Override
    public PageResult<SysExpand> findPage(SysExpandRequest sysExpandRequest) {
        LambdaQueryWrapper<SysExpand> wrapper = createWrapper(sysExpandRequest);
        Page<SysExpand> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public void updateStatus(SysExpandRequest sysExpandRequest) {
        SysExpand sysExpand = this.querySysExpand(sysExpandRequest);
        sysExpand.setExpandStatus(sysExpandRequest.getExpandStatus());
        this.updateById(sysExpand);
    }

    @Override
    public SysExpandData getByExpandCode(SysExpandRequest sysExpandRequest) {
        // 根据编码获取拓展信息
        LambdaQueryWrapper<SysExpand> sysExpandLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysExpandLambdaQueryWrapper.eq(SysExpand::getExpandCode, sysExpandRequest.getExpandCode());
        SysExpand sysExpand = this.getOne(sysExpandLambdaQueryWrapper, false);
        if (sysExpand == null) {
            throw new ServiceException(SysExpandExceptionEnum.SYS_EXPAND_NOT_EXISTED);
        }

        // 获取拓展业务的字段信息
        SysExpandFieldRequest sysExpandFieldRequest = new SysExpandFieldRequest();
        sysExpandFieldRequest.setExpandId(sysExpand.getExpandId());
        List<SysExpandField> list = sysExpandFieldService.findList(sysExpandFieldRequest);

        // 如果传了主键id，则查询一下业务表单的数据
        SysExpandData sysExpandData = new SysExpandData();
        if (sysExpandRequest.getPrimaryFieldValue() != null) {
            sysExpandData = sysExpandDataService.detailByPrimaryFieldValue(sysExpandRequest.getPrimaryFieldValue());
            if (sysExpandData == null) {
                sysExpandData = new SysExpandData();
            }
        }

        //  设置返回信息
        sysExpandData.setExpandId(sysExpand.getExpandId());
        sysExpandData.setFieldInfoList(list);
        sysExpandData.setExpandInfo(sysExpand);

        return sysExpandData;
    }

    @Override
    public List<SysExpand> findList(SysExpandRequest sysExpandRequest) {
        LambdaQueryWrapper<SysExpand> wrapper = this.createWrapper(sysExpandRequest);
        wrapper.select(SysExpand::getExpandId, SysExpand::getExpandName, SysExpand::getExpandCode);
        wrapper.eq(SysExpand::getExpandStatus, StatusEnum.ENABLE.getCode());
        return this.list(wrapper);
    }

    @Override
    public void saveOrUpdateExpandData(ExpandDataInfo expandDataInfo) {
        if (expandDataInfo == null) {
            return;
        }

        Map<String, Object> dynamicFormData = expandDataInfo.getExpandData();
        if (dynamicFormData == null || dynamicFormData.size() <= 0) {
            return;
        }

        // 具体数据转化为json
        String dynamicData = JSON.toJSONString(dynamicFormData);

        SysExpandData saveData = new SysExpandData();
        saveData.setExpandId(expandDataInfo.getExpandId());
        saveData.setPrimaryFieldValue(expandDataInfo.getPrimaryFieldValue());
        saveData.setExpandData(dynamicData);

        // 查询数据有没有在库中存在
        LambdaQueryWrapper<SysExpandData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExpandData::getExpandId, expandDataInfo.getExpandId());
        wrapper.eq(SysExpandData::getPrimaryFieldValue, expandDataInfo.getPrimaryFieldValue());
        SysExpandData sysExpandData = this.sysExpandDataService.getOne(wrapper, false);

        // 数据库中不存在，则保存
        if (sysExpandData == null) {
            this.sysExpandDataService.save(saveData);
        } else {
            saveData.setExpandDataId(sysExpandData.getExpandDataId());
            this.sysExpandDataService.updateById(saveData);
        }
    }

    @Override
    public List<ExpandFieldInfo> getPageListExpandFieldList(String expandCode) {
        SysExpandRequest sysExpandRequest = new SysExpandRequest();
        sysExpandRequest.setExpandCode(expandCode);
        SysExpandData sysExpandData = this.getByExpandCode(sysExpandRequest);

        List<SysExpandField> fieldInfoList = sysExpandData.getFieldInfoList();

        ArrayList<ExpandFieldInfo> expandFieldInfos = new ArrayList<>();
        for (SysExpandField sysExpandField : fieldInfoList) {
            // 获取是否需要列表展示
            String listShowFlag = sysExpandField.getListShowFlag();
            if (YesOrNotEnum.Y.getCode().equals(listShowFlag)) {
                ExpandFieldInfo expandFieldInfo = new ExpandFieldInfo();
                expandFieldInfo.setExpandId(sysExpandField.getExpandId());
                expandFieldInfo.setFieldName(sysExpandField.getFieldName());
                expandFieldInfo.setFieldCode(sysExpandField.getFieldCode());
                expandFieldInfos.add(expandFieldInfo);
            }
        }

        return expandFieldInfos;
    }

    @Override
    public Map<String, Object> getExpandDataInfo(String expandCode, Long primaryFieldValue) {

        SysExpandRequest sysExpandRequest = new SysExpandRequest();
        sysExpandRequest.setExpandCode(expandCode);
        sysExpandRequest.setPrimaryFieldValue(primaryFieldValue);
        SysExpandData sysExpandData = this.getByExpandCode(sysExpandRequest);

        // 获取对应数据
        HashMap<String, Object> result = new HashMap<>();
        String expandData = sysExpandData.getExpandData();
        if (StrUtil.isEmpty(expandData)) {
            return result;
        }

        // 将json转化为Map
        JSONObject jsonObject = JSON.parseObject(expandData);

        // 获取字段元数据，将需要进行字典转化的，转化为字典中文名称
        List<SysExpandField> fieldInfoList = sysExpandData.getFieldInfoList();
        for (SysExpandField sysExpandField : fieldInfoList) {
            if (FieldTypeEnum.DICT.getCode().equals(sysExpandField.getFieldType())) {
                String dictTypeCode = sysExpandField.getFieldDictTypeCode();
                String dictValue = jsonObject.getString(sysExpandField.getFieldCode());
                String dictName = dictApi.getDictName(dictTypeCode, dictValue);
                jsonObject.put(sysExpandField.getFieldCode(), dictName);
            }
        }

        return jsonObject;
    }

    /**
     * 获取信息
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    private SysExpand querySysExpand(SysExpandRequest sysExpandRequest) {
        SysExpand sysExpand = this.getById(sysExpandRequest.getExpandId());
        if (ObjectUtil.isEmpty(sysExpand)) {
            throw new ServiceException(SysExpandExceptionEnum.SYS_EXPAND_NOT_EXISTED);
        }
        return sysExpand;
    }

    /**
     * 创建查询wrapper
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    private LambdaQueryWrapper<SysExpand> createWrapper(SysExpandRequest sysExpandRequest) {
        LambdaQueryWrapper<SysExpand> queryWrapper = new LambdaQueryWrapper<>();

        Long expandId = sysExpandRequest.getExpandId();
        String expandName = sysExpandRequest.getExpandName();
        String expandCode = sysExpandRequest.getExpandCode();

        queryWrapper.eq(ObjectUtil.isNotNull(expandId), SysExpand::getExpandId, expandId);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandName), SysExpand::getExpandName, expandName);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandCode), SysExpand::getExpandCode, expandCode);

        return queryWrapper;
    }
}
