package com.xxw.platform.plugin.file.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.file.api.FileOperatorApi;

/**
 * 文件操作api的上下文
 *
 * @author liaoxiting
 * @date 2020/10/26 10:30
 */
public class FileContext {

    /**
     * 获取文件操作接口
     *
     * @author liaoxiting
     * @date 2020/10/17 14:30
     */
    public static FileOperatorApi me() {
        return SpringUtil.getBean(FileOperatorApi.class);
    }

}
