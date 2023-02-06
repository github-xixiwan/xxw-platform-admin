package com.xxw.platform.plugin.log.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.log.api.LogRecordApi;

/**
 * 日志操作api的获取
 *
 * @author liaoxiting
 * @date 2020/10/27 16:19
 */
public class LogRecordContext {

    /**
     * 获取日志操作api
     *
     * @author liaoxiting
     * @date 2020/10/27 16:19
     */
    public static LogRecordApi me() {
        return SpringUtil.getBean(LogRecordApi.class);
    }

}
