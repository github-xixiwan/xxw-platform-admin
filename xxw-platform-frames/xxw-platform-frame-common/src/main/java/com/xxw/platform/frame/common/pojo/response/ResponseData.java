package com.xxw.platform.frame.common.pojo.response;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

/**
 * http响应结果封装
 *
 * @author liaoxiting
 * @date 2020/10/17 17:33
 */
@Data
public class ResponseData<T> {

    /**
     * 请求是否成功
     */
    @ChineseDescription("请求是否成功")
    private Boolean success;

    /**
     * 响应状态码
     */
    @ChineseDescription("响应状态码")
    private String code;

    /**
     * 响应信息
     */
    @ChineseDescription("响应信息")
    private String message;

    /**
     * 响应对象
     */
    @ChineseDescription("响应对象")
    private T data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
