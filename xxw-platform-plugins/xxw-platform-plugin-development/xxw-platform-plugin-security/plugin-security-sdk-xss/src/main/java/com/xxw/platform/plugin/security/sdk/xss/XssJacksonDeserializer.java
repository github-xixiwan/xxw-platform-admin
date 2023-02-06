package com.xxw.platform.plugin.security.sdk.xss;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.xxw.platform.frame.common.util.HttpServletUtil;
import com.xxw.platform.plugin.security.sdk.xss.prop.XssProperties;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 针对于jackson反序列化时，xss危险字符串的过滤
 *
 * @author liaoxiting
 * @date 2021/1/13 22:56
 */
public class XssJacksonDeserializer extends JsonDeserializer<String> {

    private final XssProperties xssProperties;

    public XssJacksonDeserializer(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String servletPath = HttpServletUtil.getRequest().getServletPath();
        String contextPath = HttpServletUtil.getRequest().getContextPath();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        // 如果当前servlet path排除在外，则放行
        if (xssProperties != null &&
                ObjectUtil.isNotEmpty(xssProperties.getUrlExclusion())) {
            for (String exclusion : xssProperties.getUrlExclusion()) {
                if (antPathMatcher.match(contextPath + exclusion, contextPath + servletPath)) {
                    return jsonParser.getText();
                }
            }
        }

        return HtmlUtil.filter(jsonParser.getText());
    }

}
