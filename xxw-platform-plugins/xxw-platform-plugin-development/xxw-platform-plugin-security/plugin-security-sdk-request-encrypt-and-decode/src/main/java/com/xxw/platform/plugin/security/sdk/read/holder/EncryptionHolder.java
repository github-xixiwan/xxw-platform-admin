package com.xxw.platform.plugin.security.sdk.read.holder;

/**
 * 用于存储响应加密秘钥
 *
 * @author liaoxiting
 * @date 2021/3/23 12:54
 */
public class EncryptionHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置
     *
     * @param aesKey aesKey
     * @date 2020/8/24
     */
    public static void setAesKey(String aesKey) {
        CONTEXT_HOLDER.set(aesKey);
    }

    /**
     * 获取
     *
     * @author liaoxiting
     * @date 2020/8/24
     */
    public static String getAesKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除
     *
     * @author liaoxiting
     * @date 2020/8/24
     */
    public static void clearAesKey() {
        CONTEXT_HOLDER.remove();
    }

}
