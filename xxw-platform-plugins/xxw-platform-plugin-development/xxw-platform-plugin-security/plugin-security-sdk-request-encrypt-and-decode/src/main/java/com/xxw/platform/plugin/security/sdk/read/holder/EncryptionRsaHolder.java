package com.xxw.platform.plugin.security.sdk.read.holder;

import cn.hutool.crypto.asymmetric.RSA;
import com.xxw.platform.plugin.security.sdk.read.constants.EncryptionConstants;

/**
 * 用于存储RSA实例
 *
 * @author liaoxiting
 * @date 2021/6/4 08:58
 */
public class EncryptionRsaHolder {

    public static RSA STATIC_RSA = new RSA(EncryptionConstants.PRIVATE_KEY, EncryptionConstants.PUBLIC_KEY);

}
