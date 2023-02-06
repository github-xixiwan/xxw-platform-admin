package com.xxw.platform.plugin.security.api.pojo;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Builder;
import lombok.Data;

/**
 * EasyCaptcha 图形验证码参数
 *
 * @author liaoxiting
 * @date 2020/8/17 21:43
 */
@Data
@Builder
public class ImageCaptcha {

    /**
     * 缓存Key
     */
    @ChineseDescription("缓存Key")
    private String verKey;

    /**
     * Base64 图形验证码
     */
    @ChineseDescription("Base64 图形验证码")
    private String verImage;

}
