package com.xxw.platform.plugin.auth.api.pojo.auth;

import cn.hutool.crypto.asymmetric.RSA;
import lombok.Data;

/**
 * 用在系统登录，密码加密的RSA非对称加密秘钥对
 *
 * @author liaoxiting
 * @date 2022/10/16 15:28
 */
@Data
public class PwdRsaSecretProperties {

    /**
     * RSA非对称加密，公钥
     */
    private String publicKey;

    /**
     * RSA非对称加密，私钥
     */
    private String privateKey;

    public static void main(String[] args) {
        // 测试生成秘钥对
        RSA rsa = new RSA();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();

        System.out.println(publicKeyBase64);
        System.out.println(privateKeyBase64);
    }

}
