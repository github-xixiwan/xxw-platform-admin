package com.xxw.platform.plugin.auth.sdk.config;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.config.api.ConfigInitCallbackApi;
import com.xxw.platform.plugin.message.api.expander.WebSocketConfigExpander;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目初始化完成以后，修改用户websocket地址的配置
 *
 * @author liaoxiting
 * @date 2021/10/19 17:07
 */
@Component
public class InitConfigWebSocketCallbackApiImpl implements ConfigInitCallbackApi {

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Override
    public void initBefore() {

    }

    @Override
    public void initAfter() {
        LoginUser loginUser = LoginContext.me().getLoginUser();
        loginUser.setWsUrl(WebSocketConfigExpander.getWebSocketWsUrl());
        sessionManagerApi.updateSession(loginUser.getToken(), loginUser);
    }
}
