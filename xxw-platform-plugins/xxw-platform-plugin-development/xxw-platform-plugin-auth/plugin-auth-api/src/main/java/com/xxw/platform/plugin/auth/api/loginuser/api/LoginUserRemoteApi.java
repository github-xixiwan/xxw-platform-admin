package com.xxw.platform.plugin.auth.api.loginuser.api;

import com.xxw.platform.plugin.auth.api.loginuser.pojo.LoginUserRequest;
import com.xxw.platform.plugin.auth.api.loginuser.pojo.SessionValidateResponse;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 获取当前登录用户的远程调用方法，供微服务使用
 *
 * @author liaoxiting
 * @date 2021/9/29 10:08
 */
public interface LoginUserRemoteApi {

    /**
     * 通过token获取登录的用户
     *
     * @author liaoxiting
     * @date 2021/9/29 10:08
     */
    @RequestMapping(value = "/loginUserRemote/getLoginUserByToken", method = RequestMethod.POST)
    LoginUser getLoginUserByToken(@RequestBody LoginUserRequest loginUserRequest);

    /**
     * 判断token是否存在会话
     *
     * @author liaoxiting
     * @date 2021/9/29 11:39
     */
    @RequestMapping(value = "/loginUserRemote/haveSession", method = RequestMethod.GET)
    SessionValidateResponse haveSession(@RequestParam("token") String token);

    /**
     * 通过loginUser获取刷新后的LoginUser对象
     *
     * @author liaoxiting
     * @date 2021/9/29 11:39
     */
    @RequestMapping(value = "/loginUserRemote/getEffectiveLoginUser", method = RequestMethod.POST)
    LoginUser getEffectiveLoginUser(@RequestBody LoginUser loginUser);

}

