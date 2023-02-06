package com.xxw.platform.frame.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Token签名工具
 *
 * @author liaoxiting
 * @date 2022/8/1 17:41
 */
@Slf4j
public class TokenSignUtil {

    /**
     * 根据时间戳和秘钥生成md5方式的签名字符串
     *
     * @param timestamp 生成sign时的时间戳
     * @param secretKey 生成时间戳需要的秘钥
     * @author liaoxiting
     * @date 2022/8/1 17:43
     */
    public static String createSignStr(Long timestamp, String secretKey) {
        if (ObjectUtil.isEmpty(timestamp) || ObjectUtil.isEmpty(secretKey)) {
            return null;
        } else {
            return SecureUtil.md5(timestamp + secretKey);
        }
    }

    /**
     * 验证签名是否过期
     *
     * @param timestamp      生成签名时候的时间戳
     * @param secretKey      秘钥
     * @param signStr        签名字符串
     * @param expiredSeconds 签名过期时间
     * @author liaoxiting
     * @date 2022/8/1 17:48
     */
    public static boolean validateSignStr(Long timestamp, String secretKey, String signStr, Integer expiredSeconds) {

        if (ObjectUtil.isEmpty(timestamp) || ObjectUtil.isEmpty(secretKey) || ObjectUtil.isEmpty(signStr) || ObjectUtil.isEmpty(expiredSeconds)) {
            return false;
        }

        long betweenSeconds = (System.currentTimeMillis() - timestamp) / 1000;

        // 如果当前时间和时间戳时间相隔太长，则返回校验失败，过期
        if (betweenSeconds > expiredSeconds) {
            return false;
        }

        // 正确的签名
        String signRight = TokenSignUtil.createSignStr(timestamp, secretKey);

        // 比对签名
        if (signRight.equals(signStr)) {
            return true;
        }

        return false;
    }

}
