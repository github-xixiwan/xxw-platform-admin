package com.xxw.platform.plugin.log.api;

import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogRequest;

/**
 * 登录日志api接口
 *
 * @author liaoxiting
 * @date 2021/1/13 11:12
 */
public interface LoginLogServiceApi {

    /**
     * 添加登录日志
     *
     * @param sysLoginLogRequest 参数
     * @author liaoxiting
     * @date 2021/1/13 10:56
     */
    void add(SysLoginLogRequest sysLoginLogRequest);

    /**
     * 增加登录成功日志
     *
     * @param userId 用户id
     * @author liaoxiting
     * @date 2021/1/13 11:36
     */
    void loginSuccess(Long userId);

    /**
     * 增加登录失败日志
     *
     * @param userId     用户id
     * @param llgMessage 错误信息
     * @author liaoxiting
     * @date 2021/1/13 11:36
     */
    void loginFail(Long userId, String llgMessage);

    /**
     * 增加退出成功日志
     *
     * @param userId 用户id
     * @author liaoxiting
     * @date 2021/1/13 11:36
     */
    void loginOutSuccess(Long userId);

    /**
     * 增加退出失败日志
     *
     * @param userId 用户id
     * @author liaoxiting
     * @date 2021/1/13 11:36
     */
    void loginOutFail(Long userId);

}
