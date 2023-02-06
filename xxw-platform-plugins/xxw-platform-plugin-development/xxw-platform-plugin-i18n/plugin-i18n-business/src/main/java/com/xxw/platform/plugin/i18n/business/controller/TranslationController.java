package com.xxw.platform.plugin.i18n.business.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.i18n.api.pojo.request.TranslationRequest;
import com.xxw.platform.plugin.i18n.business.entity.Translation;
import com.xxw.platform.plugin.i18n.business.service.TranslationService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 多语言接口控制器
 *
 * @author liaoxiting
 * @date 2021/1/24 19:18
 */
@RestController
@ApiResource(name = "多语言接口控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class TranslationController {

    @Resource
    private TranslationService translationService;

    /**
     * 新增多语言翻译记录
     *
     * @author liaoxiting
     * @date 2021/1/24 19:17
     */
    @PostResource(name = "新增多语言配置", path = "/i18n/add")
    public ResponseData<?> add(@RequestBody @Validated(TranslationRequest.add.class) TranslationRequest translationRequest) {
        this.translationService.add(translationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑多语言翻译记录
     *
     * @author liaoxiting
     * @date 2021/1/24 19:17
     */
    @PostResource(name = "新增多语言配置", path = "/i18n/edit")
    public ResponseData<?> edit(@RequestBody @Validated(BaseRequest.edit.class) TranslationRequest translationRequest) {
        this.translationService.edit(translationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除多语言配置
     *
     * @author liaoxiting
     * @date 2021/1/24 19:20
     */
    @PostResource(name = "新增多语言配置", path = "/i18n/delete")
    public ResponseData<?> delete(@RequestBody @Validated(BaseRequest.delete.class) TranslationRequest translationRequest) {
        this.translationService.del(translationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除某个语种
     *
     * @author liaoxiting
     * @date 2021/1/24 19:20
     */
    @PostResource(name = "删除某个语种", path = "/i18n/deleteTranLanguage")
    public ResponseData<?> deleteTranLanguage(@RequestBody @Validated(TranslationRequest.deleteTranLanguage.class) TranslationRequest translationRequest) {
        this.translationService.deleteTranLanguage(translationRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看多语言详情
     *
     * @author liaoxiting
     * @date 2021/1/24 19:20
     */
    @GetResource(name = "新增多语言配置", path = "/i18n/detail")
    public ResponseData<Translation> detail(@Validated(BaseRequest.detail.class) TranslationRequest translationRequest) {
        Translation detail = this.translationService.detail(translationRequest);
        return new SuccessResponseData<>(detail);
    }

    /**
     * 查看多语言配置列表
     *
     * @author liaoxiting
     * @date 2021/1/24 19:20
     */
    @GetResource(name = "新增多语言配置", path = "/i18n/page")
    public ResponseData<PageResult<Translation>> page(TranslationRequest translationRequest) {
        PageResult<Translation> page = this.translationService.findPage(translationRequest);
        return new SuccessResponseData<>(page);
    }

}
