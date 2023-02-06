package com.xxw.platform.plugin.db.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.db.api.DbOperatorApi;

/**
 * 获取sql操作器
 *
 * @author liaoxiting
 * @date 2020/11/4 15:07
 */
public class DbOperatorContext {

    public static DbOperatorApi me() {
        return SpringUtil.getBean(DbOperatorApi.class);
    }

}
