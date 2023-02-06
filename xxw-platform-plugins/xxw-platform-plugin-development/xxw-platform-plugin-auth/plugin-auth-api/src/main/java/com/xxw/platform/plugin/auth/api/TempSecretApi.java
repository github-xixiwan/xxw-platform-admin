package com.xxw.platform.plugin.auth.api;

/**
 * 临时用户秘钥获取
 *
 * @author liaoxiting
 * @date 2022/3/26 14:05
 */
public interface TempSecretApi {

    /**
     * 获取用户临时秘钥
     *
     * @author liaoxiting
     * @date 2022/3/26 14:07
     */
    String getUserTempSecretKey(Long userId);

}
