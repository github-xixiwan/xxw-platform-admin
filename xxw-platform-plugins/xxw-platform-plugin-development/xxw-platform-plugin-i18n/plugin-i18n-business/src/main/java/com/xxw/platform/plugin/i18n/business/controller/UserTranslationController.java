package com.xxw.platform.plugin.i18n.business.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.dict.api.DictApi;
import com.xxw.platform.plugin.dict.api.constants.DictConstants;
import com.xxw.platform.plugin.i18n.api.context.TranslationContext;
import com.xxw.platform.plugin.i18n.api.pojo.request.TranslationRequest;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户多语言信息控制器
 *
 * @author liaoxiting
 * @date 2021/1/27 21:59
 */
@RestController
@ApiResource(name = "用户多语言信息控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class UserTranslationController {

    @Resource
    private DictApi dictApi;

    @Resource
    private SessionManagerApi sessionManagerApi;

    /**
     * 获取所有的多语言类型编码
     *
     * @author liaoxiting
     * @date 2021/1/24 19:20
     */
    @GetResource(name = "获取所有的多语言类型编码", path = "/i18n/getAllLanguages", requiredPermission = false)
    public ResponseData<List<SimpleDict>> getAllLanguages() {
        List<SimpleDict> dictDetailsByDictTypeCode = dictApi.getDictDetailsByDictTypeCode(DictConstants.LANGUAGES_DICT_TYPE_CODE);
        return new SuccessResponseData<>(dictDetailsByDictTypeCode);
    }

    /**
     * 获取当前用户的多语言字典
     *
     * @author liaoxiting
     * @date 2021/1/27 22:00
     */
    @GetResource(name = "获取当前用户的多语言字典", path = "/i18n/getUserTranslation", requiredPermission = false)
    public ResponseData<Map<String, String>> getUserTranslation() {
        String tranLanguageCode = LoginContext.me().getLoginUser().getTranLanguageCode();
        Map<String, String> translationDictByLanguage = TranslationContext.me().getTranslationDictByLanguage(tranLanguageCode);
        return new SuccessResponseData<>(translationDictByLanguage);
    }

    /**
     * 修改当前用户的多语言配置
     *
     * @author liaoxiting
     * @date 2021/1/27 22:04
     */
    @PostResource(name = "修改当前用户的多语言配置", path = "/i18n/changeUserTranslation", requiredPermission = false)
    public ResponseData<?> changeUserTranslation(@RequestBody @Validated(TranslationRequest.changeUserLanguage.class) TranslationRequest translationRequest) {

        String token = LoginContext.me().getToken();
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 更新当前用户的多语言配置
        loginUser.setTranLanguageCode(translationRequest.getTranLanguageCode());
        sessionManagerApi.updateSession(token, loginUser);

        return new SuccessResponseData<>();
    }

}
