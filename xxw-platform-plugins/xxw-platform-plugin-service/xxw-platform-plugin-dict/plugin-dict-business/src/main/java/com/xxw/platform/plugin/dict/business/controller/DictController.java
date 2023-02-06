package com.xxw.platform.plugin.dict.business.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.api.constants.DictConstants;
import com.xxw.platform.plugin.dict.business.entity.SysDict;
import com.xxw.platform.plugin.dict.business.pojo.TreeDictInfo;
import com.xxw.platform.plugin.dict.business.pojo.request.DictRequest;
import com.xxw.platform.plugin.dict.business.service.DictService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典详情管理，具体管理某个字典类型下的条目
 *
 * @author liaoxiting
 * @date 2020/10/29 14:45
 */
@RestController
@ApiResource(name = "字典详情管理", resBizType = ResBizTypeEnum.SYSTEM)
public class DictController {

    @Resource
    private DictService dictService;

    /**
     * 添加字典条目
     *
     * @author liaoxiting
     * @date 2020/10/29 16:35
     */
    @PostResource(name = "添加字典", path = "/dict/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(DictRequest.add.class) DictRequest dictRequest) {
        this.dictService.add(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除字典条目
     *
     * @author liaoxiting
     * @date 2020/10/29 16:35
     */
    @PostResource(name = "删除字典", path = "/dict/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(DictRequest.delete.class) DictRequest dictRequest) {
        this.dictService.del(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改字典条目
     *
     * @author liaoxiting
     * @date 2020/10/29 16:35
     */
    @PostResource(name = "修改字典", path = "/dict/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(DictRequest.edit.class) DictRequest dictRequest) {
        this.dictService.edit(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取字典详情
     *
     * @author liaoxiting
     * @date 2020/10/29 16:35
     */
    @GetResource(name = "获取字典详情", path = "/dict/detail", requiredPermission = false)
    public ResponseData<SysDict> detail(@Validated(BaseRequest.detail.class) DictRequest dictRequest) {
        SysDict detail = this.dictService.detail(dictRequest);
        return new SuccessResponseData<>(detail);
    }

    /**
     * 获取字典列表
     *
     * @author liaoxiting
     * @date 2020/10/29 16:35
     */
    @GetResource(name = "获取字典列表", path = "/dict/list", requiredPermission = false)
    public ResponseData<List<SysDict>> list(DictRequest dictRequest) {
        return new SuccessResponseData<>(this.dictService.findList(dictRequest));
    }

    /**
     * 获取字典列表(分页)
     *
     * @author liaoxiting
     * @date 2020/10/29 16:35
     */
    @GetResource(name = "获取字典列表", path = "/dict/page", requiredPermission = false)
    public ResponseData<PageResult<SysDict>> page(DictRequest dictRequest) {
        return new SuccessResponseData<>(this.dictService.findPage(dictRequest));
    }
    /**
     * 获取树形字典列表（antdv在用）
     *
     * @author liaoxiting
     * @date 2020/10/29 16:36
     */
    @GetResource(name = "获取树形字典列表", path = "/dict/getDictTreeList", requiredPermission = false)
    public ResponseData<List<TreeDictInfo>> getDictTreeList(@Validated(DictRequest.treeList.class) DictRequest dictRequest) {
        List<TreeDictInfo> treeDictList = this.dictService.getTreeDictList(dictRequest);
        return new SuccessResponseData<>(treeDictList);
    }

    /**
     * 获取系统配置分组字典列表(分页)（给系统配置界面，左侧获取配置的分类用）
     *
     * @author liaoxiting
     * @date 2021/1/25 11:47
     */
    @GetResource(name = "获取系统配置分组字典列表", path = "/dict/getConfigGroupPage", requiredPermission = false)
    public ResponseData<PageResult<SysDict>> getConfigGroupPage(DictRequest dictRequest) {
        dictRequest.setDictTypeCode(DictConstants.CONFIG_GROUP_DICT_TYPE_CODE);
        PageResult<SysDict> page = this.dictService.findPage(dictRequest);
        return new SuccessResponseData<>(page);
    }

    /**
     * 获取多语言字典列表(分页)（给多语言界面，左侧获取多语言的分类用）
     *
     * @author liaoxiting
     * @date 2021/1/25 11:47
     */
    @GetResource(name = "获取多语言字典列表", path = "/dict/getLanguagesPage", requiredPermission = false)
    public ResponseData<PageResult<SysDict>> getLanguagesPage(DictRequest dictRequest) {
        dictRequest.setDictTypeCode(DictConstants.LANGUAGES_DICT_TYPE_CODE);
        PageResult<SysDict> page = this.dictService.findPage(dictRequest);
        return new SuccessResponseData<>(page);
    }

}
