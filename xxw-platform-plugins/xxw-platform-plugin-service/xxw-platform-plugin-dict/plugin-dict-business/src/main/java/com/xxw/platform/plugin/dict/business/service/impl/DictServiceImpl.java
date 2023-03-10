package com.xxw.platform.plugin.dict.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.tree.factory.DefaultTreeBuildFactory;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.api.constants.DictConstants;
import com.xxw.platform.plugin.dict.api.exception.DictException;
import com.xxw.platform.plugin.dict.api.exception.enums.DictExceptionEnum;
import com.xxw.platform.plugin.dict.business.entity.SysDict;
import com.xxw.platform.plugin.dict.business.entity.SysDictType;
import com.xxw.platform.plugin.dict.business.mapper.DictMapper;
import com.xxw.platform.plugin.dict.business.pojo.TreeDictInfo;
import com.xxw.platform.plugin.dict.business.pojo.request.DictRequest;
import com.xxw.platform.plugin.dict.business.service.DictService;
import com.xxw.platform.plugin.dict.business.service.DictTypeService;
import com.xxw.platform.plugin.pinyin.api.PinYinApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * 基础字典 服务实现类
 *
 * @author liaoxiting
 * @date 2020/12/26 22:36
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, SysDict> implements DictService {

    @Resource
    private PinYinApi pinYinApi;

    @Resource
    private DictTypeService dictTypeService;

    @Resource(name = "defaultStringCacheOperator")
    private CacheOperatorApi<String> defaultStringCacheOperator;

    private static final String CACHE_PREFIX = "dict:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DictRequest dictRequest) {

        // 校验字典重复
        this.validateRepeat(dictRequest, false);

        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictRequest, sysDict);
        sysDict.setDictParentId(DictConstants.DEFAULT_DICT_PARENT_ID);
        sysDict.setDictPids(StrUtil.BRACKET_START + DictConstants.DEFAULT_DICT_PARENT_ID + StrUtil.BRACKET_END + StrUtil.COMMA);
        sysDict.setStatusFlag(StatusEnum.ENABLE.getCode());
        sysDict.setDictNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDict.getDictName()));
        this.save(sysDict);
    }

    @Override
    public void del(DictRequest dictRequest) {
        SysDict sysDict = this.querySysDict(dictRequest);
        sysDict.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(sysDict);

        // 清除缓存中的字典值
        defaultStringCacheOperator.remove(CACHE_PREFIX + sysDict.getDictTypeCode() + "|" + sysDict.getDictCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DictRequest dictRequest) {

        // 校验字典重复
        this.validateRepeat(dictRequest, true);

        SysDict sysDict = this.querySysDict(dictRequest);
        BeanUtil.copyProperties(dictRequest, sysDict);

        // 不能修改字典类型和编码
        sysDict.setDictTypeCode(null);
        sysDict.setDictCode(null);
        sysDict.setDictNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDict.getDictName()));

        this.updateById(sysDict);

        // 清除缓存中的字典值
        defaultStringCacheOperator.remove(CACHE_PREFIX + sysDict.getDictTypeCode() + "|" + sysDict.getDictCode());
    }

    @Override
    public SysDict detail(DictRequest dictRequest) {
        return this.getOne(this.createWrapper(dictRequest), false);
    }

    @Override
    public List<SysDict> findList(DictRequest dictRequest) {
        return this.list(this.createWrapper(dictRequest));
    }

    @Override
    public PageResult<SysDict> findPage(DictRequest dictRequest) {
        Page<SysDict> page = this.page(PageFactory.defaultPage(), this.createWrapper(dictRequest));
        return PageResultFactory.createPageResult(page);
    }
    @Override
    public List<TreeDictInfo> getTreeDictList(DictRequest dictRequest) {

        // 获取字典类型下所有的字典
        List<SysDict> sysDictList = this.findList(dictRequest);
        if (sysDictList == null || sysDictList.isEmpty()) {
            return new ArrayList<>();
        }

        // 构造树节点信息
        ArrayList<TreeDictInfo> treeDictInfos = new ArrayList<>();
        for (SysDict sysDict : sysDictList) {
            TreeDictInfo treeDictInfo = new TreeDictInfo();
            treeDictInfo.setDictId(sysDict.getDictId());
            treeDictInfo.setDictCode(sysDict.getDictCode());
            treeDictInfo.setDictParentId(sysDict.getDictParentId());
            treeDictInfo.setDictName(sysDict.getDictName());
            treeDictInfos.add(treeDictInfo);
        }

        // 构建菜单树
        return new DefaultTreeBuildFactory<TreeDictInfo>().doTreeBuild(treeDictInfos);
    }

    @Override
    public String getDictName(String dictTypeCode, String dictCode) {
        String dictName = defaultStringCacheOperator.get(CACHE_PREFIX + dictTypeCode + "|" + dictCode);
        if (StrUtil.isNotEmpty(dictName)) {
            return dictName;
        }
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeCode, dictTypeCode);
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictCode);
        sysDictLambdaQueryWrapper.ne(SysDict::getDelFlag, YesOrNotEnum.Y.getCode());

        List<SysDict> list = this.list(sysDictLambdaQueryWrapper);

        // 如果查询不到字典，则返回空串
        if (list.isEmpty()) {
            return StrUtil.EMPTY;
        }

        // 字典code存在多个重复的，返回空串并打印错误日志
        if (list.size() > 1) {
            log.error(DictExceptionEnum.DICT_CODE_REPEAT.getUserTip(), "", dictCode);
            return StrUtil.EMPTY;
        }

        dictName = list.get(0).getDictName();
        defaultStringCacheOperator.put(CACHE_PREFIX + dictTypeCode + "|" + dictCode, dictName);
        if (dictName != null) {
            return dictName;
        } else {
            return StrUtil.EMPTY;
        }
    }

    @Override
    public List<SimpleDict> getDictDetailsByDictTypeCode(String dictTypeCode) {
        DictRequest dictRequest = new DictRequest();
        dictRequest.setDictTypeCode(dictTypeCode);
        LambdaQueryWrapper<SysDict> wrapper = createWrapper(dictRequest);
        List<SysDict> dictList = this.list(wrapper);
        if (dictList.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<SimpleDict> simpleDictList = new ArrayList<>();
        for (SysDict sysDict : dictList) {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setCode(sysDict.getDictCode());
            simpleDict.setName(sysDict.getDictName());
            simpleDictList.add(simpleDict);
        }
        return simpleDictList;
    }

    @Override
    public void deleteByDictId(Long dictId) {
        this.removeById(dictId);
    }

    /**
     * 获取详细信息
     *
     * @author liaoxiting
     * @date 2021/1/13 10:50
     */
    private SysDict querySysDict(DictRequest dictRequest) {
        SysDict sysDict = this.getById(dictRequest.getDictId());
        if (ObjectUtil.isNull(sysDict)) {
            throw new DictException(DictExceptionEnum.DICT_NOT_EXISTED, dictRequest.getDictId());
        }
        return sysDict;
    }

    /**
     * 构建wrapper
     *
     * @author liaoxiting
     * @date 2021/1/13 10:50
     */
    private LambdaQueryWrapper<SysDict> createWrapper(DictRequest dictRequest) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();

        // 如果传递了dictTypeId，先把dictTypeId转化为字典类型编码
        if (ObjectUtil.isNotEmpty(dictRequest.getDictTypeId())) {
            SysDictType sysDictType = dictTypeService.getById(dictRequest.getDictTypeId());
            if (sysDictType != null) {
                dictRequest.setDictTypeCode(sysDictType.getDictTypeCode());
            }
        }

        // SQL拼接
        queryWrapper.eq(ObjectUtil.isNotNull(dictRequest.getDictId()), SysDict::getDictId, dictRequest.getDictId());
        queryWrapper.eq(StrUtil.isNotBlank(dictRequest.getDictTypeCode()), SysDict::getDictTypeCode, dictRequest.getDictTypeCode());
        queryWrapper.eq(StrUtil.isNotBlank(dictRequest.getDictCode()), SysDict::getDictCode, dictRequest.getDictCode());
        queryWrapper.like(StrUtil.isNotBlank(dictRequest.getDictName()), SysDict::getDictName, dictRequest.getDictName());

        queryWrapper.ne(SysDict::getDelFlag, YesOrNotEnum.Y.getCode());
        return queryWrapper;
    }

    /**
     * 检查添加和编辑字典是否有重复的编码和名称
     *
     * @author liaoxiting
     * @date 2021/5/12 16:58
     */
    private void validateRepeat(DictRequest dictRequest, boolean editFlag) {

        // 检验同字典类型下是否有一样的编码
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeCode, dictRequest.getDictTypeCode());
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictRequest.getDictCode());
        if (editFlag) {
            sysDictLambdaQueryWrapper.ne(SysDict::getDictId, dictRequest.getDictId());
        }
        sysDictLambdaQueryWrapper.ne(SysDict::getDelFlag, YesOrNotEnum.Y.getCode());
        long count = this.count(sysDictLambdaQueryWrapper);
        if (count > 0) {
            throw new DictException(DictExceptionEnum.DICT_CODE_REPEAT, dictRequest.getDictTypeCode(), dictRequest.getDictCode());
        }

        // 检验同字典类型下是否有一样的名称
        LambdaQueryWrapper<SysDict> dictNameWrapper = new LambdaQueryWrapper<>();
        dictNameWrapper.eq(SysDict::getDictTypeCode, dictRequest.getDictTypeCode());
        dictNameWrapper.eq(SysDict::getDictName, dictRequest.getDictName());
        if (editFlag) {
            dictNameWrapper.ne(SysDict::getDictId, dictRequest.getDictId());
        }
        dictNameWrapper.ne(SysDict::getDelFlag, YesOrNotEnum.Y.getCode());
        long dictNameCount = this.count(dictNameWrapper);
        if (dictNameCount > 0) {
            throw new DictException(DictExceptionEnum.DICT_NAME_REPEAT, dictRequest.getDictTypeCode(), dictRequest.getDictCode());
        }

    }

}
