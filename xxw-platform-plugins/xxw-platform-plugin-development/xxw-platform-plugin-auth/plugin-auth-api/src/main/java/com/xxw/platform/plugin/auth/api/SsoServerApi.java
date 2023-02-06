package com.xxw.platform.plugin.auth.api;

import com.xxw.platform.plugin.auth.api.pojo.sso.SsoLoginCodeRequest;

/**
 * 单点服务端相关api
 *
 * @author liaoxiting
 * @date 2022/5/16 16:53
 */
public interface SsoServerApi {

    /**
     * 校验账号密码是否正确，创建sso登录编码
     *
     * @param ssoLoginCodeRequest 账号和密码
     * @return ssoLoginCode，用在单点登录
     * @author liaoxiting
     * @date 2021/1/27 17:26
     */
    String createSsoLoginCode(SsoLoginCodeRequest ssoLoginCodeRequest);

}
