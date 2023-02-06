package com.xxw.platform.plugin.jwt.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.jwt.api.JwtApi;

/**
 * Jwt工具的context，获取容器中的jwt工具类
 *
 * @author liaoxiting
 * @date 2020/10/21 14:07
 */
public class JwtContext {

    /**
     * 获取jwt操作接口
     *
     * @author liaoxiting
     * @date 2020/10/21 14:07
     */
    public static JwtApi me() {
        return SpringUtil.getBean(JwtApi.class);
    }

}
