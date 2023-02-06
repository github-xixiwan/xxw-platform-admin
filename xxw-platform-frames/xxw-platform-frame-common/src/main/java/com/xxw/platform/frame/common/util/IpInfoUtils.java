package com.xxw.platform.frame.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP工具类
 *
 * @author liaoxiting
 * @date 2021/1/10 14:25
 */
public class IpInfoUtils {

    /**
     * 获取当前机器的hostname
     *
     * @author liaoxiting
     * @date 2021/1/10 18:40
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
            return "未知";
        }
    }

}
