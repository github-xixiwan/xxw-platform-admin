package com.xxw.platform.plugin.validator.api.context;

import com.xxw.platform.frame.common.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除参数缓存相关的ThreadLocal
 *
 * @author liaoxiting
 * @date 2021/10/29 11:37
 */
@Component
public class RequestRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        RequestGroupContext.clear();
        RequestParamContext.clear();
    }

}
