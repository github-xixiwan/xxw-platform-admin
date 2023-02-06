package com.xxw.platform.frame.common.pojo.response;

import com.xxw.platform.frame.common.constants.RuleConstants;

/**
 * 响应成功的封装类
 *
 * @author liaoxiting
 * @date 2020/10/16 16:23
 */
public class SuccessResponseData<T> extends ResponseData<T> {

    public SuccessResponseData() {
        super(Boolean.TRUE, RuleConstants.SUCCESS_CODE, RuleConstants.SUCCESS_MESSAGE, null);
    }

    public SuccessResponseData(T object) {
        super(Boolean.TRUE, RuleConstants.SUCCESS_CODE, RuleConstants.SUCCESS_MESSAGE, object);
    }

    public SuccessResponseData(String code, String message, T object) {
        super(Boolean.TRUE, code, message, object);
    }
}
