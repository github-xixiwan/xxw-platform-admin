package com.xxw.platform.plugin.auth.sdk.session.cookie;

import com.xxw.platform.plugin.auth.api.cookie.SessionCookieCreator;

import javax.servlet.http.Cookie;

/**
 * 默认的cookie创建
 * <p>
 * 这里预留了expandCookieProp的接口可以拓展cookie的属性
 *
 * @author liaoxiting
 * @date 2020/12/27 13:29
 */
public class DefaultSessionCookieCreator extends SessionCookieCreator {

    @Override
    public void expandCookieProp(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setPath("/");
    }

}
