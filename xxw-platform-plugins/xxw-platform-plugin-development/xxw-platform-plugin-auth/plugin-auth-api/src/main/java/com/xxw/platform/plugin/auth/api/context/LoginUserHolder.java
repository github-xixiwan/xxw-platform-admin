package com.xxw.platform.plugin.auth.api.context;

import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;

/**
 * 当前登录用户的临时保存容器
 *
 * @author liaoxiting
 * @date 2021/3/23 17:38
 */
public class LoginUserHolder {

    private static final ThreadLocal<LoginUser> LONGIN_USER_HOLDER = new ThreadLocal<>();

    /**
     * set holder中内容
     *
     * @author liaoxiting
     * @date 2021/3/23 17:41
     */
    public static void set(LoginUser abstractLoginUser) {
        LONGIN_USER_HOLDER.set(abstractLoginUser);
    }

    /**
     * 获取holder中的值
     *
     * @author liaoxiting
     * @date 2021/3/23 17:41
     */
    public static LoginUser get() {
        return LONGIN_USER_HOLDER.get();
    }

    /**
     * 删除保存的用户
     *
     * @author liaoxiting
     * @date 2021/3/23 17:42
     */
    public static void remove() {
        LONGIN_USER_HOLDER.remove();
    }

}
