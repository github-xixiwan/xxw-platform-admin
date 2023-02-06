package com.xxw.platform.plugin.pinyin.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.pinyin.api.PinYinApi;

/**
 * 拼音工具类快速获取
 *
 * @author liaoxiting
 * @date 2020/12/4 9:31
 */
public class PinyinContext {

    /**
     * 获取拼音工具类
     *
     * @author liaoxiting
     * @date 2020/12/4 9:36
     */
    public static PinYinApi me() {
        return SpringUtil.getBean(PinYinApi.class);
    }

}
