package com.xxw.platform.plugin.auth.sdk.callback;

import com.xxw.platform.frame.common.tenant.RequestTenantCodeHolder;
import com.xxw.platform.frame.common.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除参数缓存相关的ThreadLocal
 *
 * @author fengshuonan
 * @date 2021/10/29 11:37
 */
@Component
public class TenantCodeRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        RequestTenantCodeHolder.clearTenantCode();
    }

}
