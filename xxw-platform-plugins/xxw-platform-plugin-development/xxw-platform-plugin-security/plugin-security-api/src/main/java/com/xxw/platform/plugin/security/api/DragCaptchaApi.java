package com.xxw.platform.plugin.security.api;

import com.xxw.platform.plugin.security.api.pojo.DragCaptchaImageDTO;

/**
 * 拖拽验证码
 *
 * @author liaoxiting
 * @date 2021/7/5 12:05
 */
public interface DragCaptchaApi {

    /**
     * 生成拖拽验证码的返回值
     *
     * @author liaoxiting
     * @date 2021/7/5 11:55
     */
    DragCaptchaImageDTO createCaptcha();

    /**
     * 验证拖拽验证码
     *
     * @author liaoxiting
     * @date 2021/7/5 11:55
     */
    boolean validateCaptcha(String verKey, Integer verCode);

}
