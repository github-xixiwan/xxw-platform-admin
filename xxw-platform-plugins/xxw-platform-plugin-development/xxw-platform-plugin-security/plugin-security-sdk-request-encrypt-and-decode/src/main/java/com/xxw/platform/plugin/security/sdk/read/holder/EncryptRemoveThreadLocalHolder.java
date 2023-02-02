package com.xxw.platform.plugin.security.sdk.read.holder;

import com.xxw.platform.frame.common.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除加解密的相关ThreadLocal
 *
 * @author fengshuonan
 * @date 2021/10/29 11:37
 */
@Component
public class EncryptRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        EncryptionHolder.clearAesKey();
    }

}
