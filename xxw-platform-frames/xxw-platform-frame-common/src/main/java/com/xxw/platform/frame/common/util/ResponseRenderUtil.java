package com.xxw.platform.frame.common.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import com.alibaba.fastjson2.JSON;
import com.xxw.platform.frame.common.pojo.response.ErrorResponseData;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * http响应信息的直接渲染工具
 *
 * @author liaoxiting
 * @date 2020/12/15 21:39
 */
@Slf4j
public class ResponseRenderUtil {

    /**
     * 渲染接口json信息
     *
     * @author liaoxiting
     * @date 2020/12/15 21:40
     */
    public static void renderJsonResponse(HttpServletResponse response, Object responseData) {
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(ContentType.JSON.toString());
        String errorResponseJsonData = JSON.toJSONString(responseData);
        try {
            response.getWriter().write(errorResponseJsonData);
        } catch (IOException e) {
            log.error("渲染http json信息错误！", e);
        }
    }

    /**
     * 渲染接口json信息
     *
     * @author liaoxiting
     * @date 2020/12/15 21:40
     */
    public static void renderErrorResponse(HttpServletResponse response,
                                           String code, String message, String exceptionClazz) {
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(ContentType.JSON.toString());
        ErrorResponseData errorResponseData = new ErrorResponseData(code, message);
        errorResponseData.setExceptionClazz(exceptionClazz);
        String errorResponseJsonData = JSON.toJSONString(errorResponseData);
        try {
            response.getWriter().write(errorResponseJsonData);
        } catch (IOException e) {
            log.error("渲染http json信息错误！", e);
        }
    }

    /**
     * 设置渲染文件的头
     *
     * @author liaoxiting
     * @date 2021/7/1 15:01
     */
    public static void setRenderFileHeader(HttpServletResponse response, String fileName) {
        final String charset = ObjectUtil.defaultIfNull(response.getCharacterEncoding(), CharsetUtil.UTF_8);
        response.setHeader("Content-Disposition", StrUtil.format("attachment;filename={}", URLUtil.encode(fileName, Charset.forName(charset))));
        response.setContentType("application/octet-stream; charset=utf-8");
    }

    /**
     * 设置图片的渲染文件头
     *
     * @author liaoxiting
     * @date 2021/7/1 15:01
     */
    public static void setRenderImageHeader(HttpServletResponse response) {
        response.setContentType("image/png");
    }

}
