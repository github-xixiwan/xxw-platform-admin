package com.xxw.platform.plugin.system.business.user.service;

import cn.hutool.core.util.ObjectUtil;
import com.xxw.platform.frame.common.tenant.OnceTenantCodeHolder;
import com.xxw.platform.frame.common.tenant.RequestTenantCodeHolder;
import com.xxw.platform.frame.common.tenant.TenantPrefixApi;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import org.springframework.stereotype.Service;

/**
 * 获取当前登录用户的租户code
 *
 * @author liaoxiting
 * @date 2022/11/7 21:01
 */
@Service
public class TenantPrefixService implements TenantPrefixApi {

    @Override
    public String getTenantPrefix() {

        // 1. 优先从OnceTenantCodeHolder中获取租户缓存编码
        String tenantCode = OnceTenantCodeHolder.getTenantCode();

        // 如果有则以ThreadLocal中为准
        if (ObjectUtil.isNotEmpty(tenantCode)) {
            return tenantCode;
        }

        // 2. 次之，从RequestTenantCodeHolder中获取租户缓存编码，一般用在登录接口时，系统还没有LoginUser
        String requestHttpContextTenantCode = RequestTenantCodeHolder.getTenantCode();
        if (ObjectUtil.isNotEmpty(requestHttpContextTenantCode)) {
            return requestHttpContextTenantCode;
        }

        // 3. 最后，从LoginUser中获取租户编码
        LoginUser loginUser = LoginContext.me().getLoginUserNullable();

        if (loginUser == null) {
            return null;
        }

        return loginUser.getTenantCode();
    }

}
