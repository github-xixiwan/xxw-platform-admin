package com.xxw.platform.plugin.i18n.sdk.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.listener.ApplicationStartedListener;
import com.xxw.platform.plugin.i18n.api.TranslationApi;
import com.xxw.platform.plugin.i18n.api.TranslationPersistenceApi;
import com.xxw.platform.plugin.i18n.api.pojo.TranslationDict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;

import java.util.List;

/**
 * 初始化多语言翻译详
 *
 * @author liaoxiting
 * @date 2021/1/24 19:36
 */
@Slf4j
public class TranslationDictInitListener extends ApplicationStartedListener {

    @Override
    public void eventCallback(ApplicationStartedEvent event) {

        TranslationPersistenceApi tanTranslationPersistenceApi = SpringUtil.getBean(TranslationPersistenceApi.class);
        TranslationApi translationApi = SpringUtil.getBean(TranslationApi.class);

        // 从数据库读取翻译字典
        List<TranslationDict> allTranslationDict = tanTranslationPersistenceApi.getAllTranslationDict();
        if (allTranslationDict != null) {
            translationApi.init(allTranslationDict);
            log.info("初始化所有的翻译字典" + allTranslationDict.size() + "条！");
        }
    }

}
