package com.xxw.platform.plugin.system.business.user.controller;

import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.security.api.DragCaptchaApi;
import com.xxw.platform.plugin.security.api.ImageCaptchaApi;
import com.xxw.platform.plugin.security.api.pojo.DragCaptchaImageDTO;
import com.xxw.platform.plugin.security.api.pojo.ImageCaptcha;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 图形验证码
 *
 * @author liaoxiting
 * @date 2021/1/15 15:11
 */
@RestController
@ApiResource(name = "用户登录图形验证码", resBizType = ResBizTypeEnum.SYSTEM)
public class KaptchaController {

    @Resource
    private ImageCaptchaApi captchaApi;

    @Resource
    private DragCaptchaApi dragCaptchaApi;

    /**
     * 获取图形验证码
     *
     * @author liaoxiting
     * @date 2021/7/5 12:00
     */
    @GetResource(name = "获取图形验证码", path = "/captcha", requiredPermission = false, requiredLogin = false)
    public ResponseData<ImageCaptcha> captcha() {
        return new SuccessResponseData<>(captchaApi.captcha());
    }

    /**
     * 获取拖拽验证码
     *
     * @author liaoxiting
     * @date 2021/7/5 12:00
     */
    @GetResource(name = "获取图形验证码", path = "/dragCaptcha", requiredPermission = false, requiredLogin = false)
    public ResponseData<DragCaptchaImageDTO> dragCaptcha() {
        DragCaptchaImageDTO captcha = dragCaptchaApi.createCaptcha();
        captcha.setSrcImage(RuleConstants.BASE64_IMG_PREFIX + captcha.getSrcImage());
        captcha.setCutImage(RuleConstants.BASE64_IMG_PREFIX + captcha.getCutImage());
        return new SuccessResponseData<>(captcha);
    }

}
