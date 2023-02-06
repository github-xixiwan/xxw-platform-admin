package com.xxw.platform.plugin.dict.business.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.api.constants.DictConstants;
import com.xxw.platform.plugin.dict.business.entity.SysDictType;
import com.xxw.platform.plugin.dict.business.pojo.request.DictTypeRequest;
import com.xxw.platform.plugin.dict.business.service.DictTypeService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型管理
 *
 * @author liaoxiting
 * @date 2020/10/30 21:46
 */
@RestController
@ApiResource(name = "字典类型管理", resBizType = ResBizTypeEnum.SYSTEM)
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    /**
     * 添加字典类型
     *
     * @author liaoxiting
     * @date 2018/7/25 下午12:36
     */
    @PostResource(name = "添加字典类型", path = "/dictType/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(DictTypeRequest.add.class) DictTypeRequest dictTypeRequest) {
        this.dictTypeService.add(dictTypeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除字典类型
     *
     * @author liaoxiting
     * @date 2018/7/25 下午12:36
     */
    @PostResource(name = "删除字典类型", path = "/dictType/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(DictTypeRequest.delete.class) DictTypeRequest dictTypeRequest) {
        this.dictTypeService.del(dictTypeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改字典类型
     *
     * @author liaoxiting
     * @date 2018/7/25 下午12:36
     */
    @PostResource(name = "修改字典类型", path = "/dictType/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(DictTypeRequest.edit.class) DictTypeRequest dictTypeRequest) {
        this.dictTypeService.edit(dictTypeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改字典类型状态
     *
     * @author liaoxiting
     * @date 2018/7/25 下午12:36
     */
    @PostResource(name = "修改字典类型状态", path = "/dictType/updateStatus")
    @BusinessLog
    public ResponseData<?> updateStatus(@RequestBody @Validated(BaseRequest.updateStatus.class) DictTypeRequest dictTypeRequest) {
        this.dictTypeService.editStatus(dictTypeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取字典类型详情
     *
     * @author liaoxiting
     * @date 2021/1/13 11:25
     */
    @GetResource(name = "获取字典类型详情", path = "/dictType/detail", requiredPermission = false)
    public ResponseData<SysDictType> detail(@Validated(BaseRequest.detail.class) DictTypeRequest dictTypeRequest) {
        SysDictType detail = this.dictTypeService.detail(dictTypeRequest);
        return new SuccessResponseData<>(detail);
    }

    /**
     * 获取字典类型列表
     *
     * @author liaoxiting
     * @date 2020/10/30 21:46
     */
    @GetResource(name = "获取字典类型列表", path = "/dictType/list", requiredPermission = false)
    public ResponseData<List<SysDictType>> list(DictTypeRequest dictTypeRequest) {
        return new SuccessResponseData<>(dictTypeService.findList(dictTypeRequest));
    }

    /**
     * 获取字典类型列表(分页)
     *
     * @author liaoxiting
     * @date 2020/10/30 21:46
     */
    @GetResource(name = "获取字典类型列表(分页)", path = "/dictType/page", requiredPermission = false)
    public ResponseData<PageResult<SysDictType>> page(DictTypeRequest dictTypeRequest) {
        return new SuccessResponseData<>(dictTypeService.findPage(dictTypeRequest));
    }

    /**
     * 获取字典类型详情
     *
     * @author liaoxiting
     * @date 2021/1/13 11:25
     */
    @GetResource(name = "获取系统配置字典类型详情", path = "/dictType/getConfigDictTypeDetail", requiredPermission = false)
    public ResponseData<SysDictType> getConfigDictTypeDetail(DictTypeRequest dictTypeRequest) {
        dictTypeRequest.setDictTypeCode(DictConstants.CONFIG_GROUP_DICT_TYPE_CODE);
        SysDictType detail = this.dictTypeService.detail(dictTypeRequest);
        return new SuccessResponseData<>(detail);
    }

    /**
     * 获取字典类型详情
     *
     * @author liaoxiting
     * @date 2021/1/13 11:25
     */
    @GetResource(name = "获取语种字典类型型详情", path = "/dictType/getTranslationDetail", requiredPermission = false)
    public ResponseData<SysDictType> getTranslationDetail(DictTypeRequest dictTypeRequest) {
        dictTypeRequest.setDictTypeCode(DictConstants.LANGUAGES_DICT_TYPE_CODE);
        SysDictType detail = this.dictTypeService.detail(dictTypeRequest);
        return new SuccessResponseData<>(detail);
    }

}
