/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
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
 * @author fengshuonan
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
     * @author fengshuonan
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
     * @author fengshuonan
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
     * @author fengshuonan
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


