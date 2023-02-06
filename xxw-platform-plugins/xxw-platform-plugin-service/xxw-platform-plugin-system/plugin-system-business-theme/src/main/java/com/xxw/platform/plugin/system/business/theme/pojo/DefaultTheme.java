package com.xxw.platform.plugin.system.business.theme.pojo;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.util.Map;

/**
 * Xxw默认主题控制的字段
 *
 * @author liaoxiting
 * @date 2022/1/10 18:30
 */
@Data
public class DefaultTheme {

    /**
     * 平台名称
     */
    @ChineseDescription("平台名称")
    private String xxwMgrName;

    /**
     * 登录页背景图片
     */
    @ChineseDescription("登录页背景图片")
    private String xxwMgrLoginBackgroundImg;

    /**
     * 平台LOGO
     */
    @ChineseDescription("平台LOGO")
    private String xxwMgrLogo;

    /**
     * 浏览器Icon
     */
    @ChineseDescription("浏览器Icon")
    private String xxwMgrFavicon;

    /**
     * 页脚文字
     */
    @ChineseDescription("页脚文字")
    private String xxwMgrFooterText;

    /**
     * 备案号
     */
    @ChineseDescription("备案号")
    private String xxwMgrBeiNo;

    /**
     * 备案号跳转链接
     */
    @ChineseDescription("备案号跳转链接")
    private String xxwMgrBeiUrl;

    /**
     * 其他的主题配置
     */
    @ChineseDescription("其他的主题配置")
    private Map<String, String> otherConfigs;

}
