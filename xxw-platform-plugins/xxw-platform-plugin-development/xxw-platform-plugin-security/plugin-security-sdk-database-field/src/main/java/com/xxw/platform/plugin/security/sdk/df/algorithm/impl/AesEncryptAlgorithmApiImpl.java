package com.xxw.platform.plugin.security.sdk.df.algorithm.impl;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.xxw.platform.plugin.security.api.expander.SecurityConfigExpander;
import com.xxw.platform.plugin.security.sdk.df.algorithm.EncryptAlgorithmApi;

import java.nio.charset.StandardCharsets;

/**
 * AES 加密解密实现
 *
 * @author liaoxiting
 * @date 2021/7/3 11:43
 */
public class AesEncryptAlgorithmApiImpl implements EncryptAlgorithmApi {

    @Override
    public String encrypt(String encryptedData) {
        SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, SecurityConfigExpander.getEncryptSecretKey().getBytes(StandardCharsets.UTF_8));
        return symmetricCrypto.encryptHex(encryptedData);
    }

    @Override
    public String decrypt(String cipher) {
        SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, SecurityConfigExpander.getEncryptSecretKey().getBytes(StandardCharsets.UTF_8));
        return symmetricCrypto.decryptStr(cipher);
    }
}
