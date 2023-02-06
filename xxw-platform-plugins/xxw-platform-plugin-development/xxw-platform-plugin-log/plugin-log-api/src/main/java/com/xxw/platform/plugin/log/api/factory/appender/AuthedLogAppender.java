package com.xxw.platform.plugin.log.api.factory.appender;

import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;

/**
 * 日志信息追加，用来追加用户的登录信息
 *
 * @author liaoxiting
 * @date 2020/10/27 17:45
 */
public class AuthedLogAppender {

    /**
     * 填充token和userId字段
     * <p>
     * 但是此方法会依赖auth-api模块，所以用这个方法得引入auth模块
     *
     * @author liaoxiting
     * @date 2020/10/27 18:22
     */
    public static void appendAuthedHttpLog(LogRecordDTO logRecordDTO) {

        // 填充当前登录的用户信息
        try {
            // 填充登录用户的token
            logRecordDTO.setToken(LoginContext.me().getToken());

            // 填充登录用户的userId
            LoginUser loginUser = LoginContext.me().getLoginUser();
            logRecordDTO.setUserId(loginUser.getUserId());
        } catch (Exception ignored) {
            // 获取不到用户登录信息，就不填充
        }

    }

}
