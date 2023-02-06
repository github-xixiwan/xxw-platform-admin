package com.xxw.platform.plugin.security.sdk.captcha;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.SpecCaptcha;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.security.api.ImageCaptchaApi;
import com.xxw.platform.plugin.security.api.pojo.ImageCaptcha;

/**
 * 图形验证码实现
 *
 * @author liaoxiting
 * @date 2021/1/15 13:44
 */
public class ImageCaptchaService implements ImageCaptchaApi {

    private final CacheOperatorApi<String> cacheOperatorApi;

    public ImageCaptchaService(CacheOperatorApi<String> cacheOperatorApi) {
        this.cacheOperatorApi = cacheOperatorApi;
    }

    @Override
    public ImageCaptcha captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String verKey = IdUtil.simpleUUID();
        cacheOperatorApi.put(verKey, verCode);
        return ImageCaptcha.builder().verImage(specCaptcha.toBase64()).verKey(verKey).build();
    }

    @Override
    public boolean validateCaptcha(String verKey, String verCode) {
        if (StrUtil.isAllEmpty(verKey, verCode)) {
            return false;
        }

        if (!verCode.trim().toLowerCase().equals(cacheOperatorApi.get(verKey))) {
            return false;
        }

        //删除缓存中验证码
        cacheOperatorApi.remove(verKey);

        return true;
    }

    @Override
    public String getVerCode(String verKey) {
        return cacheOperatorApi.get(verKey);
    }

}
