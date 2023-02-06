package com.xxw.platform.plugin.auth.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.auth.api.LoginUserApi;

/**
 * 快速获取当前登陆用户的一系列操作方法，具体实现在Spring容器中查找
 *
 * @author liaoxiting
 * @date 2020/10/17 10:30
 */
public class LoginContext {

    public static LoginUserApi me() {
        return SpringUtil.getBean(LoginUserApi.class);
    }

}
