package com.xxw.platform.frame.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * ant风格资源过滤工具
 *
 * @author liaoxiting
 * @date 2020/12/15 22:31
 */
@Slf4j
public class AntPathMatcherUtil {

    /**
     * 判断某个接口是否在一组ant资源表达式下匹配
     *
     * @param requestURI  请求的url
     * @param antPatterns ant风格资源表达式
     * @author liaoxiting
     * @date 2020/12/15 22:31
     */
    public static Boolean getAntMatchFLag(String requestURI, String contextPath, List<String> antPatterns) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String notAuthResourcePattern : antPatterns) {
            if (antPathMatcher.match(contextPath + notAuthResourcePattern, requestURI)) {
                return true;
            }
        }
        return false;
    }

}
