package com.xxw.platform.plugin.jwt.api.expander;

import cn.hutool.core.util.RandomUtil;
import com.xxw.platform.plugin.config.api.context.ConfigContext;
import com.xxw.platform.plugin.jwt.api.constants.JwtConstants;

/**
 * jwt工具类的配置获取
 *
 * @author liaoxiting
 * @date 2020/12/1 15:05
 */
public class JwtConfigExpander {

    /**
     * 获取jwt的密钥
     *
     * @author liaoxiting
     * @date 2020/12/1 15:07
     */
    public static String getJwtSecret() {
        String sysJwtSecret = ConfigContext.me().getConfigValueNullable("SYS_JWT_SECRET", String.class);

        // 没配置就返回一个随机密码
        if (sysJwtSecret == null) {
            return RandomUtil.randomString(20);
        } else {
            return sysJwtSecret;
        }
    }

    /**
     * jwt失效时间，默认1天
     *
     * @author liaoxiting
     * @date 2020/12/1 15:08
     */
    public static Long getJwtTimeoutSeconds() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_JWT_TIMEOUT_SECONDS", Long.class, JwtConstants.DEFAULT_JWT_TIMEOUT_SECONDS);
    }

}
