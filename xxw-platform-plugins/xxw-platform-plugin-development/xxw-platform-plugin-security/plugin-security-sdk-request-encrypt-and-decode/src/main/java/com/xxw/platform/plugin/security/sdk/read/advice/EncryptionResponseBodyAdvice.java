package com.xxw.platform.plugin.security.sdk.read.advice;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson2.JSON;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.security.sdk.read.holder.EncryptionHolder;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.security.Security;
import java.util.Date;

/**
 * 响应结果加密
 *
 * @author liaoxiting
 * @date 2021/3/23 12:54
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("all")
public class EncryptionResponseBodyAdvice implements ResponseBodyAdvice {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            // 添加PKCS7Padding支持
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null != body) {

            PostResource annotation = returnType.getMethod().getAnnotation(PostResource.class);

            if (annotation != null) {
                if (annotation.requiredEncryption()) {
                    // 判断响应实体是否是 ResponseData
                    if (body instanceof ResponseData) {
                        // 转换类型
                        ResponseData responseData = (ResponseData) body;

                        Object data = responseData.getData();

                        // 将返回的数据格式化成json字符串
                        String respJsonStr = JSON.toJSONString(data);

                        // 从 ThreadLocal 中获取 aes key
                        String aesKey = EncryptionHolder.getAesKey();

                        // 偏移
                        byte[] iv = HexUtil.decodeHex(SecureUtil.md5(StrUtil.format("{}{}", aesKey, DateUtil.format(new Date(), "yyyyMMdd"))));

                        if (StrUtil.isNotBlank(aesKey)) {

                            byte[] keyByte = Base64.decode(aesKey.getBytes(CharsetUtil.CHARSET_UTF_8));

                            // AES
                            AES aes = new AES("CFB", "PKCS7Padding", keyByte, iv);
                            String encryptBase64 = aes.encryptBase64(respJsonStr);

                            responseData.setData(encryptBase64);

                        }

                        // 清除当前线程中的aes key
                        EncryptionHolder.clearAesKey();

                        return responseData;

                    }
                }
            }

        }
        return body;
    }
}
