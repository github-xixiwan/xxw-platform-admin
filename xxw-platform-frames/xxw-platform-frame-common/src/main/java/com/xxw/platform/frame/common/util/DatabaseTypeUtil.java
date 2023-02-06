package com.xxw.platform.frame.common.util;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.enums.DbTypeEnum;

/**
 * 判断数据库类型的工具
 *
 * @author liaoxiting
 * @date 2021/3/27 21:24
 */
public class DatabaseTypeUtil {

    /**
     * 判断数据库类型
     *
     * @author liaoxiting
     * @date 2021/3/27 21:25
     */
    public static DbTypeEnum getDbType(String jdbcUrl) {
        if (StrUtil.isEmpty(jdbcUrl)) {
            return DbTypeEnum.MYSQL;
        }

        // url字符串中包含了dbTypeEnum的name，则判定为该类型
        for (DbTypeEnum dbTypeEnum : DbTypeEnum.values()) {
            if (jdbcUrl.contains(dbTypeEnum.getUrlWords())) {
                return dbTypeEnum;
            }
        }

        return DbTypeEnum.MYSQL;
    }

}
