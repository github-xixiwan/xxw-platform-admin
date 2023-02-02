package com.xxw.platform.plugin.auth.api.context;

import com.xxw.platform.frame.common.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除当前登录用户相关的ThreadLocalHolder
 *
 * @author fengshuonan
 * @date 2021/10/29 11:41
 */
@Component
public class LoginUserRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        LoginUserHolder.remove();
    }

}
